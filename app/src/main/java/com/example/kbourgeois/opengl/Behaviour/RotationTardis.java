package com.example.kbourgeois.opengl.Behaviour;

import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.GameObect.GameObject;
import com.example.kbourgeois.opengl.GameObect.MonoBehaviour;
import com.example.kbourgeois.opengl.GameObect.Transform;

public class RotationTardis extends MonoBehaviour {
    private float angleBalancement = 0;

    public RotationTardis(GameObject gameObject) {
        super(gameObject);
    }

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
