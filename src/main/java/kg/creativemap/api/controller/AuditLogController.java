package kg.creativemap.api.controller;

import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.entity.AuditLog;
import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AuditLog>>> getLogs(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        if (user.getRole() != Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Только супер-администратор может просматривать журнал");
        }
        PageResponse<AuditLog> response = auditLogService.getLogs(page, size);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
