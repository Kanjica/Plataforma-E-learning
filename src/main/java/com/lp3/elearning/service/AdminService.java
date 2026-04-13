package com.lp3.elearning.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.auth.AdminRegisterDTO;
import com.lp3.elearning.entities.Admin;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.repository.AdminRepository;

@Service   
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthService authService;

    public AdminService(AdminRepository adminRepository, AuthService authService) {
        this.adminRepository = adminRepository;
        this.authService = authService;
    }   

    @Transactional(readOnly = true)
    public void createAdmin(AdminRegisterDTO data) {

        authService.validateAndPrepare(data.email());

        Admin newAdmin = Admin.builder()
            .name(data.username())
            .email(data.email())
            .password(authService.encodePassword(data.password()))
            .role(UserRole.ROLE_ADMIN)
            .build();

        adminRepository.save(newAdmin);
    }
    
}
