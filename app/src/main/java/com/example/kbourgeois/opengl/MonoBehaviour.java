package com.example.kbourgeois.opengl;

public abstract class MonoBehaviour {

    private GameObject gameObject;

    final public GameObject getGameObject() {
        return gameObject;
    }

    public MonoBehaviour(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public abstract void start();

    public abstract void update(float dt);

}
