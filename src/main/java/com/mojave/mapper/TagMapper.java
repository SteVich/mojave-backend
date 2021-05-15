package com.mojave.mapper;

import com.mojave.model.Tag;
import com.mojave.payload.response.TagResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    List<TagResponse> toResponseList(List<Tag> tags);
}
