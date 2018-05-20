package com.example.kbourgeois.opengl.Bounds;

import android.opengl.Matrix;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.FloatK.Float4;
import com.example.kbourgeois.opengl.GameObect.Transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class BoundsCompose extends Observable implements Bounds, Observer {

    private Float4 localMin = new Float4(0, 0, 0, 1);
    private Float4 localMax = new Float4(0, 0, 0, 1);
    private Float3 min = new Float3();
    private Float3 max = new Float3();
    private Float3 size = new Float3();
    private Float4 center = new Float4(0, 0, 0, 1);
    private Float3 localCenter = new Float4(0, 0, 0, 1);
    private List<Bounds> bounds = new ArrayList<>();

    private boolean needUpdate = true;

    private Transform transform;

    public BoundsCompose(Collection<? extends Bounds> bounds) {
        this.bounds.addAll(bounds);

        localMin.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        localMax.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (Iterator<? extends Bounds> iterator = bounds.iterator(); iterator.hasNext(); ) {
            Bounds next = iterator.next();

            next.addObserver(this);

            localMin.set(Math.min(localMin.getX(), next.getLocalMin().getX()), Math.min(localMin.getY(), next.getLocalMin().getY()), Math.min(localMin.getZ(), next.getLocalMin().getZ()));
            localMax.set(Math.max(localMax.getX(), next.getLocalMax().getX()), Math.max(localMax.getY(), next.getLocalMax().getY()), Math.max(localMax.getZ(), next.getLocalMax().getZ()));
        }

        calculLocalCenter();
        update();
    }

    private void calculLocalCenter() {
        this.localCenter.set((localMin.getX() + localMax.getX()) / 2, (localMin.getY() + localMax.getY()) / 2, (localMin.getZ() + localMax.getZ()) / 2);
    }

    private void calculMinMax() {
        min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (Iterator<? extends Bounds> iterator = bounds.iterator(); iterator.hasNext(); ) {
            Bounds next = iterator.next();

            min.set(Math.min(min.getX(), next.getMin().getX()), Math.min(min.getY(), next.getMin().getY()), Math.min(min.getZ(), next.getMin().getZ()));
            max.set(Math.max(max.getX(), next.getMax().getX()), Math.max(max.getY(), next.getMax().getY()), Math.max(max.getZ(), next.getMax().getZ()));
        }
    }

    private void update() {
        if (needUpdate) {
            calculMinMax();
            calculSize();
            calculCenter();
            needUpdate = false;
        }
    }

    private void calculCenter() {
        if (transform != null) {
            Matrix.multiplyMV(center.getArray(), 0, transform.getLocalToWorldMatrix().getArray(), 0, localCenter.getArray(), 0);
        } else {
            center.set(localCenter);
        }
    }


    private void calculSize() {
        this.size.set((max.getX() - min.getX()), (max.getY() - min.getY()), (max.getZ() - min.getZ()));
    }

    public Float3 getLocalMin() {
        return new Float3(localMin);
    }

    public Float3 getLocalMax() {
        return new Float3(localMax);
    }


    @Override
    public Float3 getSize() {
        update();
        return new Float3(size);
    }

    @Override
    public void setParent(Transform transform) {
        this.transform = transform;
        for (Iterator<Bounds> iterator = bounds.iterator(); iterator.hasNext(); ) {
            Bounds next = iterator.next();
            next.setParent(transform);
        }
    }

    @Override
    public Float3 getCenter() {
        update();
        return new Float3(center);
    }

    @Override
    public Float3 getMin() {
        update();
        return new Float3(min);
    }

    @Override
    public Float3 getMax() {
        update();
        return new Float3(max);
    }

    @Override
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
