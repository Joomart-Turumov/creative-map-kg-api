package kg.creativemap.api.mapper;

import kg.creativemap.api.dto.response.EventResponse;
import kg.creativemap.api.entity.Event;
import kg.creativemap.api.entity.Place;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {
        Place place = event.getPlace();

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .placeId(place != null ? place.getId() : null)
                .placeName(place != null ? place.getNameRu() : null)
                .placeIcon(place != null ? place.getIcon() : null)
                .placeCity(place != null ? place.getCity() : null)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .free(event.getFree() != null && event.getFree())
                .category(event.getCategory() != null ? event.getCategory().name().toLowerCase() : "museum")
                .build();
    }
}
