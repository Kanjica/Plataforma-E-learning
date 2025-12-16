package com.lp3.elearning.entities;

public enum UserRole {
    ROLE_INSTRUCTOR("ROLE_INSTRUCTOR"),
    ROLE_STUDENT("ROLE_STUDENT");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
