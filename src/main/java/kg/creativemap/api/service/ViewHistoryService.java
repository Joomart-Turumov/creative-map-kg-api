package kg.creativemap.api.service;

import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.dto.response.ViewHistoryResponse;
import kg.creativemap.api.entity.Place;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.entity.ViewHistory;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.PlaceMapper;
import kg.creativemap.api.repository.PlaceRepository;
import kg.creativemap.api.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public PageResponse<ViewHistoryResponse> getHistory(User user, int page, int size) {
        Page<ViewHistory> historyPage = viewHistoryRepository
                .findByUserIdWithPlace(user.getId(), PageRequest.of(page, size));

        return PageResponse.<ViewHistoryResponse>builder()
                .content(historyPage.getContent().stream().map(this::toResponse).toList())
                .page(historyPage.getNumber())
                .size(historyPage.getSize())
                .totalElements(historyPage.getTotalElements())
                .totalPages(historyPage.getTotalPages())
                .hasNext(historyPage.hasNext())
                .hasPrevious(historyPage.hasPrevious())
                .build();
    }

    @Transactional
    public ViewHistoryResponse addOrUpdate(User user, Long placeId) {
        Optional<ViewHistory> existing = viewHistoryRepository
                .findByUserIdAndPlaceId(user.getId(), placeId);

        if (existing.isPresent()) {
            ViewHistory vh = existing.get();
            vh.setViewCount(vh.getViewCount() + 1);
            vh.setViewedAt(LocalDateTime.now());
            viewHistoryRepository.save(vh);
            return toResponse(vh);
        }

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Локация не найдена: " + placeId));

        ViewHistory vh = ViewHistory.builder()
                .user(user)
                .place(place)
                .viewCount(1)
                .build();
        viewHistoryRepository.save(vh);
        return toResponse(vh);
    }

    @Transactional
    public void clearHistory(User user) {
        viewHistoryRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public void removeEntry(User user, Long placeId) {
        viewHistoryRepository.deleteByUserIdAndPlaceId(user.getId(), placeId);
    }

    private ViewHistoryResponse toResponse(ViewHistory vh) {
        PlaceResponse placeResponse = placeMapper.toResponse(vh.getPlace());
        return ViewHistoryResponse.builder()
                .id(vh.getId())
                .place(placeResponse)
                .viewCount(vh.getViewCount())
                .viewedAt(vh.getViewedAt())
                .build();
    }
}
