package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreatePlaceRequest;
import kg.creativemap.api.dto.request.UpdatePlaceRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.AdminPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/places")
@RequiredArgsConstructor
public class AdminPlaceController {

    private final AdminPlaceService adminPlaceService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlaceResponse>> createPlace(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePlaceRequest request) {
        requirePermission(user, "MANAGE_CONTENT");
        PlaceResponse response = adminPlaceService.createPlace(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Место создано"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaceResponse>> updatePlace(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlaceRequest request) {
        requirePermission(user, "MANAGE_CONTENT");
        PlaceResponse response = adminPlaceService.updatePlace(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Место обновлено"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlace(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_CONTENT");
        adminPlaceService.deletePlace(user, id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Место удалено"));
    }

    private void requirePermission(User user, String permission) {
        if (!user.getRole().hasPermission(permission)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }
}
