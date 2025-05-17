package com.balugaq.connector;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pair<A, B> {
    public final A first;
    public final B second;
}
