package com.lp3.elearning.entities;

public enum RoleUsuario {
    INSTRUTOR("instrutor"),
    ALUNO("aluno");

    private String role;

    RoleUsuario(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
