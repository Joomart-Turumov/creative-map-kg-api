package kg.creativemap.api.dto.request;

import lombok.*;

/** Частичное обновление места — все поля опциональны (обновляются только переданные). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlaceRequest {

    private String nameKy;
    private String nameRu;
    private String nameEn;
    private String type;
    private Double latitude;
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
