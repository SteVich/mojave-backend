package com.mojave.payload.request;

import com.mojave.dictionary.Priority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskUpdateRequest {

    @NotNull
    Integer number;

    @NotNull
    String title;

    @NotNull
    LocalDateTime dueDate;

    @NotNull
    Priority priority;

    @NotNull
    Long assigneeId;

    @NotNull
    Long milestoneId;

    @NotNull
    Long tagId;

    Double estimate;

    String description;
}
