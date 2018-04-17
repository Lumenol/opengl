package com.example.kbourgeois.opengl;

import android.opengl.GLES30;
import android.renderscript.Matrix4f;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ObjetSimple implements Drawable {

    private static final int COORDS_PER_VERTEX = 3;
    private static final int COORDS_PER_NORMAL = 3;
    private static final int COORDS_PER_TEX = 2;

    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    private final int TEX_STRIDE = COORDS_PER_TEX * 4;
    private final int NORMAL_STRIDE = COORDS_PER_NORMAL * 4;

    private Shader shader = null;
    private Texture texture = new Texture();
    private Transform transform = new Transform();
    private BoundsSimple bounds;

    public boolean haveShader() {
        return shader != null;
    }

    String name = "";

    @Override
    public String getName() {
        return name;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    private float[] vertices;
    private float[] normals;
    private float[] uvs;
    private int[] indices;

    private FloatBuffer verticesBuffer;
    private FloatBuffer normalsBuffer;
    private FloatBuffer uvsBuffer;
    private IntBuffer indicesBuffer;

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public ObjetSimple(float[] vertices, float[] normals, float[] uvs, int[] indices, String name) {

        bounds = new BoundsSimple(vertices, COORDS_PER_VERTEX, 0);
        bounds.setParent(transform);
        transform.setOffset(bounds.getLocalCenter());

        this.name = name;
        this.vertices = vertices;
        this.normals = normals;
        this.uvs = uvs;
        this.indices = indices;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * Float.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        verticesBuffer = byteBuffer.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(normals.length * Float.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        normalsBuffer = byteBuffer.asFloatBuffer();
        normalsBuffer.put(normals);
        normalsBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(uvs.length * Float.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        uvsBuffer = byteBuffer.asFloatBuffer();
        uvsBuffer.put(uvs);
        uvsBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(indices.length * Float.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        indicesBuffer = byteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);

        bounds = new BoundsSimple(vertices, COORDS_PER_VERTEX, 0);

    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public BoundsSimple getBounds() {
        return bounds;
    }

    @Override
    public void draw(Matrix4f projection, Matrix4f view) {

        if (haveShader()) {

            GLES30.glUseProgram(shader.getProgramID());

            int vertexID = shader.getAttribLocation("vPosition");
            GLES30.glEnableVertexAttribArray(vertexID);
            GLES30.glVertexAttribPointer(vertexID, 3, GLES30.GL_FLOAT, false, VERTEX_STRIDE, verticesBuffer);

            int texID = shader.getAttribLocation("vTexCoord");
            GLES30.glEnableVertexAttribArray(texID);
            GLES30.glVertexAttribPointer(texID, 2, GLES30.GL_FLOAT, false, TEX_STRIDE, uvsBuffer);

            int normalID = shader.getAttribLocation("vNormal");
            GLES30.glEnableVertexAttribArray(normalID);
            GLES30.glVertexAttribPointer(normalID, 2, GLES30.GL_FLOAT, false, NORMAL_STRIDE, normalsBuffer);

            // Apply the projection and view transformation.
            GLES30.glUniformMatrix4fv(shader.getUniformLocation("view"), 1, false, view.getArray(), 0);
            GLES30.glUniformMatrix4fv(shader.getUniformLocation("model"), 1, false, transform.getLocalToWorldMatrix().getArray(), 0);
            GLES30.glUniformMatrix4fv(shader.getUniformLocation("projection"), 1, false, projection.getArray(), 0);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.getID());

            GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GLES30.GL_UNSIGNED_INT, indicesBuffer);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

            GLES30.glDisableVertexAttribArray(vertexID);
            GLES30.glDisableVertexAttribArray(texID);
            GLES30.glDisableVertexAttribArray(normalID);

            GLES30.glUseProgram(0);

        } else {
            Log.w("Draw", "Il n'y a pas de shader.");
        }

    }
}
