package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreateVrTourRequest;
import kg.creativemap.api.dto.request.UpdateVrTourRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.VrTourResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.VrTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/vr-tours")
@RequiredArgsConstructor
public class AdminVrTourController {

    private final VrTourService vrTourService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VrTourResponse>>> getAllTours(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        requirePermission(user, "MANAGE_VR_TOURS");
        PageResponse<VrTourResponse> response = vrTourService.getAllTours(search, status, page, size);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VrTourResponse>> createTour(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateVrTourRequest request) {
        requirePermission(user, "MANAGE_VR_TOURS");
        VrTourResponse response = vrTourService.createTour(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "VR-тур успешно добавлен"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VrTourResponse>> updateTour(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateVrTourRequest request) {
        requirePermission(user, "MANAGE_VR_TOURS");
        VrTourResponse response = vrTourService.updateTour(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "VR-тур обновлён"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTour(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_VR_TOURS");
        vrTourService.deleteTour(user, id);
        return ResponseEntity.ok(ApiResponse.ok(null, "VR-тур удалён"));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<VrTourResponse>> toggleStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_VR_TOURS");
        VrTourResponse response = vrTourService.toggleStatus(user, id);
        return ResponseEntity.ok(ApiResponse.ok(response, "Статус изменён"));
    }

    private void requirePermission(User user, String permission) {
        if (!user.getRole().hasPermission(permission)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }
}
