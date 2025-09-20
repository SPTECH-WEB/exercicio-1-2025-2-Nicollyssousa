package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpfLikeOrEmailLikeIgnoreCase(String cpf, String email);

    List<Usuario> findByDataNascimentoGreaterThan(LocalDate dataNascimento);

    boolean existsByCpfLike(String cpf);

    boolean existsByEmailLike(String email);
}
