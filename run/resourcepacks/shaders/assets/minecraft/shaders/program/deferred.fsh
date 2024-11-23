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

// Uniforms for SVO
layout(std430, binding = 0) buffer OctreeBuffer {
    uint octree[]; // Sparse Voxel Octree (flat buffer representation)
};

uniform vec3 gridMin; // Minimum bounds of the grid in world space
uniform vec3 gridMax; // Maximum bounds of the grid in world space
uniform float voxelSize; // Base size of the smallest voxel
uniform int maxLevels;   // Maximum depth of the octree

// Function to fetch material properties
// `id`: Material ID
// `offset`: Position within the voxel (0..1 scale for the current voxel size)
vec4 material(int id, vec3 offset) {
    return vec4(1.0);
}

// Helper function to decode a node
bool isPointerNode(uint node) {
    return (node & 0x80000000u) != 0u;
}

uint getMaterialID(uint node) {
    return node & 0x1Fu; // Lower 5 bits for material ID
}

uint getChildPointer(uint node) {
    return node & 0x7FFFFFFFu; // Remove high bit
}

// Main traversal function
vec4 traverseVoxelGrid(vec3 origin, vec3 direction, float maxDistance) {
    // Initialization
    vec3 invDir = 1.0 / direction; // Reciprocal of the ray direction
    vec3 tDelta = voxelSize * abs(invDir); // Step size for t along each axis
    vec3 gridScale = gridMax - gridMin;   // Dimensions of the voxel grid
    vec3 scaledOrigin = (origin - gridMin) / gridScale; // Normalize origin to grid scale

    // Determine initial voxel
    vec3 currentVoxelPos = floor(scaledOrigin * (1.0 / voxelSize));
    vec3 step = sign(direction); // +1 or -1 step direction for each axis
    vec3 tMax = ((currentVoxelPos + (step + 1.0) * 0.5) * voxelSize - scaledOrigin) * invDir;

    // Accumulate color and alpha
    vec4 accumulatedColor = vec4(0.0); // RGBA, initially fully transparent

    float traveledDistance = 0.0;

    while (traveledDistance < maxDistance) {
        // Descend the octree to find the current voxel's material
        uint nodeIndex = 0; // Start at root
        vec3 voxelOffset = fract(scaledOrigin * (1.0 / voxelSize)); // Position within the voxel
        int level = 0;

        while (level < maxLevels) {
            uint node = octree[nodeIndex];

            if (!isPointerNode(node)) {
                // Material node: Fetch properties
                int materialID = int(getMaterialID(node));
                vec4 matProps = material(materialID, voxelOffset);

                // Blend the color and alpha
                accumulatedColor.rgb += (1.0 - accumulatedColor.a) * matProps.rgb * matProps.a;
                accumulatedColor.a += (1.0 - accumulatedColor.a) * matProps.a;

                // Terminate if fully opaque
                if (accumulatedColor.a >= 1.0) {
                    return accumulatedColor;
                }

                break;
            }

            // Pointer node: Descend to the child node
            uint childPointer = getChildPointer(node);
            int childIndex = int((voxelOffset.x > 0.5 ? 1 : 0) + 
                                 (voxelOffset.y > 0.5 ? 2 : 0) + 
                                 (voxelOffset.z > 0.5 ? 4 : 0));
            nodeIndex = childPointer + childIndex;
            voxelOffset *= 2.0; // Rescale offset to child voxel
            voxelOffset = fract(voxelOffset);
            level++;
        }

        // Determine next voxel step
        if (tMax.x < tMax.y) {
            if (tMax.x < tMax.z) {
                currentVoxelPos.x += step.x;
                traveledDistance = tMax.x;
                tMax.x += tDelta.x;
            } else {
                currentVoxelPos.z += step.z;
                traveledDistance = tMax.z;
                tMax.z += tDelta.z;
            }
        } else {
            if (tMax.y < tMax.z) {
                currentVoxelPos.y += step.y;
                traveledDistance = tMax.y;
                tMax.y += tDelta.y;
            } else {
                currentVoxelPos.z += step.z;
                traveledDistance = tMax.z;
                tMax.z += tDelta.z;
            }
        }

        // If out of bounds, terminate traversal
        if (any(lessThan(currentVoxelPos, vec3(0.0))) || any(greaterThanEqual(currentVoxelPos * voxelSize, gridScale))) {
            break;
        }
    }

    return accumulatedColor; // Return the accumulated RGBA color
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
            float normalInfluence = dot(normalize(worldPos - light.pos), normal) * 0.5 + 1.0;
            float lightInfluence = clamp((1.0 - dist / light.radius) * 1.4 - (normalInfluence * 0.8 - 0.2), 0.0, 1.0);
            totalLight = max(totalLight, unpackARGB(light.color).rgb * lightInfluence);
        }
    }
    totalLight /= 255.0;

    fragColor = vec4(color.rgb * totalLight + texture(DataSampler, texCoord).xyz, 1.0);
}
