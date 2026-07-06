package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.CreateEmployeeRequest;
import com.wagnerdf.comprar.dto.request.UpdateEmployeeRequest;
import com.wagnerdf.comprar.dto.response.EmployeeResponse;
import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.EmployeeNotFoundException;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

        private final AuthRepository authRepository;
        private final UserRepository userRepository;
        private final PermissionService permissionService;
        private final AuditService auditService;
        private final AuthenticatedUserService authenticatedUserService;
        private final PasswordEncoder passwordEncoder;

    
    @Transactional
    public void createEmployee(CreateEmployeeRequest request) {

        // =========================
        // VALIDAÇÕES
        // =========================
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        if (authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("Username já cadastrado");
        }

        // =========================
        // MAPPER
        // =========================
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .build();

        // =========================
        // REGRAS DE NEGÓCIO
        // =========================
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        // =========================
        // PERMISSÕES PADRÃO
        // =========================
        Set<Permission> permissions =
                permissionService.getPermissionsByRole(Role.EMPLOYEE);

        // =========================
        // ROLE PADRÃO
        // =========================
        Role role = Role.EMPLOYEE;

        // =========================
        // AUTH
        // =========================
        Auth auth = Auth.builder()
                .user(savedUser)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .permissions(permissions)
                .build();

        authRepository.save(auth);

        // =========================
        // AUDITORIA
        // =========================
        auditService.log(
	            authenticatedUserService.getCurrentUsername(),
	            "CREATE_EMPLOYEE"
	    );
    }
    
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAll(Pageable pageable) {

        return authRepository.findByRole(Role.EMPLOYEE, pageable)
                .map(auth -> EmployeeResponse.builder()
                        .id(auth.getUser().getId())
                        .name(auth.getUser().getName())
                        .email(auth.getUser().getEmail())
                        .birthDate(auth.getUser().getBirthDate())
                        .gender(auth.getUser().getGender())
                        .active(auth.getUser().getActive())
                        .username(auth.getUsername())
                        .build());
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponse findById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Funcionário não encontrado"));

        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new EmployeeNotFoundException("Funcionário não encontrado"));

        if (auth.getRole() != Role.EMPLOYEE) {
            throw new EmployeeNotFoundException("Funcionário não encontrado");
        }

        return EmployeeResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .active(user.getActive())
                .username(auth.getUsername())
                .build();
    }
    
    @Transactional
    public void updateEmployee(String id, UpdateEmployeeRequest request) {

        // =========================
        // BUSCA USER
        // =========================
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // BUSCA AUTH
        // =========================
        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // VALIDA SE É EMPLOYEE
        // =========================
        if (auth.getRole() != Role.EMPLOYEE) {
            throw new EmployeeNotFoundException("Funcionário não encontrado");
        }

        // =========================
        // ATUALIZA CAMPOS
        // =========================
        user.setName(request.getName().trim());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

    }
    
    @Transactional
    public void deleteEmployee(String id) {

        // =========================
        // BUSCA USER
        // =========================
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // BUSCA AUTH
        // =========================
        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // VALIDA ROLE
        // =========================
        if (auth.getRole() != Role.EMPLOYEE) {
            throw new EmployeeNotFoundException("Funcionário não encontrado");
        }

        // =========================
        // SOFT DELETE
        // =========================
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

    }
    
    @Transactional
    public void reactivateEmployee(String id) {

        // =========================
        // BUSCA USER
        // =========================
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // BUSCA AUTH
        // =========================
        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Funcionário não encontrado"));

        // =========================
        // VALIDA ROLE
        // =========================
        if (auth.getRole() != Role.EMPLOYEE) {
            throw new EmployeeNotFoundException("Funcionário não encontrado");
        }

        // =========================
        // REATIVA
        // =========================
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

    }

}
