package com.example.kbourgeois.opengl;

import com.example.kbourgeois.opengl.FloatK.Float3;

public class RotationSkyBox extends MonoBehaviour {

    public float vitesse=1800f/60;

    public RotationSkyBox(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        Transform transform = getGameObject().getTransform();
        Float3 localRotation = transform.getLocalRotation();
        localRotation.setX(localRotation.getX() - dt * vitesse);
        transform.setLocalRotation(localRotation);
    }
}
