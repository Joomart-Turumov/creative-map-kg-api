package kg.creativemap.api.controller;

import jakarta.validation.Valid;
import kg.creativemap.api.dto.request.CreateReviewRequest;
import kg.creativemap.api.dto.request.UpdateReviewRequest;
import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.ReviewResponse;
import kg.creativemap.api.dto.response.ReviewStatsResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getReviews(
            @PathVariable Long placeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ReviewResponse> reviews = reviewService.getReviewsByPlace(placeId, page, size);
        return ResponseEntity.ok(ApiResponse.ok(reviews));
    }

    @GetMapping("/place/{placeId}/stats")
    public ResponseEntity<ApiResponse<ReviewStatsResponse>> getStats(
            @PathVariable Long placeId) {
        ReviewStatsResponse stats = reviewService.getStats(placeId);
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getMyReviews(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ReviewResponse> reviews = reviewService.getMyReviews(user, page, size);
        return ResponseEntity.ok(ApiResponse.ok(reviews));
    }

    @GetMapping("/my/place/{placeId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getMyReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long placeId) {
        ReviewResponse review = reviewService.getUserReview(user, placeId);
        return ResponseEntity.ok(ApiResponse.ok(review));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse review = reviewService.createReview(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(review, "Отзыв успешно создан"));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewResponse review = reviewService.updateReview(user, reviewId, request);
        return ResponseEntity.ok(ApiResponse.ok(review, "Отзыв обновлён"));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(user, reviewId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Отзыв удалён"));
    }
}
