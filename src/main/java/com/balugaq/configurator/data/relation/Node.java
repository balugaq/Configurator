package com.balugaq.configurator.data.relation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Node {
    private String key = null;
    private Object value = null;
    private List<Node> children = new ArrayList<>();
}
