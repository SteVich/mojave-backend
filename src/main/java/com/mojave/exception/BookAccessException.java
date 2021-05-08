package com.mojave.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.NOT_FOUND)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAccessException extends RuntimeException {

   String title;

    public BookAccessException(String title) {
        super(String.format("Book %s already borrowed!", title));
        this.title = title;
    }

}
