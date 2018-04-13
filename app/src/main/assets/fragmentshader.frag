#version 300 es

precision mediump float;

uniform sampler2D tex;
in vec3 _vNormal;
in vec2 _vTexCoord;

in mat4 modelFrag;
in vec3 fragPos;

//uniform vec3 lPosition;
//uniform vec3 lColor;
//uniform vec3 cPosition;

float ambientStrength = 0.2;
float specularStrength = 1.0;
float diffuseStrength = 1.0;

out vec4 color;
void main() {

vec3 lPosition = vec3(0, 2, 0);
vec3 lColor = vec3(1,1,1);
vec3 cPosition = vec3(0,1, 2);
  float dist = distance(lPosition, fragPos);

  mat3 normalMatrix = transpose(inverse(mat3(modelFrag)));
  vec3 norm = normalize(normalMatrix * _vNormal);
  vec3 lDirection = normalize(lPosition - fragPos);

  float theta = clamp(dot(norm, lDirection), 0.0, 1.0);
  vec3 diffuse = (diffuseStrength * theta * lColor) / (dist * dist);

  vec3 viewDir = normalize(cPosition - fragPos);
  vec3 reflectDir = reflect(-lDirection, norm);

  float spec = pow(clamp(dot(viewDir, reflectDir), 0.0, 1.0), 5.0);
  vec3 specular = (specularStrength * spec * lColor)  / (dist * dist);

  vec3 ambient = ambientStrength * lColor;
  vec3 result = (ambient + diffuse + specular) * texture(tex, _vTexCoord).rgb;
  //result = (ambient + diffuse + specular) * vec3(1.0, 0.0, 0.0);
  color = vec4(result, 1.0);

}