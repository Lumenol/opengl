package com.example.kbourgeois.opengl;

import android.renderscript.Matrix4f;

public interface Drawable {

    String getName();

    Transform getTransform();

    Bounds getBounds();

    void draw(Matrix4f projection, Matrix4f view);

}
