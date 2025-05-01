package com.balugaq.configurator.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CustomId {
    private final UUID uuid;
}
