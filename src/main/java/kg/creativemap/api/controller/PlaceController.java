package kg.creativemap.api.controller;

import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.PlaceResponse;
import kg.creativemap.api.dto.response.PlaceTypeCount;
import kg.creativemap.api.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PlaceResponse>>> getPlaces(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean vrAvailable,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<PlaceResponse> result = placeService.getPlaces(
                type, city, region, vrAvailable, search, page, size);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaceResponse>> getPlace(@PathVariable Long id) {
        PlaceResponse place = placeService.getPlace(id);
        return ResponseEntity.ok(ApiResponse.ok(place));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PlaceResponse>>> searchPlaces(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<PlaceResponse> result = placeService.searchPlaces(q, page, size);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<PlaceTypeCount>>> getTypes() {
        List<PlaceTypeCount> types = placeService.getTypeCounts();
        return ResponseEntity.ok(ApiResponse.ok(types));
    }
}
