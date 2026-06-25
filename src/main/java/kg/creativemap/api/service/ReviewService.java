package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.CreateReviewRequest;
import kg.creativemap.api.dto.request.UpdateReviewRequest;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.ReviewResponse;
import kg.creativemap.api.dto.response.ReviewStatsResponse;
import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.Review;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ConflictException;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.ReviewMapper;
import kg.creativemap.api.repository.PlaceRepository;
import kg.creativemap.api.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getReviewsByPlace(Long placeId, int page, int size) {
        Page<Review> reviews = reviewRepository.findByPlaceIdWithUser(placeId, PageRequest.of(page, size));
        return PageResponse.of(reviews, reviews.getContent().stream().map(reviewMapper::toResponse).toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getMyReviews(User user, int page, int size) {
        Page<Review> reviews = reviewRepository.findByUserIdWithPlace(user.getId(), PageRequest.of(page, size));
        return PageResponse.of(reviews, reviews.getContent().stream().map(reviewMapper::toResponse).toList());
    }

    @Transactional(readOnly = true)
    public ReviewStatsResponse getStats(Long placeId) {
        return ReviewStatsResponse.builder()
                .averageRating(reviewRepository.getAverageRatingByPlaceId(placeId))
                .totalReviews(reviewRepository.countByPlaceId(placeId))
                .build();
    }

    @Transactional(readOnly = true)
    public ReviewResponse getUserReview(User user, Long placeId) {
        Review review = reviewRepository.findByUserIdAndPlaceId(user.getId(), placeId)
                .orElse(null);
        return review != null ? reviewMapper.toResponse(review) : null;
    }

    @Transactional
    public ReviewResponse createReview(User user, CreateReviewRequest request) {
        if (reviewRepository.existsByUserIdAndPlaceId(user.getId(), request.getPlaceId())) {
            throw new ConflictException("Вы уже оставили отзыв для этой локации");
        }

        Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Локация не найдена: " + request.getPlaceId()));

        Review review = Review.builder()
                .user(user)
                .place(place)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);
        updatePlaceRating(place.getId());

        return reviewMapper.toResponse(review);
    }

    @Transactional
    public ReviewResponse updateReview(User user, Long reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new ConflictException("Вы не можете редактировать чужой отзыв");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);
        updatePlaceRating(review.getPlace().getId());

        return reviewMapper.toResponse(review);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден: " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new ConflictException("Вы не можете удалить чужой отзыв");
        }

        Long placeId = review.getPlace().getId();
        reviewRepository.delete(review);
        updatePlaceRating(placeId);
    }

    private void updatePlaceRating(Long placeId) {
        Double avg = reviewRepository.getAverageRatingByPlaceId(placeId);
        Place place = placeRepository.findById(placeId).orElse(null);
        if (place != null) {
            double rating = (avg != null) ? Math.round(avg * 10.0) / 10.0 : 0.0;
            place.setRating(rating);
            placeRepository.save(place);
        }
    }
}
