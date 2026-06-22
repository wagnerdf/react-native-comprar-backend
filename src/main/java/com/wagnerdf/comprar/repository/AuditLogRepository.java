package com.wagnerdf.comprar.repository;

import com.wagnerdf.comprar.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}
