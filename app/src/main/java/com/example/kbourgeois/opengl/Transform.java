package com.example.kbourgeois.opengl;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;

import java.util.Observable;
import java.util.Observer;

public class Transform extends Observable implements Observer {
    private Transform parent;
    private Float3 center;
    private Float3 translate;
    private Float3 rotate;
    private Float3 scale;

    private boolean hasChanged = true;

    private Matrix4f matrix = new Matrix4f();

    public Transform() {
        center = new Float3(0, 0, 0);
        translate = new Float3(0, 0, 0);
        rotate = new Float3(0, 0, 0);
        scale = new Float3(1, 1, 1);

        center.addObserver(this);
        translate.addObserver(this);
        rotate.addObserver(this);
        scale.addObserver(this);
    }

    private void calculMatrix() {
        Float4 translateRotated = new Float4();
        Float4 translate = new Float4(this.translate.getX(), this.translate.getY(), this.translate.getZ(), 1);

        Matrix4f rotation = new Matrix4f();
        rotation.loadRotate(rotate.getX(), 1, 0, 0);
        rotation.rotate(rotate.getY(), 0, 1, 0);
        rotation.rotate(rotate.getZ(), 0, 0, 1);

        Matrix.multiplyMV(translateRotated.getArray(), 0, rotation.getArray(), 0, translate.getArray(), 0);

        matrix.loadTranslate(-scale.getX() * center.getX(), -scale.getY() * center.getY(), -scale.getZ() * center.getZ());
        matrix.loadMultiply(rotation, matrix);
        matrix.translate(translateRotated.getX(), translateRotated.getY(), translateRotated.getZ());
        matrix.scale(scale.getX(), scale.getY(), scale.getZ());

        if (null != parent) {
            matrix.loadMultiply(parent.getMatrix(), matrix);
        }

        hasChanged = false;
    }

    public Float3 getTranslate() {
        return translate;
    }

    public Float3 getRotate() {
        return rotate;
    }

    public Float3 getScale() {
        return scale;
    }

    public Matrix4f getMatrix() {
        if (hasChanged) {
            calculMatrix();
        }
        return matrix;
    }

    public Float3 getCenter() {
        return center;
    }

    public Transform(Transform t) {
        parent = t.parent;
        center = new Float3(t.center);
        translate = new Float3(t.translate);
        rotate = new Float3(t.rotate);
        scale = new Float3(t.scale);

        center.addObserver(this);
        translate.addObserver(this);
        rotate.addObserver(this);
        scale.addObserver(this);
    }

    public Transform getParent() {
        return parent;
    }

    public void setParent(Transform parent) {
        if (this.parent != null) {
            parent.deleteObserver(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            parent.addObserver(this);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == center || o == translate || o == rotate || o == scale || o == parent) {
            hasChanged = true;
            setChanged();
            notifyObservers();
        }
    }
}
