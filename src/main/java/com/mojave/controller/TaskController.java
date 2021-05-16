package com.mojave.controller;

import com.mojave.payload.request.TaskUpdateRequest;
import com.mojave.payload.response.ApiResponse;
import com.mojave.payload.response.TaskResponse;
import com.mojave.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskController {

    TaskService taskService;

    @PostMapping("/{projectId}/board/column/{columnId}/task")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId,
                                                   @PathVariable Long columnId,
                                                   @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.create(projectId, columnId, request));
    }

    @PostMapping("/{projectId}/board/column/{columnId}/task/{taskId}/duplicate")
    public ResponseEntity<TaskResponse> duplicateTask(@PathVariable Long projectId,
                                                      @PathVariable Long columnId,
                                                      @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.duplicate(projectId, columnId, taskId));
    }

    @PutMapping("/{projectId}/board/column/{columnId}/task/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long projectId,
                                                   @PathVariable Long columnId,
                                                   @PathVariable Long taskId,
                                                   @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.update(projectId, columnId, taskId, request));
    }

    @PutMapping("/{projectId}/board/column/{columnId}/task/{taskId}/change-column")
    public ResponseEntity<ApiResponse> changeColumn(@PathVariable Long projectId,
                                                    @PathVariable Long columnId,
                                                    @PathVariable Long taskId,
                                                    @RequestParam Integer taskPosition) {
        taskService.changeColumn(projectId, columnId, taskId, taskPosition);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully changed the column for the task"));
    }

    @PutMapping("/{projectId}/board/column/{columnId}/task/change-tasks-positions")
    public ResponseEntity<ApiResponse> changeTasksPositionsInColumn(@PathVariable Long projectId,
                                                                    @PathVariable Long columnId,
                                                                    @RequestBody List<Long> taskIdsToSetPositions) {
        taskService.changeTasksPositionsInColumns(projectId, columnId, taskIdsToSetPositions);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully changed tasks positions in the column"));
    }

    @DeleteMapping("/board/column/{columnId}/task/{taskId}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long columnId,
                                                  @PathVariable Long taskId) {
        taskService.delete(columnId, taskId);
        return ResponseEntity.ok(new ApiResponse(true, "Task has been successfully deleted"));
    }
}
