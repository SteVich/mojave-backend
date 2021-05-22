package com.mojave.dictionary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Role {

    ROLE_PROJECT_OWNER("Project Owner"),
    ROLE_PROJECT_MANAGER("PM"),
    ROLE_DEVELOPER("DEV"),
    ROLE_QA("QA");

    String value;

    static final Map<String, Role> BY_NAME = new HashMap<>();

    static {
        for (Role element : values()) {
            BY_NAME.put(element.value, element);
        }
    }

    public static Role valueOfName(String value) {
        return BY_NAME.get(value);
    }
}
