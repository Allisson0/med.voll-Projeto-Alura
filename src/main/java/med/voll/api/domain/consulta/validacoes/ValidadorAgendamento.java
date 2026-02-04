package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamento {
    void validar(DadosAgendamentoConsulta dados);
}
