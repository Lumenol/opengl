package com.example.kbourgeois.opengl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Context mContext;

    private Transform transform = new Transform();

    private Map<String, Model3D> model3DS;

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

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 10, 0, 0, 0, 0f, 1.0f, 0.0f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, 9.0f / 16.0f, 0.1f, 100.0f);

        model3DS = ModelLoader.readOBJFile(mContext, "TARDIS/TARDIS.obj");

        //int[] textures = new int[]{R.drawable.tardis_inside_d, R.drawable.tardis_bulb_d, R.drawable.tardis_glass2_d, R.drawable.tardis_d, R.drawable.tardis_glass1_d};

        Model3D tardis = model3DS.get("TARDIS");

        Collection<Model3D> values = model3DS.values();
        for (Iterator<Model3D> iterator = values.iterator(); iterator.hasNext(); ) {
            Model3D next = iterator.next();

            next.getTransform().setParent(transform);
            next.init("vertexshader.vert", "fragmentshader.frag",
                    "vPosition", "vNormal", "vTexCoord");

            next.getTransform().setOffset(tardis.getBounds().getLocalCenter());
        }

        //mCube = new Cube();
        //mCube.addLight(new Light(0, 2, 0));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Debug", "onSurfaceChanged");
        GLES30.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mProjectionMatrix, 0, 70.0f, (float) width / (float) height, 0.1f, 100.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("Debug", "onDrawFrame");
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        for (Iterator<Model3D> iterator = model3DS.values().iterator(); iterator.hasNext(); ) {
            Model3D next = iterator.next();
            next.draw(mProjectionMatrix, mViewMatrix);
        }

    }

}