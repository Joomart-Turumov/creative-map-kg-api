package kg.creativemap.api.service;

import kg.creativemap.api.dto.response.EventResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.entity.Event;
import kg.creativemap.api.mapper.EventMapper;
import kg.creativemap.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kg.creativemap.api.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
        return eventMapper.toResponse(event);
    }

    public List<EventResponse> getUpcomingEvents() {
        List<Event> events = eventRepository.findByActiveTrueAndStartDateAfterOrderByStartDateAsc(LocalDateTime.now());
        return events.stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public PageResponse<EventResponse> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findByActiveTrueOrderByStartDateDesc(pageable);
        List<EventResponse> content = eventPage.getContent().stream()
                .map(eventMapper::toResponse)
                .toList();
        return PageResponse.of(eventPage, content);
    }
}
