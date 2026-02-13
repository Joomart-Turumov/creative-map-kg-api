package kg.creativemap.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceTypeCount {

    private String type;
    private Long count;
}
