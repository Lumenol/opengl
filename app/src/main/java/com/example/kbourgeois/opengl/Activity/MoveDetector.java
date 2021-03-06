package com.example.kbourgeois.opengl.Activity;

import android.view.MotionEvent;

import com.example.kbourgeois.opengl.FloatK.Float2;

public class MoveDetector {

    private Float2 p1, p2;
    private boolean hasMouvement;

    public MoveDetector() {
        p1 = new Float2(0, 0);
        p2 = new Float2(0, 0);
    }

    public boolean hasMouvement() {
        return hasMouvement;
    }

    public Float2 getP1() {
        return p1;
    }

    public Float2 getP2() {
        return p2;
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                p1.set(event.getX(), event.getY());
            case MotionEvent.ACTION_UP:
            default:
                hasMouvement = false;
                break;


            case MotionEvent.ACTION_MOVE:
                if (hasMouvement) {
                    p1 = new Float2(p2);
                }
                p2.set(event.getX(), event.getY());
                hasMouvement = true;
                break;
        }
    }

}
