package med.voll.api.domain.paciente;

import med.voll.api.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Usuario, Long> {



}
