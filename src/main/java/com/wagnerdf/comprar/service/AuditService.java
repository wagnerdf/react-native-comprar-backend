package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.entity.AuditLog;
import com.wagnerdf.comprar.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String username, String action) {

        auditLogRepository.save(
                AuditLog.builder()
                        .username(username)
                        .action(action)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
