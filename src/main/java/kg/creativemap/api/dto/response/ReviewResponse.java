package kg.creativemap.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private String userAvatarUrl;
    private Long placeId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
