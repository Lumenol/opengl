package com.example.kbourgeois.opengl;

import android.opengl.Matrix;
import android.renderscript.Float3;
import android.util.Log;

import java.util.Arrays;

public class Transform {
    private Transform parent;
    private float center[];
    private float translate[];
    private float rotate[];
    private float scale[];

    private float matrix[] = new float[16];

    public Transform() {
        center = new float[]{0, 0, 0};
        translate = new float[]{0, 0, 0};
        rotate = new float[]{0, 0, 0};
        scale = new float[]{1, 1, 1};
        calculMatrix();
    }

    private void calculMatrix() {
        float rotation[] = new float[16];
        float tmpR[] = new float[16];
        float tmp[] = new float[16];
        float translateRotated[] = new float[4];
        float translate[] = new float[4];

        for (int i = 0; i < this.translate.length; i++) {
            translate[i] = this.translate[i];
        }
        translate[3] = 1;


        //Matrix.setIdentityM(rotation, 0);

        Matrix.setRotateM(rotation, 0, getRotateX(), 1, 0, 0);
        Matrix.setRotateM(tmpR, 0, getRotateY(), 0, 1, 0);
        Matrix.multiplyMM(tmp, 0, rotation, 0, tmpR, 0);
        Matrix.setRotateM(tmpR, 0, getRotateZ(), 0, 0, 1);

        Matrix.setRotateEulerM(rotation, 0, getRotateX(), getRotateY(), getRotateZ());

        Matrix.multiplyMM(rotation, 0, tmp, 0, tmpR, 0);
        Matrix.multiplyMV(translateRotated, 0, rotation, 0, translate, 0);


        Matrix.setIdentityM(tmp, 0);

        Matrix.translateM(tmp, 0, -getScaleX() * getCenterX(), -getScaleY() * getCenterY(), -getScaleZ() * getCenterZ());
        Matrix.multiplyMM(matrix, 0, rotation, 0, tmp, 0);
        Matrix.translateM(matrix, 0, translateRotated[0], translateRotated[1], translateRotated[2]);
        Matrix.scaleM(matrix, 0, getScaleX(), getScaleY(), getScaleZ());
    }

    public void setTranslate(float x, float y, float z) {
        translate[0] = x;
        translate[1] = y;
        translate[2] = z;
        calculMatrix();
    }

    public void setRotate(float x, float y, float z) {
        rotate[0] = x;
        rotate[1] = y;
        rotate[2] = z;
        calculMatrix();
    }

    public void setScale(float x, float y, float z) {
        scale[0] = x;
        scale[1] = y;
        scale[2] = z;
        calculMatrix();
    }

    public void setCenter(float x, float y, float z) {
        center[0] = x;
        center[1] = y;
        center[2] = z;
        calculMatrix();
    }

    public float getTranslateX() {
        return translate[0];
    }

    public void setTranslateX(float x) {
        translate[0] = x;
        calculMatrix();
    }

    public float getTranslateY() {
        return translate[1];
    }

    public void setTranslateY(float y) {
        translate[1] = y;
        calculMatrix();
    }

    public float getTranslateZ() {
        return translate[2];
    }

    public void setTranslateZ(float z) {
        translate[2] = z;
        calculMatrix();
    }

    public float getRotateX() {
        return rotate[0];
    }

    public void setRotateX(float x) {
        rotate[0] = x;
        calculMatrix();
    }

    public float getRotateY() {
        return rotate[1];
    }

    public void setRotateY(float y) {
        rotate[1] = y;
        calculMatrix();
    }

    public float getRotateZ() {
        return rotate[2];
    }

    public void setRotateZ(float z) {
        rotate[2] = z;
        calculMatrix();
    }

    public float getScaleX() {
        return scale[0];
    }

    public void setScaleX(float x) {
        scale[0] = x;
        calculMatrix();
    }

    public float getScaleY() {
        return scale[1];
    }

    public void setScaleY(float y) {
        scale[1] = y;
        calculMatrix();
    }

    public float getScaleZ() {
        return scale[2];
    }

    public void setScaleZ(float z) {
        scale[2] = z;
        calculMatrix();
    }

    public float[] getMatrix() {
        if (null == parent) {
            return matrix.clone();
        }
        float mult[] = new float[16];
        Matrix.multiplyMM(mult, 0, parent.getMatrix(), 0, matrix, 0);
        return mult;
    }

    public float getCenterX() {
        return center[0];
    }

    public void setCenterX(float x) {
        center[0] = x;
        calculMatrix();
    }

    public float getCenterY() {
        return center[1];
    }

    public void setCenterY(float y) {
        center[1] = y;
        calculMatrix();
    }

    public float getCenterZ() {
        return center[2];
    }

    public void setCenterZ(float z) {
        center[2] = z;
        calculMatrix();
    }

    public Transform(Transform t) {
        parent = t.parent;
        center = t.center.clone();
        translate = t.translate.clone();
        rotate = t.rotate.clone();
        scale = t.scale.clone();
        calculMatrix();
    }

    public Transform getParent() {
        return parent;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }
}
