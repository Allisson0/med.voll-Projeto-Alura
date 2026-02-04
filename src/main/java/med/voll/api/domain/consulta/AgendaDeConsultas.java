package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados){

        // Verifica se os ids de médico e paciente, existem no
        // banco de dados
        if (!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente informado não existe!");
        }

        // Para verificar o id do médico, é necessário que tenha vindo
        // algum id no corpo da requisição.
        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico informado não existe!");
        }

        // Recuperar os objetos paciente e médico para inserção no
        // objeto consulta a ser criado.
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        // Retorna um objeto do tipo Medico de duas formas:
        // 1 - haja um idMedico válido presente em dados, busca o médico
        // correspondente.
        // 2 - nenhum idMedico no corpo (null), retorna um médico aleatório
        // do banco de dados.
        var medico = escolherMedico(dados);

        // Construtor com todos os argumentos vindo de Lombok
        // Passamos o ID como nulo, pois o JPA irá gerá-lo no
        // momento da criação da classe.
        var consulta = new Consulta(null, medico, paciente, dados.data());

        consultaRepository.save(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() !=  null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido!");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }

}
