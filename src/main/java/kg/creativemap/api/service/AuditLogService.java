package kg.creativemap.api.service;

import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.entity.AuditLog;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void log(User actor, String action, String entityType, Long entityId, String details) {
        AuditLog entry = AuditLog.builder()
                .userId(actor.getId())
                .userName(actor.getFullName())
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();
        auditLogRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLog> getLogs(int page, int size) {
        Page<AuditLog> pageResult = auditLogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        List<AuditLog> content = pageResult.getContent();
        return PageResponse.of(pageResult, content);
    }
}
