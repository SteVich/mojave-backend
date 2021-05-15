package com.mojave.mapper;

import com.mojave.model.Milestone;
import com.mojave.payload.response.MilestoneResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MilestoneMapper {

    List<MilestoneResponse> toResponseList(List<Milestone> milestones);
}
