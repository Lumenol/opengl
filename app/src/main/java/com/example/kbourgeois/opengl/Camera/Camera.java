package com.example.kbourgeois.opengl.Camera;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;
import com.example.kbourgeois.opengl.Transform;

import java.util.Observable;
import java.util.Observer;

public abstract class Camera implements Observer {
    private Float4 eye = new Float4(0, 0, 0, 1);
    private Float4 center = new Float4(0, 0, 1, 1);
    private Float4 up = new Float4(0, 1, 0, 1);

    private Transform transform = new Transform();
    private Matrix4f view = new Matrix4f();
    protected Matrix4f projection = new Matrix4f();

    private boolean needUpdate = true;

    public Camera() {
        transform.addObserver(this);
    }

    public Matrix4f getProjection() {
        return new Matrix4f(projection.getArray());
    }

    public Transform getTransform() {
        return transform;
    }

    public Matrix4f getView() {
        update();
        return new Matrix4f(view.getArray());
    }

    private void update() {
        if (needUpdate == true) {
            Float4 eye = new Float4();
            Float4 center = new Float4();
            Float4 up = new Float4();

            Matrix4f localToWorldMatrix = transform.getLocalToWorldMatrix();
            Matrix.multiplyMV(eye.getArray(), 0, localToWorldMatrix.getArray(), 0, this.eye.getArray(), 0);
            Matrix.multiplyMV(center.getArray(), 0, localToWorldMatrix.getArray(), 0, this.center.getArray(), 0);
            Matrix.multiplyMV(up.getArray(), 0, localToWorldMatrix.getArray(), 0, this.up.getArray(), 0);

            Matrix.setLookAtM(view.getArray(), 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), up.getX(), up.getY(), up.getZ());

            needUpdate = false;
        }
    }

    public void set(Float3 eye, Float3 center) {
        transform.setPosition(eye);

        float angleY = angleEntre(-1,0, eye.getZ()-center.getZ(), eye.getX()-center.getX());
        float angleX = angleEntre(  eye.getZ()-center.getZ(), eye.getY()-center.getY(),1,0);

        Float3 rotation = transform.getRotation();
        rotation.set(angleX, angleY);
        transform.setRotation(rotation);
    }

    private float angleEntre(float ux, float uy, float vx, float vy) {
        float ps = ux * vx + uy * vy;
        float nu2 = ux * ux + uy * uy;
        float nv2 = vx * vx + vy * vy;

        float det = ux * vy - uy * vx;
        int sigdet = det >= 0 ? 1 : -1;
        double acos = Math.acos(ps / Math.sqrt(nu2 * nv2));
        return (float) Math.toDegrees(sigdet * acos);
    }

    @Override
    public void update(Observable o, Object arg) {
        needUpdate = true;
    }
}
