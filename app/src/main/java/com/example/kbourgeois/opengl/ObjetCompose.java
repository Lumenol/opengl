package com.example.kbourgeois.opengl;

import android.content.Context;
import android.renderscript.Matrix4f;

import com.example.kbourgeois.opengl.Camera.Camera;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjetCompose extends GameObject implements Drawable, Cloneable {

    private Map<Object, Object> attributs = new HashMap<>();

    private Bounds bounds;

    private List<Drawable> drawables = new ArrayList<>();

    public ObjetCompose(ObjetCompose compose) {
        name = compose.name;
        for (Drawable drawable : compose.drawables) {
            drawables.add(drawable.clone());
        }
        init();
    }

    private String name = "";

    @Override
    public String getName() {
        return name;
    }

    public ObjetCompose(Context context, String filename, Shader shader) {
        File file = new File(filename);
        name = file.getName();
        Map<String, Model3D> drawables = ModelLoader.readOBJFile(context, filename, shader);
        for (Model3D model3D : drawables.values()) {
            this.drawables.add(new ObjetSimple(model3D));
        }
        init();
    }

    private void init() {
        List<Bounds> bounds = new ArrayList<>();
        for (Iterator<? extends Drawable> iterator = drawables.iterator(); iterator.hasNext(); ) {
            Drawable next = iterator.next();
            bounds.add(next.getBounds());
            next.getTransform().setParent(getTransform());
        }

        this.bounds = new BoundsCompose(bounds);
        this.bounds.setParent(getTransform());
        getTransform().setOffset(this.bounds.getLocalCenter());

        for (Iterator<? extends Drawable> iterator = drawables.iterator(); iterator.hasNext(); ) {
            Drawable next = iterator.next();

            next.getTransform().setOffset(this.bounds.getLocalCenter());
        }

        addComponant(Drawable.class, this);
    }

    public ObjetCompose(String name, Collection<? extends Drawable> drawables) {
        this.name = name;
        this.drawables.addAll(drawables);
        init();
    }


    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public void draw(Camera camera) {
        for (Iterator<Drawable> drawableIterator = drawables.iterator(); drawableIterator.hasNext(); ) {
            Drawable next = drawableIterator.next();
            next.draw(camera);
        }
    }

    @Override
    public Drawable clone() {
        return new ObjetCompose(this);
    }
}
