package kg.creativemap.api.repository;

import kg.creativemap.api.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStartDateAfterOrderByStartDateAsc(LocalDateTime date);

    List<Event> findByActiveTrueAndStartDateAfterOrderByStartDateAsc(LocalDateTime date);

    Page<Event> findAllByOrderByStartDateDesc(Pageable pageable);

    Page<Event> findByActiveTrueOrderByStartDateDesc(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY e.startDate DESC")
    Page<Event> searchAll(@Param("search") String search, Pageable pageable);
}
