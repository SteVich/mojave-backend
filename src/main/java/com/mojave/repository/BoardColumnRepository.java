package com.mojave.repository;

import com.mojave.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

}
