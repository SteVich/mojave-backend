package com.mojave.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardColumnResponse {

    Long id;
    String name;
    List<TaskResponse> tasks = new ArrayList<>();
}
