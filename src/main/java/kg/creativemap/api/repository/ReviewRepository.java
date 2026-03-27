package kg.creativemap.api.repository;

import kg.creativemap.api.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.place WHERE r.place.id = :placeId ORDER BY r.createdAt DESC")
    Page<Review> findByPlaceIdWithUser(Long placeId, Pageable pageable);

    Optional<Review> findByUserIdAndPlaceId(Long userId, Long placeId);

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    long countByPlaceId(Long placeId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.place.id = :placeId")
    Double getAverageRatingByPlaceId(Long placeId);
}
