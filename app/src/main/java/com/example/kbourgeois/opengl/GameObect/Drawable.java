package com.example.kbourgeois.opengl.GameObect;

import com.example.kbourgeois.opengl.Bounds.Bounds;
import com.example.kbourgeois.opengl.Camera.Camera;

public interface Drawable {

    String getName();

    Transform getTransform();

    Bounds getBounds();

    Drawable clone();

    void draw(Camera camera);

}
