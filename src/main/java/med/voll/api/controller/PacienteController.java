package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/paciente")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    //Repositório do paciente
    @Autowired
    private PacienteRepository repository;

    //==== CADASTRAR PACIENTE ====
    @PostMapping
    @Transactional
    public ResponseEntity cadastrarPaciente(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder){
        //Recupera do corpo, os dados de paciente, desde que estejam válidos
        var paciente = new Paciente(dados);
        //Salva o paciente no repositório
        repository.save(paciente);

        //Retorna os dados do paciente no DTO de pacientes e retorna
        //o acesso de detalhamento dos pacientes via uriBuilder.path
        var uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));

    }

    //==== DETALHAR PACIENTE ====
    @GetMapping("/{id}")
    public ResponseEntity detalharPaciente(@PathVariable Long id){
        //Via id HTTP da requisição, recupera um paciente e
        // mostra código 200 com os dados dele
        var paciente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    //==== LISTAR PACIENTE ====
    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listarPaciente(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable paginacao){
        //Para cada paciente, retorna seus dados para listagem
        // (nome, email e cpf), desde que esteja ativo
        // a página segue o seu padrão de 10 pacientes por página e
        // em ordem crescente do nome.
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);

        //Retorna a página
        return ResponseEntity.ok(page);
    }

    //==== ATUALIZAR PACIENTE ====
    @PutMapping
    @Transactional
    public ResponseEntity atualizarPaciente(@Valid @RequestBody DadosAtualizaPaciente dados){
        //Recupera a referência de paciente, por meio do Id
        // recebido do DadosAtualizaPaciente do body
        // só é possível atualizar nome, telefone e o endereço
        // do paciente. Além de necessitar o campo "id" indicando
        // o paciente a ser alterado
        var paciente = repository.getReferenceById(dados.id());
        //Atualiza estes dados
        paciente.atualizaPaciente(dados);

        //Retorna os dados atualizados
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    //==== REMOVER PACIENTE ====
    @DeleteMapping("/{id}")
    //@Transational para comunicar mudanças para o banco de dados
    @Transactional
    public ResponseEntity deleterPaciente(@PathVariable Long id){
        //Com base na referência do id,
        // exclui o paciente
        var paciente = repository.getReferenceById(id);
        paciente.excluir();

        //Retorna código 204 e sem build
        return ResponseEntity.noContent().build();
    }

}
