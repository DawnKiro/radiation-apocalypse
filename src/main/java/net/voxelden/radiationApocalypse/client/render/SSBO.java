package net.voxelden.radiationApocalypse.client.render;

import net.voxelden.radiationApocalypse.RadiationApocalypse;
import org.lwjgl.opengl.GL43;

import java.nio.ByteBuffer;

public class SSBO {
    private final int id;

    public SSBO() {
        id = GL43.glGenBuffers();
    }

    public void update(ByteBuffer buffer) {
        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, id);
        GL43.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, buffer, GL43.GL_DYNAMIC_READ);
        GL43.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, buffer);
        GL43.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
        RadiationApocalypse.LOGGER.info("BUFFER = {}, ERROR = {}", id, GL43.glGetError());
    }

    public void bind(int binding) {
        GL43.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, binding, id);
    }

    public void delete() {
        GL43.glDeleteBuffers(id);
    }
}
