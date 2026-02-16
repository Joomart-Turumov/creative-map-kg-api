package kg.creativemap.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private Long placeId;
    private String placeName;
    private String placeIcon;
    private String placeCity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean free;
    private String category;
}
