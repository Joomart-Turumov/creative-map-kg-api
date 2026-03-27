package kg.creativemap.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAdminRequest {

    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Boolean active;
}
