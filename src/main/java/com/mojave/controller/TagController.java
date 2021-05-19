package com.mojave.controller;

import com.mojave.payload.response.ApiResponse;
import com.mojave.payload.response.TagResponse;
import com.mojave.service.TagService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TagController {

    TagService tagService;

    @GetMapping("/{projectId}/tag")
    public ResponseEntity<List<TagResponse>> getAllTags(@PathVariable Long projectId) {
        return ResponseEntity.ok(tagService.getAllTagsForProject(projectId));
    }

    @PostMapping("/{projectId}/tag")
    public ResponseEntity<Long> createTag(@PathVariable Long projectId,
                                          @RequestParam String name,
                                          @RequestParam String color) {
        return ResponseEntity.ok(tagService.createTag(projectId, name, color));
    }

    @PutMapping("/tag/{tagId}")
    public ResponseEntity<ApiResponse> updateColor(@PathVariable Long tagId,
                                                   @RequestParam String newColor) {
        tagService.updateColor(tagId, newColor);
        return ResponseEntity.ok(new ApiResponse(true, "Tag color was update successfully"));
    }

    @DeleteMapping("/{projectId}/tag/{tagId}")
    public ResponseEntity<ApiResponse> deleteMilestone(@PathVariable Long projectId, @PathVariable Long tagId) {
        ApiResponse apiResponse;

        boolean result = tagService.deleteTag(projectId, tagId);
        if (result) {
            apiResponse = new ApiResponse(true, "Tag was deleted successfully");
        } else {
            apiResponse = new ApiResponse(false, "Error! This tag is used by tasks");
        }
        return ResponseEntity.ok(apiResponse);
    }
}
