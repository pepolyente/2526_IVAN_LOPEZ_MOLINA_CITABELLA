package com.citabella.citabellaapi.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponse {

    private Integer idUsuario;

    private String nombreUsuario;

    private String email;

    private String rol;

    private boolean tieneCliente;
}
