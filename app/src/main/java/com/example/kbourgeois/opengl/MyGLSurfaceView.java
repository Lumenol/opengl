package com.example.kbourgeois.opengl;

import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

class MyGLSurfaceView extends GLSurfaceView implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private final MyGLRenderer myRenderer;
    private final float TOUCH_SCALE_FACTOR = 0.01f;

    private float mScale = 1;

    private GestureDetectorCompat mGestureDetectorCompat;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean r = mScaleGestureDetector.onTouchEvent(event);
        r = mGestureDetectorCompat.onTouchEvent(event) || r;
        if (r) {
            requestRender();
        }
        return r;
    }

    public MyGLSurfaceView(OpenGLActivity mainActivity) {
        super(mainActivity);

        Log.d("Debug : ", "MyGLSurfaceView");
        setEGLContextClientVersion(2);
        Log.d("Debug : ", "Setting OpenGLES version");

        myRenderer = new MyGLRenderer(mainActivity);

        Log.d("Debug : ", "Initialize renderer");
        setRenderer(myRenderer);
        Log.d("Debug : ", "Set renderer");
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        Log.d("Debug : ", "Set render mode");
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mGestureDetectorCompat = new GestureDetectorCompat(mainActivity, this);
        mScaleGestureDetector = new ScaleGestureDetector(mainActivity, this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), "dX : " + distanceX + " dY " + distanceY);

        if (distanceX != 0 || distanceY != 0) {
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                distanceY = 0;
            } else {
                distanceX = 0;
            }
            Transform transform = myRenderer.getmModel().getTransform();
            switch (e2.getPointerCount()) {
                case 1:
                    transform.setRotate(transform.getRotateX()+Math.signum(-distanceY), transform.getRotateY()+Math.signum(-distanceX) , transform.getRotateZ());
                    Log.d("Rotation", transform.getRotateX() + " " + transform.getRotateY() + " " + transform.getRotateZ());
                    return true;
                case 2:
                    transform.setTranslate(transform.getTranslateX() + distanceX * TOUCH_SCALE_FACTOR, transform.getTranslateY() + distanceY * TOUCH_SCALE_FACTOR, transform.getTranslateZ());
                    return true;
            }

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        myRenderer.getmModel().getTransform().setTranslate(0, 0, 0);
        myRenderer.getmModel().getTransform().setRotate(0, 0, 0);
        myRenderer.getmModel().getTransform().setScale(1, 1, 1);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), "vX : " + velocityX + " vY " + velocityY);
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        //float scale = detector.getCurrentSpan() / detector.getPreviousSpan();
        float scale = mScaleGestureDetector.getScaleFactor();
        if (scale != 1) {
            mScale *= scale;
            Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), Float.toString(mScale));
            myRenderer.getmModel().getTransform().setScale(mScale, mScale, mScale);
            return true;
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d("Event :", Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
