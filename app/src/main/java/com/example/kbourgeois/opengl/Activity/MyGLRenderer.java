package com.example.kbourgeois.opengl.Activity;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.kbourgeois.opengl.Behaviour.DeplacementEnnemis;
import com.example.kbourgeois.opengl.Behaviour.RotationSkyBox;
import com.example.kbourgeois.opengl.Behaviour.RotationTardis;
import com.example.kbourgeois.opengl.Camera.Camera;
import com.example.kbourgeois.opengl.Camera.Perspective;
import com.example.kbourgeois.opengl.FloatK.Float3;
import com.example.kbourgeois.opengl.GameObect.Drawable;
import com.example.kbourgeois.opengl.GameObect.GameObject;
import com.example.kbourgeois.opengl.GameObect.ObjetCompose;
import com.example.kbourgeois.opengl.GameObect.Shader;
import com.example.kbourgeois.opengl.GameObect.Transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    private long lastFrame = System.currentTimeMillis();

    private Camera camera;

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


        camera = new Perspective(70.0f, 9.0f / 16.0f, 0.1f, 100.0f);
        //camera = new Orthographic(-10,10,-10,10,0.1f,100f);
        camera.set(new Float3(0, 0, 10), new Float3(0, 0, 0));
        Log.d("Camera", camera.getTransform().getRotation().toString());

        Shader shader = new Shader(mContext, "vertexshader.vert", "fragmentshader.frag");
        model = new ObjetCompose(mContext, "Tardis/tardis.obj", shader);

        {
            float scale = 0.5f;
            model.getTransform().setLocalScale(new Float3(scale, scale, scale));
            model.getTransform().setPosition(new Float3(0, 0, -5));
            gameObjects.add(model);
            model.getTransform().setParent(transform);
            model.addComponant(RotationTardis.class);
        }

        ObjetCompose skybox = new ObjetCompose(mContext, "Skybox/cube.obj", shader);
        {
            float skyScale = 100.f;
            Transform transform = skybox.getTransform();
            transform.setLocalScale(new Float3(skyScale, skyScale, skyScale));
            transform.setLocalRotation(new Float3(0, 90, 0));
            ((RotationSkyBox) skybox.addComponant(RotationSkyBox.class)).vitesse = 250f / 60;
        }
        //skybox.getTransform().setParent(getTransform()); // Pour pouvoir faire tourner la boite et regarder partout
        gameObjects.add(skybox);

        ObjetCompose ennemi = new ObjetCompose(mContext, "Rock/rock.obj", shader);
        for (int i = 0; i < 50; i++) {
            ObjetCompose clone = (ObjetCompose) ennemi.clone();
            clone.addComponant(DeplacementEnnemis.class);
            gameObjects.add(clone);
            float scale = 0.15f;
            clone.getTransform().setLocalScale(new Float3(scale, scale, scale));
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES30.glViewport(0, 0, width, height);
        //projectionMatrix.loadPerspective(70.0f, (float) width / height, 0.1f, 100.0f);
        if (camera instanceof Perspective) {
            Perspective p = (Perspective) camera;
            p.setAspect((float) width / height);
        }
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
                d.draw(camera);
            }
        }
    }

}