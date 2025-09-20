package school.sptech.prova_ac1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuariosEncontrados = repository.findAll();

        if(usuariosEncontrados.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuariosEncontrados);
        }

       return ResponseEntity.ok(usuariosEncontrados);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        boolean existeCpfOuEmail = repository.existsByCpfLikeOrEmailLikeIgnoreCase(usuario.getCpf(), usuario.getEmail());
        if(existeCpfOuEmail){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Optional<Usuario> usuarioPeloId  = repository.findById(id);

        if(usuarioPeloId.isPresent()){
            return ResponseEntity.ok(usuarioPeloId.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean usuarioPeloId = repository.existsById(id);

        if(usuarioPeloId){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        List<Usuario> usuariosComDataMaiorQue = repository.findByDataNascimentoGreaterThan(nascimento);

        if(usuariosComDataMaiorQue.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuariosComDataMaiorQue);
        }
        return ResponseEntity.ok(usuariosComDataMaiorQue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable
            Integer id,
            @RequestBody
            Usuario usuario
    ) {
        Optional<Usuario> existeusuarioPeloId = repository.findById(id);
        boolean existeCpf = repository.existsByCpfLike(usuario.getCpf());
        boolean existeEmail = repository.existsByEmailLike(usuario.getEmail());

        if(existeusuarioPeloId.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuarioExistente = existeusuarioPeloId.get();
        if((existeEmail && !usuario.getEmail().equals(usuarioExistente.getEmail()) ||
                (existeCpf && !usuario.getCpf().equals(usuarioExistente.getCpf())))){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuario.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(usuario));
    }
}
