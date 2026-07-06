package com.wagnerdf.comprar.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.annotation.Auditable;
import com.wagnerdf.comprar.dto.request.CreateEmployeeRequest;
import com.wagnerdf.comprar.dto.request.UpdateEmployeeRequest;
import com.wagnerdf.comprar.dto.response.EmployeeResponse;
import com.wagnerdf.comprar.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request
    ) {

        employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<Page<EmployeeResponse>> findAll(
            @PageableDefault(size = 10, sort = "username") Pageable pageable
    ) {

        return ResponseEntity.ok(
                employeeService.findAll(pageable)
        );

    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> findById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                employeeService.findById(id)
        );

    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
    @Auditable(action = "UPDATE_EMPLOYEE")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody UpdateEmployeeRequest request
    ) {

        employeeService.updateEmployee(id, request);

        return ResponseEntity.ok().build();

    }

}
