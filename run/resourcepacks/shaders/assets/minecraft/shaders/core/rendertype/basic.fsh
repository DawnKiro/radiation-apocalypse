#version 430

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;
uniform float AlphaCutoff;

in vec4 vertexColor;
in vec2 texCoord0;
in vec3 normal;

layout(location = 0) out vec4 fragColor;
layout(location = 1) out vec4 fragNormal;
layout(location = 2) out vec4 fragData;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < AlphaCutoff) discard;
    
    fragColor = color;
    fragNormal = vec4(normal * 0.5 + 0.5, 1.0);
    fragData = vec4(texture(Sampler1, texCoord0).rgb, 1.0);
}
