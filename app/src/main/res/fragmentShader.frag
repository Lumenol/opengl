#version 300 es
precision mediump float;

in vec3 frag_pos;
in vec3 frag_color;

out vec3 color;

void main(){
    color = frag_color;
}