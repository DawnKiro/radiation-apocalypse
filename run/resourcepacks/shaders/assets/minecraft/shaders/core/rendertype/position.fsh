#version 330

uniform vec4 ColorModulator;

layout(location = 0) out vec4 fragColor;
layout(location = 1) out vec4 fragNormal;
layout(location = 2) out vec4 fragData;

void main() {
    fragColor = ColorModulator;
    fragNormal = vec4(vec3(0.5), 1.0);
    fragData = vec4(vec3(0.0), 1.0);
}
