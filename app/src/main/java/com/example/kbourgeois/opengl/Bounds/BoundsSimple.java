package com.example.kbourgeois.opengl.Bounds;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;
import com.example.kbourgeois.opengl.GameObect.Transform;

import java.util.Observable;
import java.util.Observer;

public class BoundsSimple extends Observable implements Observer, Bounds {

    private Float4 localMin = new Float4(0, 0, 0, 1);
    private Float4 localMax = new Float4(0, 0, 0, 1);
    private float[] pointsExterieurs;
    private Float3 min = new Float3();
    private Float3 max = new Float3();
    private Float3 size = new Float3();
    private Float4 center = new Float4(0, 0, 0, 1);
    private Float3 localCenter = new Float4(0, 0, 0, 1);
    private Transform transform;

    private boolean needUpdate = true;

    public BoundsSimple(final Float3 localMin, final Float3 localMax) {
        this.localMin.set(localMin.getX(), localMin.getY(), localMin.getZ());
        this.localMax.set(localMax.getX(), localMax.getY(), localMax.getZ());
        calculPointsExterieur();
        calculLocalCenter();
        update();
    }

    public BoundsSimple(float vertices[], int coords_per_vertex, int shift) {
        localMin.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        localMax.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (int i = 0; i < vertices.length; i += 3 + shift) {
            for (int j = 0; j < coords_per_vertex; j++) {
                localMin.set(Math.min(localMin.getX(), vertices[i]), Math.min(localMin.getY(), vertices[i + 1]), Math.min(localMin.getZ(), vertices[i + 2]));
                localMax.set(Math.max(localMax.getX(), vertices[i]), Math.max(localMax.getY(), vertices[i + 1]), Math.max(localMax.getZ(), vertices[i + 2]));
            }
        }
        calculPointsExterieur();
        calculLocalCenter();
        update();
    }

    public Float3 getLocalMin() {
        return new Float3(localMin);
    }

    public Float3 getLocalMax() {
        return new Float3(localMax);
    }

    private void calculLocalCenter() {
        this.localCenter.set((localMin.getX() + localMax.getX()) / 2, (localMin.getY() + localMax.getY()) / 2, (localMin.getZ() + localMax.getZ()) / 2);
    }

    private void calculSize() {
        this.size.set((max.getX() - min.getX()), (max.getY() - min.getY()), (max.getZ() - min.getZ()));
    }

    private void update() {
        if (needUpdate) {
            calculMinMax();
            calculSize();
            calculCenter();
            needUpdate = false;
        }
    }

    private void calculMinMax() {
        float[] nPointsExterieurs = new float[pointsExterieurs.length];

        Matrix4f localToWorldMatrix;

        if (transform != null) {
            localToWorldMatrix = transform.getLocalToWorldMatrix();
        } else {
            localToWorldMatrix = new Matrix4f();
        }

        Matrix.multiplyMM(nPointsExterieurs, 0, localToWorldMatrix.getArray(), 0, pointsExterieurs, 0);
        Matrix.multiplyMM(nPointsExterieurs, 16, localToWorldMatrix.getArray(), 0, pointsExterieurs, 16);

        float[] min = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
        float[] max = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                min[j] = Math.min(min[j], nPointsExterieurs[i * 4 + j]);
                max[j] = Math.max(max[j], nPointsExterieurs[i * 4 + j]);
            }
        }

        this.min.set(min[0], min[1], min[2]);
        this.max.set(max[0], max[1], max[2]);
    }

    private void calculCenter() {
        if (transform != null) {
            Matrix.multiplyMV(center.getArray(), 0, transform.getLocalToWorldMatrix().getArray(), 0, localCenter.getArray(), 0);
        } else {
            center.set(localCenter);
        }
    }

    private void calculPointsExterieur() {

        pointsExterieurs = new float[]
                {
                        localMin.getX(), localMin.getY(), localMin.getZ(), 1,
                        localMax.getX(), localMin.getY(), localMin.getZ(), 1,
                        localMax.getX(), localMin.getY(), localMax.getZ(), 1,
                        localMin.getX(), localMin.getY(), localMax.getZ(), 1,
                        localMin.getX(), localMax.getY(), localMin.getZ(), 1,
                        localMax.getX(), localMax.getY(), localMin.getZ(), 1,
                        localMax.getX(), localMax.getY(), localMax.getZ(), 1,
                        localMin.getX(), localMax.getY(), localMax.getZ(), 1
                };


    }

    public Transform getTransform() {
        return transform;
    }

    public void setParent(Transform transform) {
        if (null != transform) {
            transform.deleteObserver(this);
        }
        this.transform = transform;
        if (null != transform) {
            transform.addObserver(this);
        }
        setChanged();
        notifyObservers();

        needUpdate = true;
    }

    public Float3 getSize() {
        update();
        return new Float3(size);
    }

    public Float3 getCenter() {
        update();
        return new Float3(center);
    }

    public Float3 getMin() {
        update();
        return new Float3(min);
    }

    public Float3 getMax() {
        update();
        return new Float3(max);
    }

    public Float3 getLocalCenter() {
        return new Float3(localCenter);
    }

    @Override
    public void update(Observable o, Object arg) {
        needUpdate = true;
        setChanged();
        notifyObservers();
    }
}
