package com.balugaq.connector;

import org.bukkit.NamespacedKey;

public class Keys {
    public static final NamespacedKey source = new NamespacedKey(Connector.getInstance(), "source");
    public static final NamespacedKey destination = new NamespacedKey(Connector.getInstance(), "destination");
    public static final NamespacedKey c_link_uuid = new NamespacedKey(Connector.getInstance(), "c_link_uuid");
    public static final NamespacedKey c_link_source = new NamespacedKey(Connector.getInstance(), "c_link_source");
    public static final NamespacedKey c_link_destination = new NamespacedKey(Connector.getInstance(), "c_link_destination");
    public static final NamespacedKey c_link_interaction = new NamespacedKey(Connector.getInstance(), "c_link_interaction");
    public static final NamespacedKey c_node_uuid = new NamespacedKey(Connector.getInstance(), "c_node_uuid");
    public static final NamespacedKey c_node_child = new NamespacedKey(Connector.getInstance(), "c_node_child");
    public static final NamespacedKey c_node_interaction = new NamespacedKey(Connector.getInstance(), "c_node_interaction");
    public static final NamespacedKey c_interact_handler = new NamespacedKey(Connector.getInstance(), "c_interact_handler");
    public static final NamespacedKey c_interaction_belongs_to = new NamespacedKey(Connector.getInstance(), "c_interaction_belongs_to");
}
