package com.example.kbourgeois.opengl;

import com.example.kbourgeois.opengl.FloatK.Float3;

public class DeplacementEnnemis extends MonoBehaviour {
    public DeplacementEnnemis(GameObject gameObject) {
        super(gameObject);
    }

    private float vitesse = 10;

    @Override
    public void start() {
    }

    private void reset() {
        getGameObject().getTransform().setPosition(new Float3(0f, 0f, -50f));
    }

    @Override
    public void update(float dt) {
        Transform modelTransform = getGameObject().getTransform();
        Float3 position = modelTransform.getPosition();
        if (position.getZ() >= 50) {
            reset();
        } else {
            position.setZ(position.getZ() + vitesse * dt);
            modelTransform.setPosition(position);
        }
    }
}
