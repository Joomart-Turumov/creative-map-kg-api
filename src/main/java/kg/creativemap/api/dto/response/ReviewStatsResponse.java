package kg.creativemap.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewStatsResponse {

    private Double averageRating;
    private Long totalReviews;
}
