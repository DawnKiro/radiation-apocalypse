package net.voxelden.radiationApocalypse.client.render;

import org.lwjgl.opengl.GL43;

import java.nio.ByteBuffer;

public class SSBO {
    private final int id;
    private int size;

    public SSBO(int binding) {
        id = GL43.glGenBuffers();

        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, id);
        GL43.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, ByteBuffer.allocate(0), GL43.GL_DYNAMIC_DRAW);
        GL43.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, binding, id);
        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void update(ByteBuffer buffer) {
        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, id);

        buffer.rewind();
        int bufferSize = buffer.remaining();
        if (size != bufferSize) {
            GL43.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, bufferSize, GL43.GL_DYNAMIC_DRAW);
            size = bufferSize;
        }

        GL43.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);
        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void delete() {
        GL43.glDeleteBuffers(id);
    }
}
