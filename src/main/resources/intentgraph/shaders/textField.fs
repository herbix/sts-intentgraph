//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec2 u_boxLeftBottom;
uniform vec2 u_boxRightTop;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;
varying vec2 v_position;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    gl_FragColor = texColor * v_color;
}
