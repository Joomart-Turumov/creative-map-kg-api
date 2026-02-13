package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.UpdateUserRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal User user) {
        UserResponse response = userService.getCurrentUser(user);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(user, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Профиль обновлён"));
    }
}
