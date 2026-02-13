package kg.creativemap.api.service;

import kg.creativemap.api.dto.response.FavoriteResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.entity.Favorite;
import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.exception.ConflictException;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.PlaceMapper;
import kg.creativemap.api.repository.FavoriteRepository;
import kg.creativemap.api.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(User user) {
        return favoriteRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FavoriteResponse addFavorite(User user, Long placeId) {
        if (favoriteRepository.existsByUserIdAndPlaceId(user.getId(), placeId)) {
            throw new ConflictException("Локация уже в избранном");
        }

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Локация не найдена: " + placeId));

        Favorite favorite = Favorite.builder()
                .user(user)
                .place(place)
                .build();
        favoriteRepository.save(favorite);

        return toResponse(favorite);
    }

    @Transactional
    public void removeFavorite(User user, Long placeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndPlaceId(user.getId(), placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Локация не найдена в избранном"));
        favoriteRepository.delete(favorite);
    }

    private FavoriteResponse toResponse(Favorite favorite) {
        PlaceResponse placeResponse = placeMapper.toResponse(favorite.getPlace());
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .place(placeResponse)
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
