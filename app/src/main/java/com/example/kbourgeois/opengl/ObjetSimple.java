package com.example.kbourgeois.opengl;

import android.renderscript.Matrix4f;

public class ObjetSimple extends GameObject implements Drawable {

    private BoundsSimple bounds;
    private Model3D model3D;

    public ObjetSimple(ObjetSimple simple) {
        this(simple.model3D);
    }

    @Override
    public Drawable clone() {
        return new ObjetSimple(this);
    }

    public ObjetSimple(Model3D model3D) {
        this.model3D = model3D;
        Bounds model3DBounds = model3D.getBounds();
        this.bounds = new BoundsSimple(model3DBounds.getLocalMin(), model3DBounds.getLocalMax());
        this.bounds.setParent(getTransform());
        getTransform().setOffset(this.bounds.getLocalCenter());
        addComponant(Drawable.class, this);
    }

    @Override
    public BoundsSimple getBounds() {
        return bounds;
    }

    @Override
    public String getName() {
        return model3D.getName();
    }

    @Override
    public void draw(Matrix4f projection, Matrix4f view) {
        model3D.draw(projection, view, getTransform().getLocalToWorldMatrix());
    }
}
