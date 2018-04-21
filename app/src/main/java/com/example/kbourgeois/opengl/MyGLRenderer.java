package com.example.kbourgeois.opengl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    private long lastFrame = System.currentTimeMillis();

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    private Context mContext;

    private Transform transform = new Transform();

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

        model = new ObjetCompose(mContext, "TARDIS/TARDIS.obj", new Shader(mContext, "vertexshader.vert", "fragmentshader.frag"));
        model.getTransform().setParent(transform);

        model.addComponant(RotationTardis.class);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES30.glViewport(0, 0, width, height);
        projectionMatrix.loadPerspective(70.0f, (float) width / height, 0.1f, 100.0f);
    }

    private void update(long dt) {
        model.update(dt);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        update(System.currentTimeMillis() - lastFrame);
        lastFrame = System.currentTimeMillis();
        Log.d("Debug", "onDrawFrame");
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        model.draw(projectionMatrix, viewMatrix);
    }

}