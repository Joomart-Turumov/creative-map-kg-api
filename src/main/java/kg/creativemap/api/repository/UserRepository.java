package kg.creativemap.api.repository;

import kg.creativemap.api.entity.Role;
import kg.creativemap.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCardNumber(String cardNumber);

    boolean existsByEmail(String email);

    boolean existsByCardNumber(String cardNumber);

    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    Page<User> findByRoleIn(@Param("roles") List<Role> roles, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role IN :roles AND " +
           "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "u.cardNumber LIKE CONCAT('%', :search, '%'))")
    Page<User> findByRoleInAndSearch(@Param("roles") List<Role> roles,
                                     @Param("search") String search,
                                     Pageable pageable);
}
