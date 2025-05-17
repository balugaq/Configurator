package com.balugaq.connector.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CustomId {
    private final UUID uuid;
}
