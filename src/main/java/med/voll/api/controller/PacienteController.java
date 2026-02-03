package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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

}
