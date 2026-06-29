package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceRequest {

    @NotBlank(message = "Название (ky) обязательно")
    private String nameKy;

    private String nameRu;
    private String nameEn;

    @NotBlank(message = "Тип обязателен")
    private String type;

    @NotNull(message = "Широта обязательна")
    private Double latitude;

    @NotNull(message = "Долгота обязательна")
    private Double longitude;

    private String city;
    private String region;
    private String address;
    private String descriptionKy;
    private String descriptionRu;
    private String descriptionEn;
    private String photoUrl;
    private String vrUrl;
    private Boolean vrAvailable;
    private String icon;
}
