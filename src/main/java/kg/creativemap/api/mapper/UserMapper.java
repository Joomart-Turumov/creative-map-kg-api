package kg.creativemap.api.mapper;

import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .cardNumber(user.getCardNumber())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .active(user.getActive())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .accentColor(user.getAccentColor())
                .language(user.getLanguage())
                .darkTheme(user.getDarkTheme())
                .pushNotifications(user.getPushNotifications())
                .emailNotifications(user.getEmailNotifications())
                .permissions(user.getRole().getPermissions())
                .defaultLanguage(user.getRole().getDefaultLanguage())
                .build();
    }
}
