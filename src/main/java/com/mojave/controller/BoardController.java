package com.mojave.controller;

import com.mojave.payload.response.BoardResponse;
import com.mojave.service.BoardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BoardController {

    BoardService boardService;

    @GetMapping("/{projectId}/board")
    public ResponseEntity<BoardResponse> getBoardForProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(boardService.getBoardResponseForProject(projectId));
    }
}
