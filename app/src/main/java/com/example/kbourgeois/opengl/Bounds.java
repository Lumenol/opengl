package com.example.kbourgeois.opengl;

import android.opengl.Matrix;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

public class Bounds {

    private float min[];
    private float max[];
    private float center[];

    public Bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.min = new float[]{minX, minY, minZ};
        this.max = new float[]{maxX, maxY, maxZ};
        this.center = new float[]{(getMinX() + getMaxX()) / 2, (getMinY() + getMaxY()) / 2, (getMinZ() + getMaxZ()) / 2};
    }

    public float getMinX() {
        return min[0];
    }

    public float getMinY() {
        return min[1];
    }

    public float getMinZ() {
        return min[2];
    }

    public float getMaxX() {
        return max[0];
    }

    public float getMaxY() {
        return max[1];
    }

    public float getMaxZ() {
        return max[2];
    }

    public float getCenterX() {
        return center[0];
    }

    public float getCenterY() {
        return center[1];
    }

    public float getCenterZ() {
        return center[2];
    }

    public Bounds(float vertices[], int coords_per_vertex, int shift) {
        min = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
        max = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};

        for (int i = 0; i < vertices.length; i += coords_per_vertex + shift) {
            for (int j = 0; j < coords_per_vertex; j++) {
                min[j] = Math.min(min[j], vertices[i + j]);
                max[j] = Math.max(max[j], vertices[i + j]);
            }
        }

        this.center = new float[]{(getMinX() + getMaxX()) / 2, (getMinY() + getMaxY()) / 2, (getMinZ() + getMaxZ()) / 2};
    }

    public Bounds transform(Transform transform) {
        float[] transformMatrix = transform.getMatrix();
        float[] transfomMin = new float[3], transfomMax = new float[3];
        Matrix.multiplyMV(transfomMin, 0, transformMatrix, 0, min, 0);
        Matrix.multiplyMV(transfomMin, 0, transformMatrix, 0, max, 0);
        return new Bounds(transfomMin[0], transfomMin[1], transfomMin[2], transfomMax[0], transfomMax[1], transfomMax[2]);
    }

}
