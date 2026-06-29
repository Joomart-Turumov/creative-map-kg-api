package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreatePlaceRequest;
import kg.creativemap.api.dto.request.UpdatePlaceRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.entity.Role;
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
        requireMinRole(user, Role.CONTENT_MAKER);
        PlaceResponse response = adminPlaceService.createPlace(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Место создано"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaceResponse>> updatePlace(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlaceRequest request) {
        requireMinRole(user, Role.CONTENT_MAKER);
        PlaceResponse response = adminPlaceService.updatePlace(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Место обновлено"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlace(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requireMinRole(user, Role.CONTENT_MAKER);
        adminPlaceService.deletePlace(user, id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Место удалено"));
    }

    private void requireMinRole(User user, Role minRole) {
        if (!user.getRole().isAboveOrEqual(minRole)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }
}
