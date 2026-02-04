package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamento;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    //Tratador de erros para EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        //Retorna um erro 404 - notFound
        return ResponseEntity.notFound().build();
    }

    //Tratando erros de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){

        //Recupera os campos e erros
        var erros = ex.getFieldErrors();

        //Retorna o erro 400, e apresenta no corpo a lista de DadosErroValidacao
        //com campo de erro e mensagem de erro.
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    //Record privado interno de Tratador de Erros
    private record DadosErroValidacao(String campo, String mensagem){
        //Recebendo FieldError, pega apenas o campo e a mensagem para retorno
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroRegraDeNegocio(ValidacaoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
