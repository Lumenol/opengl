package com.example.kbourgeois.opengl;

import android.opengl.GLES30;
import android.util.Log;

public class Light {

    private float position[];
    private float color[] = {1.0f, 1.0f, 1,0f};

    private int mProgram;

    private int mLightPosID;
    private int mLightColID;

    Light(float x, float y, float z){
        position = new float[3];
        position[0] = x;
        position[1] = y;
        position[2] = z;


    }

    Light (float x, float y, float z, float r, float g, float b){

        position = new float[3];
        position[0] = x;
        position[1] = y;
        position[2] = z;

        color[0] = r;
        color[1] = g;
        color[2] = b;

    }

    void rotate(){

    }

    void draw(int program){
        mLightPosID = GLES30.glGetUniformLocation(program, "lPosition");
        mLightColID = GLES30.glGetUniformLocation(program, "lColor");
        Log.d("Debug :", "draw: mLightPosID : " + mLightPosID);
        Log.d("Debug :", "draw: mLightColID : " + mLightColID);
        GLES30.glUniform3fv(mLightPosID, 1, position, 0);
        GLES30.glUniform3fv(mLightColID, 1, color, 0);
    }
}
