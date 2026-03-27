package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

    @NotBlank(message = "Название обязательно")
    private String title;

    private String description;

    private Long placeId;

    private String startDate;

    private String endDate;

    private Boolean free;

    private String category;

    private String status;
}
