package com.balugaq.configurator.data.writer;

import com.balugaq.configurator.data.relation.Node;
import java.io.File;

public interface IWriter {
    void write(File file, Node node);
}