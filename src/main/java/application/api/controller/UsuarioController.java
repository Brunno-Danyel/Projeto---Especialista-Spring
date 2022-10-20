package application.api.controller;

import application.api.dto.CredenciaisDTO;
import application.api.dto.TokenDTO;
import application.api.exeption.SenhaInvalidaExcepiton;
import application.domain.entities.Usuario;
import application.domain.entities.service.impl.UserServiceImpl;
import application.domain.repositories.UserRepository;
import application.security.jwt.JwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UserServiceImpl userService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @Autowired
    private UserRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return userService.save(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO dto) {
        try {
            Usuario usuario = Usuario.builder().login(dto.getLogin()).senha(dto.getSenha()).build();
            UserDetails usuarioAutenticado = userService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaExcepiton e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List list = repository.findAll();
        return ResponseEntity.ok().body(list);
    }
}
