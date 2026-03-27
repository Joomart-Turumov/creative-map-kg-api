package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreateEventRequest;
import kg.creativemap.api.dto.request.UpdateEventRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.EventResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> getAllEvents(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        requireMinRole(user, Role.CONTENT_MAKER);
        PageResponse<EventResponse> response = adminEventService.getAllEvents(search, page, size);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateEventRequest request) {
        requireMinRole(user, Role.CONTENT_MAKER);
        EventResponse response = adminEventService.createEvent(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Мероприятие создано"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request) {
        requireMinRole(user, Role.CONTENT_MAKER);
        EventResponse response = adminEventService.updateEvent(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Мероприятие обновлено"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requireMinRole(user, Role.CONTENT_MAKER);
        adminEventService.deleteEvent(user, id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Мероприятие удалено"));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<EventResponse>> toggleStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requireMinRole(user, Role.CONTENT_MAKER);
        EventResponse response = adminEventService.toggleStatus(user, id);
        return ResponseEntity.ok(ApiResponse.ok(response, "Статус изменён"));
    }

    private void requireMinRole(User user, Role minRole) {
        if (!user.getRole().isAboveOrEqual(minRole)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }
}
