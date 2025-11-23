package com.lp3.elearning.dto;

import com.lp3.elearning.entities.RoleUsuario;

public record RegisterDTO(String login, String password, RoleUsuario role) {
}
