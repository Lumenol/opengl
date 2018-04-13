#version 300 es
uniform mat4 MVP;
in vec3 vertex_pos;
in vec3 vertex_color;

out vec3 frag_pos;
out vec3 frag_color;

void main() {
    gl_Position = MVP * vec4(vertex_pos,1.0);
    frag_pos = vertex_pos;
    frag_color = vertex_color;
}