package com.example.kbourgeois.opengl;

import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.kbourgeois.opengl.FloatK.Float3;

class MyGLSurfaceView extends GLSurfaceView implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnDoubleTapListener {
    private final MyGLRenderer myRenderer;
    private final float TOUCH_SCALE_FACTOR = 0.01f;

    private float mScale = 1;
    private DetectorMove mDetectorMoveDoubleTap = new DetectorMove();


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
        mGestureDetectorCompat.setOnDoubleTapListener(this);
        mGestureDetectorCompat.setIsLongpressEnabled(false);
        mScaleGestureDetector = new ScaleGestureDetector(mainActivity, this);
        mScaleGestureDetector.setQuickScaleEnabled(false);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), e.getX() + " " + e.getY());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), "dX : " + distanceX + " dY " + distanceY);

        if (distanceX != 0 || distanceY != 0) {
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                distanceY = 0;
                distanceX *= -1;
            } else {
                distanceX = 0;
                distanceY *= -1;
            }
            Transform transform = myRenderer.getTransform();
            switch (e2.getPointerCount()) {
                case 1:
                    Float3 rotation = transform.getLocalRotation();
                    rotation.set(rotation.getX() + Math.signum(distanceY), rotation.getY() + Math.signum(distanceX), rotation.getZ());
                    transform.setLocalRotation(rotation);
                    Log.d("Rotation", rotation.getX() + " " + rotation.getY() + " " + rotation.getZ());
                    return true;
            }

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), "vX : " + velocityX + " vY " + velocityY);
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        float scale = detector.getScaleFactor();
        if (scale != 1) {
            mScale *= scale;
            Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), Float.toString(mScale));
            Transform transform = myRenderer.getTransform();
            Float3 localScale = transform.getScale();
            localScale.set(mScale, mScale, mScale);
            transform.setScale(localScale);
            return true;
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d("Event", Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        mDetectorMoveDoubleTap.onTouchEvent(e);
        if (mDetectorMoveDoubleTap.hasMouvement()) {
            float distanceX = mDetectorMoveDoubleTap.getP2().getX() - mDetectorMoveDoubleTap.getP1().getX();
            float distanceY = mDetectorMoveDoubleTap.getP1().getY() - mDetectorMoveDoubleTap.getP2().getY();
            Log.d(Thread.currentThread().getStackTrace()[2].getMethodName(), "dX : " + distanceX + " dY " + distanceY);
            Transform transform = myRenderer.getTransform();
            Float3 translate = transform.getPosition();
            translate.set(translate.getX() + distanceX * TOUCH_SCALE_FACTOR, translate.getY() + distanceY * TOUCH_SCALE_FACTOR, translate.getZ());
            transform.setPosition(translate);
            Log.d("Translate", translate.getX() + " " + translate.getY() + " " + translate.getZ());
            return true;
        }
        return false;
    }
}
