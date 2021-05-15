package com.mojave.payload.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryResponse {

    Long id;
    LocalDateTime date;
    String description;
    UserResponse user;
}
