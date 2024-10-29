#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NormalSampler;

uniform mat4 InvWorldMat;

uniform vec3 CameraPos;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 origin = vec3(1151.0, 64.0, -636.0);

    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 posData = InvWorldMat * vec4(texCoord * 2.0 - 1.0, texture(DiffuseDepthSampler, texCoord).r * 2.0 - 1.0, 1.0);
    vec3 pos = (posData.xyz / posData.w) + CameraPos - origin;
    vec3 normal = texture(NormalSampler, texCoord).xyz * 2.0 - 1.0;

    fragColor = vec4(color.rgb + normal * 0.2, 1.0);
}
