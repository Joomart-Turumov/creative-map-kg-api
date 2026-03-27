package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVrTourRequest {

    @NotBlank(message = "Название обязательно")
    private String title;

    private String description;

    @NotBlank(message = "Ссылка на VR-тур обязательна")
    private String iframeUrl;

    private String thumbnailUrl;

    private String address;

    private String category;

    private String region;

    private String status;
}
