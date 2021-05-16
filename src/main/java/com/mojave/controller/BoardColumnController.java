package com.mojave.controller;

import com.mojave.payload.response.ApiResponse;
import com.mojave.service.BoardColumnService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BoardColumnController {

    BoardColumnService boardColumnService;

    @PostMapping("/{projectId}/board/{boardId}/column")
    public ResponseEntity<Long> createColumn(@PathVariable Long projectId,
                                             @PathVariable Long boardId,
                                             @NotBlank @RequestParam String columnName) {
        return ResponseEntity.ok(boardColumnService.createColumn(boardId, columnName));
    }

    @PutMapping("/{projectId}/board/column/{columnId}")
    public ResponseEntity<ApiResponse> updateColumnName(@PathVariable Long projectId,
                                                        @PathVariable Long columnId,
                                                        @NotBlank @RequestParam String newColumnName) {
        boardColumnService.updateColumnName(columnId, newColumnName);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully update column name"));
    }

    @DeleteMapping("/board/{boardId}/column/{columnId}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long boardId,
                                                  @PathVariable Long columnId) {
        boardColumnService.delete(boardId, columnId);
        return ResponseEntity.ok(new ApiResponse(true, "Section has been successfully deleted"));
    }
}
