package com.mojave.controller;

import com.mojave.payload.response.ApiResponse;
import com.mojave.payload.response.ProjectResponse;
import com.mojave.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectController {

    ProjectService projectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getResponseById(projectId));
    }

    @PutMapping("/{projectId}/name")
    public ResponseEntity<ApiResponse> updateName(@PathVariable Long projectId, @RequestParam String name) {
        projectService.updateName(projectId, name);
        return ResponseEntity.ok(new ApiResponse(true, "Project name was successfully updated"));
    }

    @PutMapping("/{projectId}/description")
    public ResponseEntity<ApiResponse> updateDescription(@PathVariable Long projectId, @RequestParam String description) {
        projectService.updateDescription(projectId, description);
        return ResponseEntity.ok(new ApiResponse(true, "Project description was successfully updated"));
    }

    @PutMapping("/{projectId}/imageUrl")
    public ResponseEntity<ApiResponse> updateImageUrl(@PathVariable Long projectId, @RequestParam String imageUrl) {
        projectService.updateImageUrl(projectId, imageUrl);
        return ResponseEntity.ok(new ApiResponse(true, "Project image url was successfully updated"));
    }
}
