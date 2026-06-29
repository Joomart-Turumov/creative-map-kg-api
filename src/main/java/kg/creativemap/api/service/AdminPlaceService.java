package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.CreatePlaceRequest;
import kg.creativemap.api.dto.request.UpdatePlaceRequest;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.PlaceType;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.PlaceMapper;
import kg.creativemap.api.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final AuditLogService auditLogService;

    @Transactional
    public PlaceResponse createPlace(User currentUser, CreatePlaceRequest request) {
        Place place = Place.builder()
                .nameKy(request.getNameKy())
                .nameRu(request.getNameRu())
                .nameEn(request.getNameEn())
                .type(parseType(request.getType()))
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .city(request.getCity())
                .region(request.getRegion())
                .address(request.getAddress())
                .descriptionKy(request.getDescriptionKy())
                .descriptionRu(request.getDescriptionRu())
                .descriptionEn(request.getDescriptionEn())
                .photoUrl(request.getPhotoUrl())
                .vrUrl(request.getVrUrl())
                .vrAvailable(request.getVrAvailable() != null ? request.getVrAvailable() : false)
                .icon(request.getIcon())
                .rating(0.0)
                .active(true)
                .build();

        Place saved = placeRepository.save(place);
        auditLogService.log(currentUser, "CREATE_LOCATION", "PLACE", saved.getId(),
                "Создано место «" + displayName(saved) + "»");
        return placeMapper.toResponse(saved);
    }

    @Transactional
    public PlaceResponse updatePlace(User currentUser, Long id, UpdatePlaceRequest request) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место не найдено: " + id));

        if (request.getNameKy() != null) place.setNameKy(request.getNameKy());
        if (request.getNameRu() != null) place.setNameRu(request.getNameRu());
        if (request.getNameEn() != null) place.setNameEn(request.getNameEn());
        if (request.getType() != null) place.setType(parseType(request.getType()));
        if (request.getLatitude() != null) place.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) place.setLongitude(request.getLongitude());
        if (request.getCity() != null) place.setCity(request.getCity());
        if (request.getRegion() != null) place.setRegion(request.getRegion());
        if (request.getAddress() != null) place.setAddress(request.getAddress());
        if (request.getDescriptionKy() != null) place.setDescriptionKy(request.getDescriptionKy());
        if (request.getDescriptionRu() != null) place.setDescriptionRu(request.getDescriptionRu());
        if (request.getDescriptionEn() != null) place.setDescriptionEn(request.getDescriptionEn());
        if (request.getPhotoUrl() != null) place.setPhotoUrl(request.getPhotoUrl());
        if (request.getVrUrl() != null) place.setVrUrl(request.getVrUrl());
        if (request.getVrAvailable() != null) place.setVrAvailable(request.getVrAvailable());
        if (request.getIcon() != null) place.setIcon(request.getIcon());

        Place saved = placeRepository.save(place);
        auditLogService.log(currentUser, "UPDATE_LOCATION", "PLACE", saved.getId(),
                "Обновлено место «" + displayName(saved) + "»");
        return placeMapper.toResponse(saved);
    }

    @Transactional
    public void deletePlace(User currentUser, Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место не найдено: " + id));
        placeRepository.delete(place);
        auditLogService.log(currentUser, "DELETE_LOCATION", "PLACE", id,
                "Удалено место «" + displayName(place) + "»");
    }

    private String displayName(Place p) {
        if (p.getNameRu() != null && !p.getNameRu().isBlank()) return p.getNameRu();
        return p.getNameKy();
    }

    private PlaceType parseType(String type) {
        if (type == null || type.isBlank()) return PlaceType.MUSEUM;
        try {
            return PlaceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PlaceType.MUSEUM;
        }
    }
}
