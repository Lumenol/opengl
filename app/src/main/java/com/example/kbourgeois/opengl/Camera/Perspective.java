package com.example.kbourgeois.opengl.Camera;

import android.renderscript.Matrix4f;

public class Perspective extends Camera {

    private boolean needUpdate = true;

    private float fovy = 70.0f;
    private float aspect = 9.0f / 16.0f;
    private float near = .1f, far = 100f;

    public Perspective() {
    }

    public Perspective(float fovy, float aspect, float near, float far) {
        this.fovy = fovy;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
    }

    private void update() {
        if (needUpdate == true) {
            projection.loadPerspective(fovy, aspect, near, far);
            needUpdate = false;
        }
    }

    @Override
    public Matrix4f getProjection() {
        update();
        return super.getProjection();
    }

    public float getFovy() {
        return fovy;
    }

    public void setFovy(float fovy) {
        needUpdate = true;
        this.fovy = fovy;
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        needUpdate = true;
        this.aspect = aspect;
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
