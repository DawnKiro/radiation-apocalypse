package net.voxelden.radiationApocalypse.client.render.light;

import org.joml.Vector3f;

public class Light {
    private float radius;
    private int color;
    private Type type;
    private Vector3f pos;


    public Light(float radius, int color, Type type, Vector3f pos) {
        this.radius = radius;
        this.color = color;
        this.type = type;
        this.pos = pos;
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

    public int pitch() {
        return 0;
    }

    public int yaw() {
        return 0;
    }

    public enum Type {
        POINT,
        SPOT,
        GLOBAL
    }
}
