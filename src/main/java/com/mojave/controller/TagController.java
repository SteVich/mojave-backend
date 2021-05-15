package com.mojave.controller;

import com.mojave.payload.response.TagResponse;
import com.mojave.service.TagService;
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
public class TagController {

    TagService tagService;

    @GetMapping("/{projectId}/tag")
    public ResponseEntity<List<TagResponse>> getAllTags(@PathVariable Long projectId) {
        return ResponseEntity.ok(tagService.getAllTagsForProject(projectId));
    }
}
