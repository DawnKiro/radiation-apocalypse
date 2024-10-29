#version 330

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec3 normal;

out vec4 fragColor;

layout(location = 0) out vec4 outColor;
layout(location = 1) out vec4 outNormal;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    outColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
    outNormal = vec4(normal * 0.5 + 0.5, 1.0);
}
