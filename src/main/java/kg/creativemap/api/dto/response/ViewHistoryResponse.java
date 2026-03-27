package kg.creativemap.api.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewHistoryResponse {

    private Long id;
    private PlaceResponse place;
    private Integer viewCount;
    private LocalDateTime viewedAt;
}
