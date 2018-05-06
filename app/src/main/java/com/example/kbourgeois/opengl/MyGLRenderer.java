package com.example.kbourgeois.opengl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.example.kbourgeois.opengl.FloatK.Float3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    private long lastFrame = System.currentTimeMillis();

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    private Context mContext;

    private Transform transform = new Transform();

    private Collection<GameObject> gameObjects = new ArrayList<>();

    private ObjetCompose model;

    public MyGLRenderer(Context context) {
        super();
        mContext = context;
        Log.d("Debug : ", "MyGLRenderer");
    }


    public Transform getTransform() {
        return transform;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        Log.d("Debug : ", "onSurfaceCreated");
        GLES30.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
        GLES30.glClearDepthf(1.0f);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glDepthFunc(GLES30.GL_LEQUAL);

        Matrix.setLookAtM(viewMatrix.getArray(), 0, 0, 0, 10, 0, 0, 0, 0f, 1.0f, 0.0f);
        projectionMatrix.loadPerspective(70.0f, 9.0f / 16.0f, 0.1f, 100.0f);

        Shader shader = new Shader(mContext, "vertexshader.vert", "fragmentshader.frag");
        model = new ObjetCompose(mContext, "TARDIS/TARDIS.obj", shader);
        gameObjects.add(model);
        model.getTransform().setParent(transform);
        model.addComponant(RotationTardis.class);

        ObjetCompose skybox = new ObjetCompose(mContext, "Skybox/cube.obj", shader);
        float skyScale = 100.f;
        skybox.getTransform().setLocalScale(new Float3(skyScale, skyScale, skyScale));
        //skybox.getTransform().setParent(getTransform());
        gameObjects.add(skybox);


        for (int i = 0; i < 1; i++) {
            ObjetCompose ennemi = new ObjetCompose(mContext, "Boat/OldBoat.obj", shader);
            ennemi.addComponant(DeplacementEnnemis.class);
            gameObjects.add(ennemi);
            float scale = 0.15f;
            ennemi.getTransform().setLocalScale(new Float3(scale, scale, scale));
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES30.glViewport(0, 0, width, height);
        projectionMatrix.loadPerspective(70.0f, (float) width / height, 0.1f, 100.0f);
    }

    private void update(float dt) {
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject next = iterator.next();
            next.update(dt);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        update((float) ((System.currentTimeMillis() - lastFrame) / 1000.0));
        lastFrame = System.currentTimeMillis();
        Log.d("Debug", "onDrawFrame");
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject next = iterator.next();
            if (next instanceof Drawable) {
                Drawable d = (Drawable) next;
                d.draw(projectionMatrix, viewMatrix);
            }
        }
    }

}