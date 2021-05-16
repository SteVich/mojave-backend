package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.model.Board;
import com.mojave.model.BoardColumn;
import com.mojave.repository.BoardColumnRepository;
import com.mojave.repository.BoardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BoardColumnService {

    BoardColumnRepository boardColumnRepository;
    BoardRepository boardRepository;

    @Transactional
    public Long createColumn(Long boardId, String columnName) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "boardId", boardId));

        BoardColumn column = new BoardColumn();
        column.setBoard(board);
        column.setName(columnName);

        Integer maxColumnPosition = boardColumnRepository.getMaxColumnPosition(boardId);
        column.setPositionInBoard(maxColumnPosition + 1);

        return boardColumnRepository.save(column).getId();
    }

    @Transactional
    public void updateColumnName(Long boardId, String newColumnName) {
        BoardColumn column = boardColumnRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("BoardColumn", "boardId", boardId));

        column.setName(newColumnName);
        boardColumnRepository.save(column);
    }

    @Transactional
    public void updateColumnsPositions(Long boardId, List<Long> idsToSetPositions) {
        List<BoardColumn> columns = boardColumnRepository.findAllByIdIn(idsToSetPositions);

        idsToSetPositions.forEach(id ->
                columns.stream()
                        .filter(column -> column.getId().equals(id))
                        .forEach(column -> column.setPositionInBoard(idsToSetPositions.indexOf(id)))
        );

        boardColumnRepository.saveAll(columns);
    }

    @Transactional
    public void delete(Long boardId, Long columnId) {
        boardColumnRepository.deleteById(columnId);
    }
}
