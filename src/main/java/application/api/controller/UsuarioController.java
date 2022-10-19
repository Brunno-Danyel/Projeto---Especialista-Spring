package application.api.controller;

import application.api.dto.CredenciaisDTO;
import application.api.dto.TokenDTO;
import application.api.exeption.SenhaInvalidaExcepiton;
import application.domain.entities.Usuario;
import application.domain.entities.service.impl.UserServiceImpl;
import application.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private final UserServiceImpl userService;
    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return userService.save(usuario);
    }

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
}
