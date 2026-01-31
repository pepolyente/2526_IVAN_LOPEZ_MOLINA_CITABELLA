package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.dto.user.UsuarioRequest;
import com.citabella.citabellaapi.dto.user.UsuarioResponse;
import com.citabella.citabellaapi.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UserService userService;

    public UsuarioController(UserService userService) {
        this.userService = userService;
    }

    //@GetMapping("/me")
    /*public UsuarioResponse me() {

        Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

        boolean tieneCliente = usuarioService.tieneCliente(usuario.getIdUsuario());

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getRol().getNombre(),
                tieneCliente
        );
    }
    */

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }
}
