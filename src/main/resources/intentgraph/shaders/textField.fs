//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec2 u_boxLeftBottom;
uniform vec2 u_boxRightTop;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;
varying vec4 v_position;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);
    vec4 position = v_position;
    position = position / position.w;
    if (position.x < u_boxLeftBottom.x || position.x > u_boxRightTop.x || position.y < u_boxLeftBottom.y || position.y > u_boxRightTop.y) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
    } else {
        gl_FragColor = texColor * v_color;
    }
}
