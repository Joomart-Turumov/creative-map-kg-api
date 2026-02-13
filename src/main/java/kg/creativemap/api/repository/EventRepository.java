package kg.creativemap.api.repository;

import kg.creativemap.api.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStartDateAfterOrderByStartDateAsc(LocalDateTime date);

    Page<Event> findAllByOrderByStartDateDesc(Pageable pageable);
}
