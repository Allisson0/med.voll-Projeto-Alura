package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("""
            SELECT c.data = :data
            FROM Consulta c
            WHERE
            c.medico.id = :idMedico
            """)
    Boolean MedicoOcupado(LocalDateTime data, Long idMedico);

    Boolean existsByPacienteIdAndDataBetween(Long pacienteId, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);
}
