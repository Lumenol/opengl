package com.example.kbourgeois.opengl.GameObect;

import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;

import java.util.Observable;
import java.util.Observer;

public class Transform extends Observable implements Observer {
    private Transform parent;

    private boolean needUpdate = true;

    private Float3 offset = new Float3();
    private Float4 position = new Float4();
    private Float4 rotation = new Float4();
    private Float4 scale = new Float4();
    private Float4 localPosition = new Float4(0, 0, 0, 1);
    private Float4 localRotation = new Float4(0, 0, 0, 1);
    private Float4 localScale = new Float4(1, 1, 1, 1);
    private Matrix4f localToWorldMatrix = new Matrix4f();

    public Transform() {
    }

    public Float3 getOffset() {
        return new Float3(offset);
    }

    public void setOffset(Float3 offset) {
        this.offset.set(offset);
        needUpdate = true;
    }

    public void setChanged() {
        needUpdate = true;
        super.setChanged();
        super.notifyObservers();
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
        setChanged();
    }

    public Float3 getPosition() {
        update();
        return new Float3(position);
    }

    public void setPosition(Float3 position) {
        if (parent == null) {
            this.localPosition.set(position.getX(), position.getY(), position.getZ(), 1);
        } else {
            parent.update();
            this.localPosition.set(position.getX() - parent.position.getX(), position.getY() - parent.position.getY(), position.getZ() - parent.position.getZ());
        }
        setChanged();
    }

    public Float3 getRotation() {
        update();
        return new Float3(rotation);
    }

    public void setRotation(Float3 rotation) {
        if (parent == null) {
            this.localRotation.set(rotation.getX(), rotation.getY(), rotation.getZ(), 1);
        } else {
            parent.update();
            this.localRotation.set(rotation.getX() - parent.rotation.getX(), rotation.getY() - parent.rotation.getY(), rotation.getZ() - parent.rotation.getZ());
        }
        setChanged();
    }

    public Float3 getScale() {
        update();
        return new Float3(scale);
    }

    public void setScale(Float3 scale) {
        if (parent == null) {
            this.localScale.set(scale.getX(), scale.getY(), scale.getZ(), 1);
        } else {
            parent.update();
            this.localScale.set(scale.getX() / parent.scale.getX(), scale.getY() / parent.scale.getY(), scale.getZ() / parent.scale.getZ());
        }
        setChanged();
    }

    public Float3 getLocalPosition() {
        update();
        return new Float3(localPosition);
    }

    public void setLocalPosition(Float3 localPosition) {
        this.localPosition.set(localPosition.getX(), localPosition.getY(), localPosition.getZ(), 1);
        setChanged();
    }

    public Float3 getLocalRotation() {
        update();
        return new Float3(localRotation);
    }

    public void setLocalRotation(Float3 localRotation) {
        this.localRotation.set(localRotation.getX(), localRotation.getY(), localRotation.getZ(), 1);
        setChanged();
    }

    public Float3 getLocalScale() {
        update();
        return new Float3(localScale);
    }

    public void setLocalScale(Float3 localScale) {
        this.localScale.set(localScale.getX(), localScale.getY(), localScale.getZ(), 1);
        setChanged();
    }

    public Matrix4f getLocalToWorldMatrix() {
        update();
        return new Matrix4f(localToWorldMatrix.getArray());
    }

    private void update() {
        if (needUpdate) {
            if (null != parent) {
                parent.update();
                this.position.set(parent.position.getX() + localPosition.getX(), parent.position.getY() + localPosition.getY(), parent.position.getZ() + localPosition.getZ());
                this.rotation.set(parent.rotation.getX() + localRotation.getX(), parent.rotation.getY() + localRotation.getY(), parent.rotation.getZ() + localRotation.getZ());
                this.scale.set(parent.scale.getX() * localScale.getX(), parent.scale.getY() * localScale.getY(), parent.scale.getZ() * localScale.getZ());
            } else {
                position.set(localPosition);
                rotation.set(localRotation);
                scale.set(localScale);
            }

            localToWorldMatrix.loadTranslate(position.getX(), position.getY(), position.getZ());
            localToWorldMatrix.scale(scale.getX(), scale.getY(), scale.getZ());
            localToWorldMatrix.rotate(rotation.getX(), 1, 0, 0);
            localToWorldMatrix.rotate(rotation.getY(), 0, 1, 0);
            localToWorldMatrix.rotate(rotation.getZ(), 0, 0, 1);
            localToWorldMatrix.translate(-offset.getX(), -offset.getY(), -offset.getZ());

            needUpdate = false;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == parent) {
            setChanged();
        }
    }
}
