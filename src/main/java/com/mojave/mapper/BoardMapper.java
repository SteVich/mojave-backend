package com.mojave.mapper;

import com.mojave.model.Board;
import com.mojave.payload.response.BoardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardResponse mapToResponse(Board board);
}
