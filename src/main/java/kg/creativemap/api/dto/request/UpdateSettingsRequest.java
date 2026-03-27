package kg.creativemap.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSettingsRequest {

    private String accentColor;
    private String language;
    private Boolean darkTheme;
    private Boolean pushNotifications;
    private Boolean emailNotifications;
}
