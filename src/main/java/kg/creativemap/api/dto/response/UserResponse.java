package kg.creativemap.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String cardNumber;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String role;
    private Boolean active;
    private String createdAt;

    // Settings
    private String accentColor;
    private String language;
    private Boolean darkTheme;
    private Boolean pushNotifications;
    private Boolean emailNotifications;

    // Permissions
    private java.util.List<String> permissions;
    private String defaultLanguage;
}
