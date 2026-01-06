package com.citabella.citabellaapi.controller.auth;

import com.citabella.citabellaapi.dto.usuario.UsuarioRequest;
import com.citabella.citabellaapi.dto.usuario.UsuarioResponse;
import com.citabella.citabellaapi.entity.seguridad.Usuario;
import com.citabella.citabellaapi.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //@GetMapping("/me")
    public UsuarioResponse me() {

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

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody @Valid UsuarioRequest request) {

        Usuario usuario = usuarioService.crearUsuario(
                request.nombreUsuario(),
                request.email(),
                request.password()
        );

        UsuarioResponse response = new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getRol().getNombre(),
                false
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
