package com.example.kbourgeois.opengl;

import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.Camera.Camera;

public interface Drawable{

    String getName();

    Transform getTransform();

    Bounds getBounds();

    Drawable clone();

    void draw(Camera camera);

}
