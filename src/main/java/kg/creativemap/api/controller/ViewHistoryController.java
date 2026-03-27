package kg.creativemap.api.controller;

import kg.creativemap.api.dto.response.ApiResponse;
import kg.creativemap.api.dto.response.PageResponse;
import kg.creativemap.api.dto.response.ViewHistoryResponse;
import kg.creativemap.api.entity.User;
import kg.creativemap.api.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class ViewHistoryController {

    private final ViewHistoryService viewHistoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ViewHistoryResponse>>> getHistory(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<ViewHistoryResponse> history = viewHistoryService.getHistory(user, page, size);
        return ResponseEntity.ok(ApiResponse.ok(history));
    }

    @PostMapping("/{placeId}")
    public ResponseEntity<ApiResponse<ViewHistoryResponse>> addView(
            @AuthenticationPrincipal User user,
            @PathVariable Long placeId) {
        ViewHistoryResponse response = viewHistoryService.addOrUpdate(user, placeId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Просмотр записан"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearHistory(
            @AuthenticationPrincipal User user) {
        viewHistoryService.clearHistory(user);
        return ResponseEntity.ok(ApiResponse.ok(null, "История очищена"));
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<ApiResponse<Void>> removeEntry(
            @AuthenticationPrincipal User user,
            @PathVariable Long placeId) {
        viewHistoryService.removeEntry(user, placeId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Запись удалена"));
    }
}
