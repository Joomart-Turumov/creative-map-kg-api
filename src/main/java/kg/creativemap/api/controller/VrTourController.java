package kg.creativemap.api.controller;

import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.VrTourResponse;
import kg.creativemap.api.service.VrTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vr-tours")
@RequiredArgsConstructor
public class VrTourController {

    private final VrTourService vrTourService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VrTourResponse>>> getAllPublished() {
        List<VrTourResponse> tours = vrTourService.getAllPublishedTours();
        return ResponseEntity.ok(ApiResponse.ok(tours));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<VrTourResponse>>> getPublishedPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<VrTourResponse> result = vrTourService.getPublishedTours(page, size);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VrTourResponse>> getTour(@PathVariable Long id) {
        VrTourResponse tour = vrTourService.getTour(id);
        return ResponseEntity.ok(ApiResponse.ok(tour));
    }
}
