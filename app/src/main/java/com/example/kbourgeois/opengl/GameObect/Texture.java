package com.example.kbourgeois.opengl.GameObect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;

public class Texture {

    private int[] textureId = new int[]{0};
    private Context context;
    private String filename;


    public Texture() {
    }

    public Texture(final Texture texture) {
        filename = texture.filename;
        if (!charger()) {
            throw new RuntimeException("Erreur de chargement.");
        }
    }

    public Texture(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        if (!charger()) {
            throw new RuntimeException("Erreur de chargement.");
        }
    }

    public void setFile(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        if (!charger()) {
            throw new RuntimeException("Erreur de chargement.");
        }
    }

    private Bitmap renverseBitmap(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, (bitmap.getHeight() - 1) * bitmap.getWidth(), -bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return Bitmap.createBitmap(pixels, bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }

    private boolean charger() {

        if (GLES30.glIsTexture(textureId[0])) {
            GLES30.glDeleteTextures(1, textureId, 0);
        }

        GLES30.glGenTextures(1, textureId, 0);
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(filename));
            Bitmap bitmapRenverse = renverseBitmap(bitmap);
            bitmap.recycle();

            // Bind to the texture in OpenGL
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, getID());

            // Set filtering
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmapRenverse, 0);

            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmapRenverse.recycle();
        } catch (IOException e) {
            Log.e("Load Texture", "open file", e);
            return false;
        }
        return true;
    }

    public int getID() {
        return textureId[0];
    }

    @Override
    protected void finalize() throws Throwable {
        GLES30.glDeleteTextures(1, textureId, 0);
        super.finalize();
    }
}
