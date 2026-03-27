package kg.creativemap.api.config;

import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.card-number:00000000000001}")
    private String adminCardNumber;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.email:superadmin@creativemap.kg}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (userRepository.findByCardNumber(adminCardNumber).isEmpty()) {
            User superAdmin = User.builder()
                    .email(adminEmail)
                    .cardNumber(adminCardNumber)
                    .password(passwordEncoder.encode(adminPassword))
                    .fullName("Супер Администратор")
                    .role(Role.SUPER_ADMIN)
                    .active(true)
                    .build();

            try {
                userRepository.save(superAdmin);
                log.info("Супер-администратор создан: card_number={}", adminCardNumber);
            } catch (Exception e) {
                log.debug("Супер-администратор уже существует");
            }
        }
    }
}
