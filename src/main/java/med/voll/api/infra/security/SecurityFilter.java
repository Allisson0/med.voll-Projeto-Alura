package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repository.findByLogin(subject);

            //Passamos um objeto Spring Security de usuário com informações
            //de user, credenciais e autorizações do usuário.
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            //Autenticamos o usuário para o Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //Dispara o próximo filtro (ou envia a requisição para o Controller)
        filterChain.doFilter(request, response);

    }

    //Recupera o token para validação e retorna para autenticação
    //Se não houver token, o Spring Security cuidará disto e verificará
    //Se é uma requisição do tipo Post para /login, se for, passará
    //se não for, dará erro 403 - forbidden
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}