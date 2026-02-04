package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarDisponibilidadeMedico implements ValidadorAgendamento{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoOcupado = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
        if (medicoOcupado){
            throw new ValidacaoException("Médico já possui consulta agendada para esse mesmo horário.");
        }
    }
}
