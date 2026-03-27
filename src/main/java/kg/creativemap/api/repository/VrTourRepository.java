package kg.creativemap.api.repository;

import kg.creativemap.api.entity.VrTour;
import kg.creativemap.api.entity.VrTourStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VrTourRepository extends JpaRepository<VrTour, Long> {

    Page<VrTour> findByStatusOrderByCreatedAtDesc(VrTourStatus status, Pageable pageable);

    Page<VrTour> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT v FROM VrTour v WHERE v.status = :status AND (LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.address) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY v.createdAt DESC")
    Page<VrTour> searchByStatus(@Param("search") String search, @Param("status") VrTourStatus status, Pageable pageable);

    @Query("SELECT v FROM VrTour v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(v.address) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY v.createdAt DESC")
    Page<VrTour> search(@Param("search") String search, Pageable pageable);
}
