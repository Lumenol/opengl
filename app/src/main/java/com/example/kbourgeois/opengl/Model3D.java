package com.example.kbourgeois.opengl;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Model3D {

    private int mVertexID;
    private int mTexID;
    private int mNormalID;

    private int mProgram;

    private float mVertices[];
    private float mNormals[];
    private float mUV[];
    private int mIndices[];


    private FloatBuffer mVertexBuffer;
    private IntBuffer mIndexBuffer;
    private FloatBuffer mTexBuffer;
    private FloatBuffer mNormalBuffer;


    private static final int COORDS_PER_VERTEX = 3;
    private static final int COORDS_PER_NORMAL = 3;
    private static final int COORDS_PER_TEX = 2;

    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int TEX_STRIDE = COORDS_PER_TEX * 4;
    private final int NORMAL_STRIDE = COORDS_PER_NORMAL * 4;
    private int mVertexShaderID;
    private int mFragShaderID;
    private int mIdViewMatrix;
    private int mIdProjMatrix;
    private int mIdModelMatrix;
    private Texture mTexture;

    private Transform transform = new Transform();
    private Bounds bounds;

    private String name;

    public String getName() {
        return name;
    }

    Model3D(float[] vertices, float[] normals, float[] uvs, int[] indices) {
        this(vertices, normals, uvs, indices, "");
    }

    public Bounds getBounds() {
        return bounds;
    }

    Model3D(float[] vertices, float[] normals, float[] uvs, int[] indices, String name) {

        this.name = name;

        bounds = new Bounds(vertices, COORDS_PER_VERTEX, 0);
        transform.setOffset(bounds.getLocalCenter());

        mVertices = vertices;
        mNormals = normals;
        mUV = uvs;
        mIndices = indices;

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(mVertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mNormals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf.asFloatBuffer();
        mNormalBuffer.put(mNormals);
        mNormalBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mUV.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTexBuffer = byteBuf.asFloatBuffer();
        mTexBuffer.put(mUV);
        mTexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(mIndices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuf.asIntBuffer();
        mIndexBuffer.put(mIndices);
        mIndexBuffer.position(0);

    }

    public Texture getmTexture() {
        return mTexture;
    }

    public void setmTexture(Texture mTexture) {
        this.mTexture = mTexture;
    }

    void init(String vertexShader, String fragmentShader, String vertexLoc,
              String normalLoc, String texLoc, Texture texture) {

        mTexture = texture;
        texture.charger();

        mProgram = GLES30.glCreateProgram();

        mVertexShaderID = ShaderUtilities.loadShader(GLES30.GL_VERTEX_SHADER, vertexShader);
        mFragShaderID = ShaderUtilities.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader);
        GLES30.glAttachShader(mProgram, mVertexShaderID);
        GLES30.glAttachShader(mProgram, mFragShaderID);
        GLES30.glLinkProgram(mProgram);
        int[] linkStatus = {0};
        GLES30.glGetProgramiv(mProgram, GLES30.GL_LINK_STATUS, linkStatus, 0);

        mVertexID = GLES30.glGetAttribLocation(mProgram, vertexLoc);
        mNormalID = GLES30.glGetAttribLocation(mProgram, normalLoc);
        mTexID = GLES30.glGetAttribLocation(mProgram, texLoc);

        mIdProjMatrix = GLES30.glGetUniformLocation(mProgram, "projection");
        mIdViewMatrix = GLES30.glGetUniformLocation(mProgram, "view");
        mIdModelMatrix = GLES30.glGetUniformLocation(mProgram, "model");

        Log.d("MODEL 3D", "init: mVertexID :" + mVertexID);
    }

    public Transform getTransform() {
        return transform;
    }

    void draw(float[] projection, float[] view) {
        GLES30.glUseProgram(mProgram);

        GLES30.glEnableVertexAttribArray(mVertexID);
        GLES30.glVertexAttribPointer(
                mVertexID, 3, GLES30.GL_FLOAT, false, VERTEX_STRIDE, mVertexBuffer);

        GLES30.glEnableVertexAttribArray(mTexID);
        GLES30.glVertexAttribPointer(
                mTexID, 2, GLES30.GL_FLOAT, false, TEX_STRIDE, mTexBuffer);

        GLES30.glEnableVertexAttribArray(mNormalID);
        GLES30.glVertexAttribPointer(
                mNormalID, 2, GLES30.GL_FLOAT, false, NORMAL_STRIDE, mNormalBuffer);

        // Apply the projection and view transformation.
        GLES30.glUniformMatrix4fv(mIdViewMatrix, 1, false, view, 0);
        GLES30.glUniformMatrix4fv(mIdModelMatrix, 1, false, transform.getLocalToWorldMatrix().getArray(), 0);
        GLES30.glUniformMatrix4fv(mIdProjMatrix, 1, false, projection, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexture.getID());

        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, mIndices.length, GLES30.GL_UNSIGNED_INT, mIndexBuffer);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);


        GLES30.glDisableVertexAttribArray(mVertexID);
        GLES30.glDisableVertexAttribArray(mTexID);
        GLES30.glDisableVertexAttribArray(mNormalID);

        GLES30.glUseProgram(0);

    }

    void clean() {
        GLES30.glDetachShader(mProgram, mVertexShaderID);
        GLES30.glDetachShader(mProgram, mFragShaderID);
        GLES30.glDeleteProgram(mProgram);
    }

    public int[] getmIndices() {
        return mIndices;
    }

    public float[] getmNormals() {
        return mNormals;
    }

    public float[] getmUV() {
        return mUV;
    }

    public float[] getmVertices() {
        return mVertices;
    }

    @Override
    protected void finalize() throws Throwable {
        clean();
        super.finalize();
    }
}
