#version 300 es

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

in vec3 vPosition;
in vec3 vNormal;
in vec2 vTexCoord;

out vec3 _vNormal;
out vec2 _vTexCoord;

out mat4 modelFrag;
out vec3 fragPos;

void main() {
    gl_Position =  projection * view * model * vec4(vPosition, 1.0);

    _vTexCoord = vTexCoord;
    _vNormal = vNormal;
    fragPos = vec3(model * vec4(vPosition, 1.0));
    modelFrag = model;
}