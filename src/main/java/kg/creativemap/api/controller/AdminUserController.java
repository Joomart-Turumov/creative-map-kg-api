package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreateUserRequest;
import kg.creativemap.api.dto.request.UpdateUserAdminRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        requirePermission(user, "MANAGE_USERS");
        PageResponse<UserResponse> response = adminUserService.getUsers(user, search, page, size);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateUserRequest request) {
        requirePermission(user, "MANAGE_USERS");
        UserResponse response = adminUserService.createUser(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Пользователь создан"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserAdminRequest request) {
        requirePermission(user, "MANAGE_USERS");
        UserResponse response = adminUserService.updateUser(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Пользователь обновлён"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_USERS");
        adminUserService.deleteUser(user, id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Пользователь удалён"));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<ApiResponse<UserResponse>> blockUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_USERS");
        UpdateUserAdminRequest request = new UpdateUserAdminRequest();
        request.setActive(false);
        UserResponse response = adminUserService.updateUser(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Пользователь заблокирован"));
    }

    @PostMapping("/{id}/unblock")
    public ResponseEntity<ApiResponse<UserResponse>> unblockUser(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        requirePermission(user, "MANAGE_USERS");
        UpdateUserAdminRequest request = new UpdateUserAdminRequest();
        request.setActive(true);
        UserResponse response = adminUserService.updateUser(user, id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Пользователь разблокирован"));
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRoles(
            @AuthenticationPrincipal User user) {
        requirePermission(user, "MANAGE_USERS");
        List<Map<String, Object>> roles = Arrays.stream(Role.values())
                .map(role -> {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("name", role.name());
                    info.put("level", role.level());
                    info.put("permissions", role.getPermissions());
                    info.put("defaultLanguage", role.getDefaultLanguage());
                    return info;
                })
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(roles));
    }

    private void requirePermission(User user, String permission) {
        if (!user.getRole().hasPermission(permission)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }
}
