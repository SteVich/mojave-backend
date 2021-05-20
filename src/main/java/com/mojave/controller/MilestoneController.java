package com.mojave.controller;

import com.mojave.payload.response.ApiResponse;
import com.mojave.payload.response.MilestoneResponse;
import com.mojave.service.MilestoneService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MilestoneController {

    MilestoneService milestoneService;

    @GetMapping("/{projectId}/milestone")
    public ResponseEntity<List<MilestoneResponse>> getAllMilestones(@PathVariable Long projectId) {
        return ResponseEntity.ok(milestoneService.getAllMilestonesForProject(projectId));
    }

    @PostMapping("/{projectId}/milestone")
    public ResponseEntity<Long> createMilestone(@PathVariable Long projectId, @RequestParam String name) {
        return ResponseEntity.ok(milestoneService.createMilestone(projectId, name));
    }

    @DeleteMapping("/{projectId}/milestone/{milestoneId}")
    public ResponseEntity<ApiResponse> deleteMilestone(@PathVariable Long projectId, @PathVariable Long milestoneId) {
        ApiResponse apiResponse;
        boolean result = milestoneService.deleteMilestone(projectId, milestoneId);
        if (result) {
            apiResponse = new ApiResponse(true, "Milestone was deleted successfully");
        } else {
            apiResponse = new ApiResponse(false, "Error! This milestone is used by tasks");
        }
        return ResponseEntity.ok(apiResponse);
    }

}
