package com.example.kbourgeois.opengl;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;

import java.util.Observable;
import java.util.Observer;

public class Transform extends Observable implements Observer {
    private Transform parent;

    private Float3 offset = new Float3();

    public Float3 getOffset() {
        return new Float3(offset);
    }

    public void setOffset(Float3 offset) {
        this.offset.set(offset);
        update();
    }

    private Float4 position = new Float4();
    private Float4 rotation = new Float4();
    private Float4 scale = new Float4();

    private Float4 localPosition = new Float4(0, 0, 0, 1);
    private Float4 localRotation = new Float4(0, 0, 0, 1);
    private Float4 localScale = new Float4(1, 1, 1, 1);

    private Matrix4f localToWorldMatrix = new Matrix4f();
    private Matrix4f worldToLocalMatrix = new Matrix4f();

    public Transform() {
        update();
    }

    public Transform getParent() {
        return parent;
    }

    public void setParent(Transform parent) {
        if (null != parent) {
            parent.deleteObserver(this);
        }
        this.parent = parent;
        if (null != parent) {
            parent.addObserver(this);
        }
        update();
    }

    public Float3 getPosition() {
        return new Float3(position);
    }

    public void setPosition(Float3 position) {
        if (parent == null) {
            this.localPosition.set(position.getX(), position.getY(), position.getZ(), 1);
        } else {
            Matrix.multiplyMV(this.localPosition.getArray(), 0, parent.worldToLocalMatrix.getArray(), 0, new float[]{position.getX(), position.getY(), position.getZ(), 1}, 0);
        }
        update();
    }

    public Float3 getRotation() {
        return new Float3(rotation);
    }

    public void setRotation(Float3 rotation) {
        if (parent == null) {
            this.localRotation.set(rotation.getX(), rotation.getY(), rotation.getZ(), 1);
        } else {
            Matrix.multiplyMV(this.localRotation.getArray(), 0, parent.worldToLocalMatrix.getArray(), 0, new float[]{rotation.getX(), rotation.getY(), rotation.getZ(), 1}, 0);
        }
        update();
    }

    public Float3 getScale() {
        return new Float3(scale);
    }

    public void setScale(Float3 scale) {
        if (parent == null) {
            this.localScale.set(scale.getX(), scale.getY(), scale.getZ(), 1);
        } else {
            Matrix.multiplyMV(this.localScale.getArray(), 0, parent.worldToLocalMatrix.getArray(), 0, new float[]{scale.getX(), scale.getY(), scale.getZ(), 1}, 0);
        }
        update();
    }

    public Float3 getLocalPosition() {
        return new Float3(localPosition);
    }

    public void setLocalPosition(Float3 localPosition) {
        this.localPosition.set(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1);
        update();
    }

    public Float3 getLocalRotation() {
        return new Float3(localRotation);
    }

    public void setLocalRotation(Float3 localRotation) {
        this.localRotation.set(localRotation.getX(), localRotation.getY(), localRotation.getZ(), 1);
        update();
    }

    public Float3 getLocalScale() {
        return new Float3(localScale);
    }

    public void setLocalScale(Float3 localScale) {
        this.localScale.set(localScale.getX(), localScale.getY(), localScale.getZ(), 1);
        update();
    }

    public Matrix4f getLocalToWorldMatrix() {
        return new Matrix4f(localToWorldMatrix.getArray());
    }

    public Matrix4f getWorldToLocalMatrix() {
        return new Matrix4f(worldToLocalMatrix.getArray());
    }

    private void update() {

        if (null != parent) {
            Matrix.multiplyMV(this.position.getArray(), 0, parent.localToWorldMatrix.getArray(), 0, this.localPosition.getArray(), 0);
            Matrix.multiplyMV(this.rotation.getArray(), 0, parent.localToWorldMatrix.getArray(), 0, this.localRotation.getArray(), 0);
            Matrix.multiplyMV(this.scale.getArray(), 0, parent.localToWorldMatrix.getArray(), 0, this.localScale.getArray(), 0);
            localToWorldMatrix.load(parent.localToWorldMatrix);
        } else {
            position.set(localPosition);
            rotation.set(localRotation);
            scale.set(localScale);
            localToWorldMatrix.loadIdentity();
        }

        localToWorldMatrix.translate(localPosition.getX(), localPosition.getY(), localPosition.getZ());
        localToWorldMatrix.scale(localScale.getX(), localScale.getY(), localScale.getZ());
        localToWorldMatrix.rotate(localRotation.getX(), 1, 0, 0);
        localToWorldMatrix.rotate(localRotation.getY(), 0, 1, 0);
        localToWorldMatrix.rotate(localRotation.getZ(), 0, 0, 1);
        localToWorldMatrix.translate(-offset.getX(), -offset.getY(), -offset.getZ());


        worldToLocalMatrix.load(localToWorldMatrix);
        worldToLocalMatrix.inverse();

        setChanged();
        notifyObservers();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == parent) {
            update();
        }
    }
}
