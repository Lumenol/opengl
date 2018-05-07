package com.example.kbourgeois.opengl.Camera;

import android.renderscript.Matrix4f;

public class Orthographic extends Camera {

    private boolean needUpdate = true;

    private float left=-10, right=10;
    private float bottom=-10, top=10;
    private float near=0.1f, far=10;

    public Orthographic() {
    }

    private void update() {
        if (needUpdate == true) {
            projection.loadOrtho(left, right, bottom, top, near, far);
            needUpdate = false;
        }
    }

    @Override
    public Matrix4f getProjection() {
        update();
        return super.getProjection();
    }

    public Orthographic(float left, float right, float bottom, float top, float near, float far) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.near = near;
        this.far = far;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        needUpdate = true;
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        needUpdate = true;
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        needUpdate = true;
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        needUpdate = true;
        this.top = top;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        needUpdate = true;
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        needUpdate = true;
        this.far = far;
    }
}
