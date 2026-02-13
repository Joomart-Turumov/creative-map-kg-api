package kg.creativemap.api.service;

import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.dto.response.PlaceTypeCount;
import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.PlaceType;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.PlaceMapper;
import kg.creativemap.api.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public PageResponse<PlaceResponse> getPlaces(String type, String city, String region,
                                                  Boolean vrAvailable, String search,
                                                  int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        if (search != null && !search.isBlank()) {
            Page<Place> result = placeRepository.search(search.trim(), pageable);
            return PageResponse.of(result, result.getContent().stream().map(placeMapper::toResponse).toList());
        }

        PlaceType placeType = null;
        if (type != null && !type.isBlank()) {
            try {
                placeType = PlaceType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неизвестный тип локации: " + type);
            }
        }

        Page<Place> result = placeRepository.findWithFilters(
                placeType,
                city != null && !city.isBlank() ? city : null,
                region != null && !region.isBlank() ? region : null,
                vrAvailable,
                pageable);

        return PageResponse.of(result, result.getContent().stream().map(placeMapper::toResponse).toList());
    }

    public PlaceResponse getPlace(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Локация не найдена: " + id));
        return placeMapper.toResponse(place);
    }

    public PageResponse<PlaceResponse> searchPlaces(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Place> result = placeRepository.search(query, pageable);
        return PageResponse.of(result, result.getContent().stream().map(placeMapper::toResponse).toList());
    }

    public List<PlaceTypeCount> getTypeCounts() {
        List<Object[]> raw = placeRepository.countByType();
        List<PlaceTypeCount> counts = new ArrayList<>();
        for (Object[] row : raw) {
            counts.add(PlaceTypeCount.builder()
                    .type(((PlaceType) row[0]).name().toLowerCase())
                    .count((Long) row[1])
                    .build());
        }
        return counts;
    }
}
