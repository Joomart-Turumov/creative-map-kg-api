package kg.creativemap.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    private String title;
    private String description;
    private Long placeId;
    private String startDate;
    private String endDate;
    private Boolean free;
    private String category;
    private Boolean active;
}
