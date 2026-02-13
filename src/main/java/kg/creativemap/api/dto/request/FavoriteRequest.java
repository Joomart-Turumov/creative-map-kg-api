package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {

    @NotNull(message = "ID локации обязателен")
    private Long placeId;
}
