package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.FavoriteRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.FavoriteResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getFavorites(
            @AuthenticationPrincipal User user) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(user);
        return ResponseEntity.ok(ApiResponse.ok(favorites));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> addFavorite(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.addFavorite(user, request.getPlaceId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(favorite, "Добавлено в избранное"));
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Long placeId) {
        favoriteService.removeFavorite(user, placeId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Удалено из избранного"));
    }
}
