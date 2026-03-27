package kg.creativemap.api.service;

import kg.creativemap.api.dto.request.CreateEventRequest;
import kg.creativemap.api.dto.request.UpdateEventRequest;
import kg.creativemap.api.dto.response.EventResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.entity.*;
import kg.creativemap.api.exception.ResourceNotFoundException;
import kg.creativemap.api.mapper.EventMapper;
import kg.creativemap.api.repository.EventRepository;
import kg.creativemap.api.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final EventMapper eventMapper;
    private final AuditLogService auditLogService;

    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Transactional(readOnly = true)
    public PageResponse<EventResponse> getAllEvents(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage;
        if (search != null && !search.isBlank()) {
            eventPage = eventRepository.searchAll(search.trim(), pageable);
        } else {
            eventPage = eventRepository.findAllByOrderByStartDateDesc(pageable);
        }
        List<EventResponse> content = eventPage.getContent().stream()
                .map(eventMapper::toResponse)
                .toList();
        return PageResponse.of(eventPage, content);
    }

    @Transactional
    public EventResponse createEvent(User currentUser, CreateEventRequest request) {
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .free(request.getFree() != null ? request.getFree() : false)
                .category(parseCategory(request.getCategory()))
                .active(!"DRAFT".equalsIgnoreCase(request.getStatus()))
                .build();

        if (request.getPlaceId() != null) {
            Place place = placeRepository.findById(request.getPlaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Место не найдено: " + request.getPlaceId()));
            event.setPlace(place);
        }

        if (request.getStartDate() != null && !request.getStartDate().isBlank()) {
            event.setStartDate(LocalDateTime.parse(request.getStartDate(), DT_FORMAT));
        }
        if (request.getEndDate() != null && !request.getEndDate().isBlank()) {
            event.setEndDate(LocalDateTime.parse(request.getEndDate(), DT_FORMAT));
        }

        Event saved = eventRepository.save(event);
        auditLogService.log(currentUser, "CREATE_EVENT", "EVENT", saved.getId(),
                "Создано мероприятие «" + saved.getTitle() + "»");
        return eventMapper.toResponse(saved);
    }

    @Transactional
    public EventResponse updateEvent(User currentUser, Long id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Мероприятие не найдено: " + id));

        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getFree() != null) event.setFree(request.getFree());
        if (request.getCategory() != null) event.setCategory(parseCategory(request.getCategory()));
        if (request.getActive() != null) event.setActive(request.getActive());

        if (request.getPlaceId() != null) {
            Place place = placeRepository.findById(request.getPlaceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Место не найдено: " + request.getPlaceId()));
            event.setPlace(place);
        }

        if (request.getStartDate() != null && !request.getStartDate().isBlank()) {
            event.setStartDate(LocalDateTime.parse(request.getStartDate(), DT_FORMAT));
        }
        if (request.getEndDate() != null && !request.getEndDate().isBlank()) {
            event.setEndDate(LocalDateTime.parse(request.getEndDate(), DT_FORMAT));
        }

        Event saved = eventRepository.save(event);
        auditLogService.log(currentUser, "UPDATE_EVENT", "EVENT", saved.getId(),
                "Обновлено мероприятие «" + saved.getTitle() + "»");
        return eventMapper.toResponse(saved);
    }

    @Transactional
    public void deleteEvent(User currentUser, Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Мероприятие не найдено: " + id));
        eventRepository.delete(event);
        auditLogService.log(currentUser, "DELETE_EVENT", "EVENT", id,
                "Удалено мероприятие «" + event.getTitle() + "»");
    }

    @Transactional
    public EventResponse toggleStatus(User currentUser, Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Мероприятие не найдено: " + id));
        boolean newActive = !Boolean.TRUE.equals(event.getActive());
        event.setActive(newActive);
        Event saved = eventRepository.save(event);
        auditLogService.log(currentUser, "TOGGLE_EVENT", "EVENT", id,
                "Статус мероприятия «" + event.getTitle() + "» → " + (newActive ? "Активно" : "Черновик"));
        return eventMapper.toResponse(saved);
    }

    private EventCategory parseCategory(String category) {
        if (category == null || category.isBlank()) return EventCategory.MUSEUM;
        try {
            return EventCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EventCategory.MUSEUM;
        }
    }
}
