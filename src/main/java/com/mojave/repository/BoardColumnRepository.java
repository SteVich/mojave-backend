package com.mojave.repository;

import com.mojave.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> findAllByIdIn(List<Long> idsToSetPositions);

    @Query("select max(bc.positionInBoard) from BoardColumn bc where bc.board.id = :boardId")
    Integer getMaxColumnPosition(@Param("boardId") Long boardId);
}
