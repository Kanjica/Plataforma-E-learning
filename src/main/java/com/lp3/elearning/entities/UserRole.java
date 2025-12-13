package com.lp3.elearning.entities;

public enum UserRole {
    INSTRUCTOR("instructor"), // MUDANÇA AQUI
    STUDENT("student"); // MUDANÇA AQUI

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
