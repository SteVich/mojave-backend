package com.mojave.controller;

import com.mojave.payload.response.MilestoneResponse;
import com.mojave.service.MilestoneService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
