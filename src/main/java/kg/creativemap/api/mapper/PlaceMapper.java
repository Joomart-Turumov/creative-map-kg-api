package kg.creativemap.api.mapper;

import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.entity.Place;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    public PlaceResponse toResponse(Place place) {
        return PlaceResponse.builder()
                .id(place.getId())
                .nameKy(place.getNameKy())
                .nameRu(place.getNameRu())
                .nameEn(place.getNameEn())
                .type(place.getType().name().toLowerCase())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .city(place.getCity())
                .region(place.getRegion())
                .rating(place.getRating())
                .vrAvailable(place.getVrAvailable())
                .icon(place.getIcon())
                .address(place.getAddress())
                .descriptionKy(place.getDescriptionKy())
                .descriptionRu(place.getDescriptionRu())
                .descriptionEn(place.getDescriptionEn())
                .photoUrl(place.getPhotoUrl())
                .vrUrl(place.getVrUrl())
                .build();
    }
}
