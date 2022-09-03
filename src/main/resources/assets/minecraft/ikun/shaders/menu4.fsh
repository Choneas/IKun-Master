#ifdef GL_ES
precision mediump float;
#endif

#define TAU 6.28318530718
#define MAX_ITER 10

#extension GL_OES_standard_derivatives : enable

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main() {

    float letime = time * 0.25;

    // uv should be the 0-1 uv of texture...
    vec2 uv = gl_FragCoord.xy / resolution.xy;


    vec2 p = mod(uv*TAU, TAU)-200.0;

    vec2 i = vec2(p);
    float c = 2.0;
    float inten = 0.005;

    for (int n = 0; n < MAX_ITER; n++)
    {
        float t = letime * (0.5 - (4.5 / float(n+1)));
        i = p + vec2(cos(t + i.y) - sin(t + i.y), sin(t - i.y) + cos(t + i.x));
        c += 1.0/length(vec2(p.y / (sin(i.x + t) / inten), p.y / (cos(i.y + t)/inten)));
    }

    c /= float(MAX_ITER);

    c = 1.17-pow(c, 1.5);

    vec3 colour = vec3(pow(abs(c), 8.0));

    colour = clamp(colour + vec3(0.72549019607, 0.06666666666, 1), 0.0, 2.0);

    gl_FragColor = vec4(colour, 1.0);
}