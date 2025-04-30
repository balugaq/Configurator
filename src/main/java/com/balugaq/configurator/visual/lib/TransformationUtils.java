package com.balugaq.configurator.visual.lib;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

/**
 * @author metamechanists
 * @author Idra
 */
public final class TransformationUtils {
    private static final List<BlockFace> AXIS;

    public static double yawToCardinalDirection(float yaw) {
        return (double)(-Math.round(yaw / 90.0F)) * (Math.PI / 2D);
    }

    public static @NotNull BlockFace yawToFace(float yaw) {
        return (BlockFace)AXIS.get(Math.round(yaw / 90.0F) & 3);
    }

    public static Vector3f rotatedRadius(float radius, float x, float y, float z) {
        return (new Vector3f(0.0F, 0.0F, radius)).rotateX(x).rotateY(y).rotateZ(z);
    }

    public static Vector3f rotatedRadius(float radius, double y) {
        return (new Vector3f(0.0F, 0.0F, radius)).rotateY((float)y);
    }

    public static @NotNull Vector3f getDisplacement(Location from, @NotNull Location to) {
        return to.clone().subtract(from).toVector().toVector3f();
    }

    public static @NotNull Vector3f getDisplacement(Vector3f from, @NotNull Vector3f to) {
        return (new Vector3f(to)).sub(from);
    }

    public static @NotNull Vector3f getDirection(@NotNull Location from, @NotNull Location to) {
        return getDisplacement(from, to).normalize();
    }

    public static @NotNull Vector3f getDirection(@NotNull Vector3f from, @NotNull Vector3f to) {
        return getDisplacement(from, to).normalize();
    }

    public static @NotNull Location getMidpoint(@NotNull Location from, @NotNull Location to) {
        return from.clone().add(to).multiply((double)0.5F);
    }

    public static @NotNull Vector3f getMidpoint(@NotNull Vector3f from, @NotNull Vector3f to) {
        return (new Vector3f(from)).add(to).mul(0.5F);
    }

    private TransformationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        AXIS = new ArrayList(List.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST));
    }
}
