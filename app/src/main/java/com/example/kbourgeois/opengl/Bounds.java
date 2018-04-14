package com.example.kbourgeois.opengl;

import android.opengl.Matrix;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;

import java.util.Observable;
import java.util.Observer;

public class Bounds implements Observer {

    private void calculCenter() {
        this.center.set((minLocal.getX() + maxLocal.getX()) / 2, (minLocal.getY() + maxLocal.getY()) / 2, (minLocal.getZ() + maxLocal.getZ()) / 2);
    }

    private void majSize() {
        this.size.set((max.getX() - min.getX()), (max.getY() - min.getY()), (max.getZ() - min.getZ()));
    }

    private void majMinMax() {
        float[] nPointsExterieurs = new float[pointsExterieurs.length];

        Matrix.multiplyMM(nPointsExterieurs, 0, transform.getMatrix().getArray(), 0, pointsExterieurs, 0);
        Matrix.multiplyMM(nPointsExterieurs, 16, transform.getMatrix().getArray(), 0, pointsExterieurs, 16);

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

    private void majPosition() {
        Matrix.multiplyMV(position.getArray(), 0, transform.getMatrix().getArray(), 0, center.getArray(), 0);
        position.commit();
    }

    private Float4 minLocal = new Float4(0, 0, 0, 1);
    private Float4 maxLocal = new Float4(0, 0, 0, 1);

    private float[] pointsExterieurs;

    private Float3 min = new Float3();
    private Float3 max = new Float3();

    private Float3 size = new Float3();
    private Float4 position = new Float4(0, 0, 0, 1);
    private Float3 center = new Float4(0, 0, 0, 1);

    private Transform transform;

    private void calculPointsExterieur() {

        pointsExterieurs = new float[]
                {
                        minLocal.getX(), minLocal.getY(), minLocal.getZ(), 1,
                        maxLocal.getX(), minLocal.getY(), minLocal.getZ(), 1,
                        maxLocal.getX(), minLocal.getY(), maxLocal.getZ(), 1,
                        minLocal.getX(), minLocal.getY(), maxLocal.getZ(), 1,
                        minLocal.getX(), maxLocal.getY(), minLocal.getZ(), 1,
                        maxLocal.getX(), maxLocal.getY(), minLocal.getZ(), 1,
                        maxLocal.getX(), maxLocal.getY(), maxLocal.getZ(), 1,
                        minLocal.getX(), maxLocal.getY(), maxLocal.getZ(), 1
                };


    }

    public Bounds(Transform transform, final Float3 minLocal, final Float3 maxLocal) {
        this.transform = transform;
        this.minLocal.set(minLocal.getX(), minLocal.getY(), minLocal.getZ());
        this.maxLocal.set(maxLocal.getX(), maxLocal.getY(), maxLocal.getZ());
        calculPointsExterieur();
        calculCenter();
        majMinMax();
        majSize();
        majPosition();
        transform.addObserver(this);
    }

    public Bounds(Transform transform, float vertices[], int coords_per_vertex, int shift) {
        this.transform = transform;
        minLocal.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        maxLocal.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (int i = 0; i < vertices.length; i += 3 + shift) {
            for (int j = 0; j < coords_per_vertex; j++) {
                minLocal.set(Math.min(minLocal.getX(), vertices[i]), Math.min(minLocal.getY(), vertices[i + 1]), Math.min(minLocal.getZ(), vertices[i + 2]));
                maxLocal.set(Math.max(maxLocal.getX(), vertices[i]), Math.max(maxLocal.getY(), vertices[i + 1]), Math.max(maxLocal.getZ(), vertices[i + 2]));
            }
        }
        calculPointsExterieur();
        calculCenter();
        majMinMax();
        majSize();
        majPosition();
        transform.addObserver(this);
    }

    public Float3 getSize() {
        return size;
    }

    public Float3 getPosition() {
        return position;
    }

    public Float3 getMin() {
        return min;

    }

    public Float3 getMax() {
        return max;
    }

    public Float3 getCenter() {
        return center;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == transform) {
            majMinMax();
            majSize();
            majPosition();
        }
    }
}
