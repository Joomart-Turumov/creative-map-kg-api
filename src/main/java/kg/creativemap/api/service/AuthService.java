package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.LoginRequest;
import kg.creativemap.api.dto.request.RegisterRequest;
import kg.creativemap.api.dto.response.AuthResponse;
import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.RefreshToken;
import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ConflictException;
import kg.creativemap.api.mapper.UserMapper;
import kg.creativemap.api.repository.RefreshTokenRepository;
import kg.creativemap.api.repository.UserRepository;
import kg.creativemap.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(Role.USER)
                .active(true)
                .build();
        userRepository.save(user);

        return generateTokenPair(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Неверный email или пароль"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверный email или пароль");
        }

        if (!user.getActive()) {
            throw new BadCredentialsException("Аккаунт деактивирован");
        }

        return generateTokenPair(user);
    }

    @Transactional
    public AuthResponse refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new BadCredentialsException("Невалидный refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadCredentialsException("Refresh token истёк");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);

        return generateTokenPair(user);
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        refreshTokenRepository.findByToken(refreshTokenStr)
                .ifPresent(refreshTokenRepository::delete);
    }

    private AuthResponse generateTokenPair(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(
                        jwtTokenProvider.getRefreshTokenExpiration() / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);

        UserResponse userResponse = userMapper.toResponse(user);
        return AuthResponse.of(accessToken, refreshTokenStr,
                jwtTokenProvider.getAccessTokenExpiration() / 1000, userResponse);
    }
}
