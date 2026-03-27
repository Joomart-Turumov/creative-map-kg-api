package kg.creativemap.api.mapper;

import kg.creativemap.api.dto.response.VrTourResponse;
import kg.creativemap.api.entity.VrTour;
import org.springframework.stereotype.Component;

@Component
public class VrTourMapper {

    public VrTourResponse toResponse(VrTour tour) {
        return VrTourResponse.builder()
                .id(tour.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .iframeUrl(tour.getIframeUrl())
                .thumbnailUrl(tour.getThumbnailUrl())
                .address(tour.getAddress())
                .category(tour.getCategory() != null ? tour.getCategory().name() : "OTHER")
                .region(tour.getRegion())
                .status(tour.getStatus() != null ? tour.getStatus().name() : "DRAFT")
                .createdByName(tour.getCreatedBy() != null ? tour.getCreatedBy().getFullName() : null)
                .createdAt(tour.getCreatedAt())
                .updatedAt(tour.getUpdatedAt())
                .build();
    }
}
