package net.voxelden.radiationApocalypse.client.render.light;

import net.minecraft.util.math.ColorHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Light {
    private float radius;
    private int color;
    private Type type;
    private Vector3f pos;
    private Vector2f rotation;

    public Light(float radius, int color, Type type, Vector3f pos, Vector2f rotation) {
        this.radius = radius;
        this.color = color;
        this.type = type;
        this.pos = pos;
        this.rotation = rotation;
    }

    public Light(float radius, int red, int green, int blue, Type type, Vector3f pos, Vector2f rotation) {
        this(radius, ColorHelper.Argb.getArgb(red, green, blue), type, pos, rotation);
    }

    public Light(float radius, int red, int green, int blue, Type type, Vector3f pos) {
        this(radius, red, green, blue, type, pos, new Vector2f());
    }

    public Light(float radius, int red, int green, int blue, Type type) {
        this(radius, red, green, blue, type, new Vector3f());
    }

    public int color() {
        return color;
    }

    public void color(int color) {
        this.color = color;
    }

    public Type type() {
        return type;
    }

    public void type(Type type) {
        this.type = type;
    }

    public float radius() {
        return radius;
    }

    public void radius(float radius) {
        this.radius = radius;
    }

    public Vector3f pos() {
        return pos;
    }

    public void pos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector2f rotation() {
        return rotation;
    }

    public void rotation(Vector2f rotation) {
        this.rotation = rotation;
    }

    public enum Type {
        POINT,
        SPOT,
        GLOBAL
    }
}
