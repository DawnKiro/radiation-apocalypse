#version 430

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NormalSampler;
uniform sampler2D DataSampler;

uniform mat4 InvWorldMat;

uniform vec3 CameraPos;

struct Light {
  vec3 pos;
  float radius;
  int color;
  int type;
  ivec2 rotation;
};

layout(std430, binding = 0) buffer LightsBuffer {
    int lightsLength;
    ivec3 lightsLengthadwasdsdef;
    Light lights[];
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 origin = vec3(1151.0, 64.0, -636.0);

    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 posData = InvWorldMat * vec4(texCoord * 2.0 - 1.0, texture(DiffuseDepthSampler, texCoord).r * 2.0 - 1.0, 1.0);
    vec3 worldPos = (posData.xyz / posData.w) + CameraPos - origin;
    vec3 normal = texture(NormalSampler, texCoord).xyz * 2.0 - 1.0;

    float normalInfluence = dot(normalize(worldPos), normal) * 0.5 + 1.0;
    float lightInfluence = pow(max(0.0, 1.0 - length(worldPos) / 127.0), 1.4) - 0.4 * normalInfluence;
    fragColor = vec4(max(vec3(0.0), color.rgb * lightInfluence) + texture(DataSampler, texCoord).xyz, 1.0);
    fragColor.r += lights[0].pos.x;
}
