package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.CreateUserRequest;
import kg.creativemap.api.dto.request.UpdateUserAdminRequest;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.UserResponse;
import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ConflictException;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.UserMapper;
import kg.creativemap.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsers(User currentUser, String search, int page, int size) {
        List<Role> visibleRoles = getVisibleRoles(currentUser.getRole());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> usersPage;
        if (search != null && !search.isBlank()) {
            usersPage = userRepository.findByRoleInAndSearch(visibleRoles, search, pageRequest);
        } else {
            usersPage = userRepository.findByRoleIn(visibleRoles, pageRequest);
        }

        List<UserResponse> content = usersPage.getContent().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(usersPage, content);
    }

    @Transactional
    public UserResponse createUser(User currentUser, CreateUserRequest request) {
        Role targetRole = Role.valueOf(request.getRole().toUpperCase());

        if (targetRole == Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Невозможно создать супер-администратора");
        }

        if (!currentUser.getRole().isAbove(targetRole)) {
            throw new AccessDeniedException("Недостаточно прав для создания пользователя с данной ролью");
        }

        if (userRepository.existsByCardNumber(request.getCardNumber())) {
            throw new ConflictException("Пользователь с таким номером карты уже существует");
        }

        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            email = request.getCardNumber() + "@card.creativemap.kg";
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User newUser = User.builder()
                .cardNumber(request.getCardNumber())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(targetRole)
                .active(true)
                .createdBy(currentUser)
                .build();
        userRepository.save(newUser);

        auditLogService.log(currentUser, "CREATE_USER", "User", newUser.getId(),
                "Создан пользователь: " + newUser.getFullName() + " (" + targetRole + ")");

        return userMapper.toResponse(newUser);
    }

    @Transactional
    public UserResponse updateUser(User currentUser, Long targetId, UpdateUserAdminRequest request) {
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        validateHierarchy(currentUser, target);

        if (request.getRole() != null) {
            Role newRole = Role.valueOf(request.getRole().toUpperCase());
            if (newRole == Role.SUPER_ADMIN) {
                throw new AccessDeniedException("Невозможно назначить роль супер-администратора");
            }
            if (!currentUser.getRole().isAbove(newRole)) {
                throw new AccessDeniedException("Недостаточно прав для назначения данной роли");
            }
            target.setRole(newRole);
        }

        if (request.getFullName() != null) target.setFullName(request.getFullName());
        if (request.getEmail() != null) target.setEmail(request.getEmail());
        if (request.getPhone() != null) target.setPhone(request.getPhone());
        if (request.getActive() != null) target.setActive(request.getActive());

        userRepository.save(target);

        auditLogService.log(currentUser, "UPDATE_USER", "User", target.getId(),
                "Обновлён пользователь: " + target.getFullName());

        return userMapper.toResponse(target);
    }

    @Transactional
    public void deleteUser(User currentUser, Long targetId) {
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        if (target.getRole() == Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Невозможно удалить супер-администратора");
        }

        validateHierarchy(currentUser, target);

        String targetName = target.getFullName();
        userRepository.delete(target);

        auditLogService.log(currentUser, "DELETE_USER", "User", targetId,
                "Удалён пользователь: " + targetName);
    }

    private void validateHierarchy(User currentUser, User target) {
        if (target.getRole() == Role.SUPER_ADMIN) {
            throw new AccessDeniedException("Невозможно управлять супер-администратором");
        }
        if (!currentUser.getRole().isAbove(target.getRole())) {
            throw new AccessDeniedException("Недостаточно прав для управления этим пользователем");
        }
    }

    private List<Role> getVisibleRoles(Role currentRole) {
        return Arrays.stream(Role.values())
                .filter(r -> currentRole.isAbove(r))
                .collect(Collectors.toList());
    }
}
