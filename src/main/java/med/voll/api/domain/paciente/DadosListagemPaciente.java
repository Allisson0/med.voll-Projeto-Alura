package med.voll.api.domain.paciente;

public record DadosListagemPaciente(
        String nome,
        String email,
        String cpf
) {
    //==== CONVERTE DADOS DE PACIENTE PARA DADOS DE LISTAGEM DE PACIENTES ====
    public DadosListagemPaciente(Paciente dados){
        this(dados.getNome(), dados.getEmail(), dados.getCpf());
    }
}
