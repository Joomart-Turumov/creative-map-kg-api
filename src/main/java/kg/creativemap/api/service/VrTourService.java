package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.CreateVrTourRequest;
import kg.creativemap.api.dto.request.UpdateVrTourRequest;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.VrTourResponse;
import kg.creativemap.api.entity.*;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.VrTourMapper;
import kg.creativemap.api.repository.UserRepository;
import kg.creativemap.api.repository.VrTourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VrTourService {

    private final VrTourRepository vrTourRepository;
    private final UserRepository userRepository;
    private final VrTourMapper vrTourMapper;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public PageResponse<VrTourResponse> getPublishedTours(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VrTour> tourPage = vrTourRepository.findByStatusOrderByCreatedAtDesc(VrTourStatus.PUBLISHED, pageable);
        List<VrTourResponse> content = tourPage.getContent().stream()
                .map(vrTourMapper::toResponse)
                .toList();
        return PageResponse.of(tourPage, content);
    }

    @Transactional(readOnly = true)
    public List<VrTourResponse> getAllPublishedTours() {
        Pageable pageable = PageRequest.of(0, 1000);
        Page<VrTour> tourPage = vrTourRepository.findByStatusOrderByCreatedAtDesc(VrTourStatus.PUBLISHED, pageable);
        return tourPage.getContent().stream()
                .map(vrTourMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VrTourResponse getTour(Long id) {
        VrTour tour = vrTourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VR-тур не найден: " + id));
        return vrTourMapper.toResponse(tour);
    }

    @Transactional(readOnly = true)
    public PageResponse<VrTourResponse> getAllTours(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VrTour> tourPage;
        if (search != null && !search.isBlank()) {
            tourPage = vrTourRepository.search(search.trim(), pageable);
        } else {
            tourPage = vrTourRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        List<VrTourResponse> content = tourPage.getContent().stream()
                .map(vrTourMapper::toResponse)
                .toList();
        return PageResponse.of(tourPage, content);
    }

    @Transactional
    public VrTourResponse createTour(User currentUser, CreateVrTourRequest request) {
        // getReferenceById создаёт managed-прокси в текущем EntityManager,
        // избегая PersistentObjectException при persist() с detached User из JwtAuthFilter
        User managedUser = userRepository.getReferenceById(currentUser.getId());

        VrTour tour = VrTour.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .iframeUrl(request.getIframeUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .address(request.getAddress())
                .category(parseCategory(request.getCategory()))
                .region(request.getRegion())
                .status(parseStatus(request.getStatus()))
                .createdBy(managedUser)
                .build();

        VrTour saved = vrTourRepository.save(tour);
        auditLogService.log(currentUser, "CREATE_VR_TOUR", "VR_TOUR", saved.getId(),
                "Добавлен VR-тур «" + saved.getTitle() + "»");
        return vrTourMapper.toResponse(saved);
    }

    @Transactional
    public VrTourResponse updateTour(User currentUser, Long id, UpdateVrTourRequest request) {
        VrTour tour = vrTourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VR-тур не найден: " + id));

        if (request.getTitle() != null) tour.setTitle(request.getTitle());
        if (request.getDescription() != null) tour.setDescription(request.getDescription());
        if (request.getIframeUrl() != null) tour.setIframeUrl(request.getIframeUrl());
        if (request.getThumbnailUrl() != null) tour.setThumbnailUrl(request.getThumbnailUrl());
        if (request.getAddress() != null) tour.setAddress(request.getAddress());
        if (request.getCategory() != null) tour.setCategory(parseCategory(request.getCategory()));
        if (request.getRegion() != null) tour.setRegion(request.getRegion());
        if (request.getStatus() != null) tour.setStatus(parseStatus(request.getStatus()));

        VrTour saved = vrTourRepository.save(tour);
        auditLogService.log(currentUser, "UPDATE_VR_TOUR", "VR_TOUR", saved.getId(),
                "Обновлён VR-тур «" + saved.getTitle() + "»");
        return vrTourMapper.toResponse(saved);
    }

    @Transactional
    public void deleteTour(User currentUser, Long id) {
        VrTour tour = vrTourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VR-тур не найден: " + id));
        vrTourRepository.delete(tour);
        auditLogService.log(currentUser, "DELETE_VR_TOUR", "VR_TOUR", id,
                "Удалён VR-тур «" + tour.getTitle() + "»");
    }

    @Transactional
    public VrTourResponse toggleStatus(User currentUser, Long id) {
        VrTour tour = vrTourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VR-тур не найден: " + id));
        VrTourStatus newStatus = tour.getStatus() == VrTourStatus.PUBLISHED
                ? VrTourStatus.DRAFT : VrTourStatus.PUBLISHED;
        tour.setStatus(newStatus);
        VrTour saved = vrTourRepository.save(tour);
        auditLogService.log(currentUser, "TOGGLE_VR_TOUR", "VR_TOUR", id,
                "Статус VR-тура «" + tour.getTitle() + "» → " + newStatus.name());
        return vrTourMapper.toResponse(saved);
    }

    private VrTourCategory parseCategory(String category) {
        if (category == null || category.isBlank()) return VrTourCategory.OTHER;
        try {
            return VrTourCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return VrTourCategory.OTHER;
        }
    }

    private VrTourStatus parseStatus(String status) {
        if (status == null || status.isBlank()) return VrTourStatus.DRAFT;
        try {
            return VrTourStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return VrTourStatus.DRAFT;
        }
    }
}
