package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public class ValidarDisponibilidadeMedico {

    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoOcupado = repository.MedicoOcupado(dados.data(), dados.idMedico());
        if (medicoOcupado){
            throw new ValidacaoException("Médico já possui consulta agendada para esse mesmo horário.");
        }
    }
}
