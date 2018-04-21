package com.example.kbourgeois.opengl;

import android.renderscript.Matrix4f;

import java.util.Map;

public interface Drawable {

    String getName();

    Transform getTransform();

    Bounds getBounds();

    void draw(Matrix4f projection, Matrix4f view);

}
