package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.BoardMapper;
import com.mojave.model.Board;
import com.mojave.payload.response.BoardResponse;
import com.mojave.repository.BoardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BoardService {

    BoardRepository boardRepository;
    BoardMapper boardMapper;

    @Transactional(readOnly = true)
    public BoardResponse getBoardResponseForProject(Long projectId) {
        Board board = boardRepository.findBoardByProjectId(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "project id", projectId));
        return boardMapper.mapToResponse(board);
    }

}
