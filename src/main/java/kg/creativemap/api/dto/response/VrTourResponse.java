package kg.creativemap.api.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VrTourResponse {

    private Long id;
    private String title;
    private String description;
    private String iframeUrl;
    private String thumbnailUrl;
    private String address;
    private String category;
    private String region;
    private String status;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
