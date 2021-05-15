package com.mojave.controller;

import com.mojave.payload.request.TaskUpdateRequest;
import com.mojave.payload.response.ApiResponse;
import com.mojave.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project/board/column")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskController {

    TaskService taskService;

    @PostMapping("/{columnId}/task")
    public ResponseEntity<ApiResponse> createTask(@PathVariable Long columnId,
                                                  @RequestBody TaskUpdateRequest request) {
        taskService.create(columnId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Task has been created successfully"));
    }
}
