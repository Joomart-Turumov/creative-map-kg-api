package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.UpdateUserRequest;
import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.UserMapper;
import kg.creativemap.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getCurrentUser(User user) {
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(User user, UpdateUserRequest request) {
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }
}
