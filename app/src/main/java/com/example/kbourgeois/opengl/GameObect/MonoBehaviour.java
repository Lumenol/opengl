package com.example.kbourgeois.opengl.GameObect;

public abstract class MonoBehaviour {

    private GameObject gameObject;

    public MonoBehaviour(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    final public GameObject getGameObject() {
        return gameObject;
    }

    public abstract void start();

    public abstract void update(float dt);

}
