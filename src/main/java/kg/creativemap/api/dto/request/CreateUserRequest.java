package kg.creativemap.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Номер карты обязателен")
    @Pattern(regexp = "\\d{14}", message = "Номер карты должен содержать ровно 14 цифр")
    private String cardNumber;

    @NotBlank(message = "ФИО обязательно")
    private String fullName;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    @NotBlank(message = "Роль обязательна")
    private String role;

    private String email;
    private String phone;
}
