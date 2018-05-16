package com.example.kbourgeois.opengl;

import com.example.kbourgeois.opengl.FloatK.Float3;

public class RotationTardis extends MonoBehaviour {
    public RotationTardis(GameObject gameObject) {
        super(gameObject);
    }

    private float angleBalancement = 0;

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        angleBalancement = (float) ((angleBalancement + (dt * 90f / 60)) % (2 * Math.PI));

        Transform modelTransform = getGameObject().getTransform();
        Float3 localRotation = modelTransform.getLocalRotation();
        localRotation.set((float) (Math.sin(angleBalancement) * 10)
                , localRotation.getY() + dt * 18000f / 60
                , (float) (Math.cos(angleBalancement) * 10));
        modelTransform.setLocalRotation(localRotation);

    }
}
