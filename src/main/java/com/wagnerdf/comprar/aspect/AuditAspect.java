package com.wagnerdf.comprar.aspect;

import com.wagnerdf.comprar.annotation.Auditable;
import com.wagnerdf.comprar.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @AfterReturning("@annotation(auditable)")
    public void logAction(JoinPoint joinPoint, Auditable auditable) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        auditService.log(username, auditable.action());
    }
}
