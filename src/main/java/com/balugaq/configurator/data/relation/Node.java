package com.balugaq.configurator.data.relation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Node {
    private String key;
    private Object value;
    private List<Node> children;
}
