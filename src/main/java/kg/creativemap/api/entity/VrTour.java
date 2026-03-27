package kg.creativemap.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vr_tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VrTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "iframe_url", nullable = false)
    private String iframeUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private String address;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VrTourCategory category = VrTourCategory.OTHER;

    private String region;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VrTourStatus status = VrTourStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
