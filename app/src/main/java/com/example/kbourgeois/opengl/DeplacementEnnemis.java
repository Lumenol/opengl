package com.example.kbourgeois.opengl;

import com.example.kbourgeois.opengl.FloatK.Float3;

import java.util.Random;

public class DeplacementEnnemis extends MonoBehaviour {
    public DeplacementEnnemis(GameObject gameObject) {
        super(gameObject);
    }

    private float vitesse = 10;

    @Override
    public void start() {
    }

    private void reset() {
        Random random = new Random();
        float x = (2*random.nextFloat()-1) * 5;
        float y = (2*random.nextFloat()-1) * 5;
        float z = 50 + (2*random.nextFloat()-1) * 50;

        getGameObject().getTransform().setPosition(new Float3(x, y, -(50f + z)));
    }

    @Override
    public void update(float dt) {
        Transform modelTransform = getGameObject().getTransform();
        Float3 position = modelTransform.getPosition();

        Object componant = getGameObject().getComponant(Drawable.class);
        if (componant instanceof Drawable) {
            Drawable drawable = (Drawable) componant;
            if (drawable.getBounds().getMin().getZ() >= 10) {
                reset();
            } else {
                position.setZ(position.getZ() + vitesse * dt);
                modelTransform.setPosition(position);
            }
        }
    }
}
