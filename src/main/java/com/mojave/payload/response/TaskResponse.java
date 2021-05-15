package com.mojave.payload.response;

import com.mojave.dictionary.Priority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {

    Long id;
    Integer number;
    String title;
    LocalDateTime dueDate;
    Double estimate;
    String description;
    UserResponse assignee;
    Priority priority;
    MilestoneResponse milestone;
    TagResponse tag;
    List<HistoryResponse> histories;
}
