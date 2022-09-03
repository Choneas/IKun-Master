#ifdef GL_ES
precision mediump float;
#endif

uniform float time;

uniform vec2 resolution;

#define speed 0.9
#define freq 1.0
#define amp 0.9
#define phase 0.8


void main( void ) {
    vec2 p = ( gl_FragCoord.xy / resolution.xy ) - 0.1;

    float sx = (amp)*1.9 * sin( 4.0 * (freq) * (p.x-phase) - 1.0 * (speed)*time);

    float dy = 43. / ( 60. * abs(4.9*p.y - sx - 1.2));

    gl_FragColor = vec4( (p.x + 0.05) * dy, 0.2 * dy, dy, 2.0 );
}