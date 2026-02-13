package kg.creativemap.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponse {

    private Long id;
    private String nameKy;
    private String nameRu;
    private String nameEn;
    private String type;
    private Double latitude;
    private Double longitude;
    private String city;
    private String region;
    private Double rating;
    private Boolean vrAvailable;
    private String icon;
    private String address;
    private String descriptionKy;
    private String descriptionRu;
    private String descriptionEn;
    private String photoUrl;
    private String vrUrl;
}
