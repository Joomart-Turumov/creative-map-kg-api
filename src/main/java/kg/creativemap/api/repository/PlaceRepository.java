package kg.creativemap.api.repository;

import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.PlaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Page<Place> findByActiveTrue(Pageable pageable);

    Page<Place> findByTypeAndActiveTrue(PlaceType type, Pageable pageable);

    Page<Place> findByCityIgnoreCaseAndActiveTrue(String city, Pageable pageable);

    Page<Place> findByRegionIgnoreCaseAndActiveTrue(String region, Pageable pageable);

    Page<Place> findByVrAvailableAndActiveTrue(Boolean vrAvailable, Pageable pageable);

    @Query("SELECT p FROM Place p WHERE p.active = true AND " +
           "(LOWER(p.nameKy) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.nameRu) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.nameEn) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.city) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(p.address) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Place> search(@Param("q") String query, Pageable pageable);

    @Query("SELECT p FROM Place p WHERE p.active = true " +
           "AND (:type IS NULL OR p.type = :type) " +
           "AND (:city IS NULL OR LOWER(p.city) = LOWER(:city)) " +
           "AND (:region IS NULL OR LOWER(p.region) = LOWER(:region)) " +
           "AND (:vrAvailable IS NULL OR p.vrAvailable = :vrAvailable)")
    Page<Place> findWithFilters(
            @Param("type") PlaceType type,
            @Param("city") String city,
            @Param("region") String region,
            @Param("vrAvailable") Boolean vrAvailable,
            Pageable pageable);

    @Query("SELECT p.type, COUNT(p) FROM Place p WHERE p.active = true GROUP BY p.type ORDER BY COUNT(p) DESC")
    List<Object[]> countByType();

    long countByActiveTrue();
}
