package com.mojave.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardResponse {

    Long id;
    String name;

    @JsonProperty("columns")
    List<BoardColumnResponse> boardColumns = new ArrayList<>();
}
