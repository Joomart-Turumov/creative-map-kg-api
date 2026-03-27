package kg.creativemap.api.mapper;

import kg.creativemap.api.dto.response.ReviewResponse;
import kg.creativemap.api.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .userAvatarUrl(review.getUser().getAvatarUrl())
                .placeId(review.getPlace().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
