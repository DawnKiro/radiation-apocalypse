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

layout(std430, binding = 0) readonly buffer LightsBuffer {
    Light lights[];
};

in vec2 texCoord;

out vec4 fragColor;

vec4 unpackARGB(int color) {
    return vec4((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF);
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 posData = InvWorldMat * vec4(texCoord * 2.0 - 1.0, texture(DiffuseDepthSampler, texCoord).r * 2.0 - 1.0, 1.0);
    vec3 worldPos = (posData.xyz / posData.w) + CameraPos;
    vec3 normal = texture(NormalSampler, texCoord).xyz * 2.0 - 1.0;
    vec3 totalLight = vec3(0.0);

    for (int i = 0; i < lights.length(); i++) {
        Light light = lights[i];
        float dist = length(worldPos - light.pos);
        if (dist < light.radius) {
            vec3 lightColor = unpackARGB(light.color).rgb;
            if (distance(lightColor, totalLight) > 0.05) {
                float normalInfluence = dot(normalize(worldPos - light.pos), normal) * 0.5 + 1.0;
                float lightInfluence = clamp((1.0 - dist / light.radius) * 1.4 - (normalInfluence * 0.8 - 0.2), 0.0, 1.0);
                totalLight = max(totalLight, lightColor * lightInfluence);
            }
        }
    }
    totalLight /= 255.0;

    fragColor = vec4(color.rgb * totalLight + texture(DataSampler, texCoord).xyz, 1.0);
}
