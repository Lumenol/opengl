package com.example.kbourgeois.opengl.GameObect;

import com.example.kbourgeois.opengl.Bounds.Bounds;
import com.example.kbourgeois.opengl.Bounds.BoundsSimple;
import com.example.kbourgeois.opengl.Camera.Camera;

public class ObjetSimple extends GameObject implements Drawable {

    private BoundsSimple bounds;
    private Model3D model3D;

    public ObjetSimple(ObjetSimple simple) {
        this(simple.model3D);
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
    public Drawable clone() {
        return new ObjetSimple(this);
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
    public void draw(Camera camera) {
        model3D.draw(camera.getProjection(), camera.getView(), getTransform().getLocalToWorldMatrix(), camera.getTransform().getPosition());
    }
}
