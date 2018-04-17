package com.example.kbourgeois.opengl;

import android.content.Context;
import android.renderscript.Matrix4f;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjetCompose implements Drawable {

    private Transform transform = new Transform();
    private Bounds bounds;

    private List<Drawable> drawables = new ArrayList<>();


    private String name = "";

    @Override
    public String getName() {
        return name;
    }

    public ObjetCompose(Context context, String filename, Shader shader) {
        File file = new File(filename);
        name = file.getName();
        Map<String, ObjetSimple> drawables = ModelLoader.readOBJFile(context, filename, shader);
        init(drawables.values());
    }

    private void init(Collection<? extends Drawable> drawables) {
        this.drawables.addAll(drawables);
        List<Bounds> bounds = new ArrayList<>();
        for (Iterator<? extends Drawable> iterator = drawables.iterator(); iterator.hasNext(); ) {
            Drawable next = iterator.next();

            bounds.add(next.getBounds());
            next.getTransform().setParent(transform);
        }

        this.bounds = new BoundsCompose(bounds);
        this.bounds.setParent(transform);
        transform.setOffset(this.bounds.getLocalCenter());

        for (Iterator<? extends Drawable> iterator = drawables.iterator(); iterator.hasNext(); ) {
            Drawable next = iterator.next();

            next.getTransform().setOffset(this.bounds.getLocalCenter());
        }

    }

    public ObjetCompose(String name, Collection<? extends Drawable> drawables) {
        this.name = name;
        init(drawables);
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void draw(Matrix4f projection, Matrix4f view) {
        for (Iterator<Drawable> drawableIterator = drawables.iterator(); drawableIterator.hasNext(); ) {
            Drawable next = drawableIterator.next();
            next.draw(projection, view);
        }
    }
}
