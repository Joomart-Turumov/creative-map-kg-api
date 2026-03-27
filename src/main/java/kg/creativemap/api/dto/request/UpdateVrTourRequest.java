package kg.creativemap.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVrTourRequest {

    private String title;
    private String description;
    private String iframeUrl;
    private String thumbnailUrl;
    private String address;
    private String category;
    private String region;
    private String status;
}
