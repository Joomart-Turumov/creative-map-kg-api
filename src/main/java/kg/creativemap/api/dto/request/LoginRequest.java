package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;

    private String cardNumber;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
