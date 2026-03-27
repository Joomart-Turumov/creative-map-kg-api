package kg.creativemap.api.repository;

import kg.creativemap.api.entity.ViewHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    @Query("SELECT vh FROM ViewHistory vh JOIN FETCH vh.place WHERE vh.user.id = :userId ORDER BY vh.viewedAt DESC")
    Page<ViewHistory> findByUserIdWithPlace(Long userId, Pageable pageable);

    Page<ViewHistory> findByUserIdOrderByViewedAtDesc(Long userId, Pageable pageable);

    Optional<ViewHistory> findByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndPlaceId(Long userId, Long placeId);
}
