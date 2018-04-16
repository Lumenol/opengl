package com.example.kbourgeois.opengl;

import android.opengl.GLES30;
import android.util.Log;

public class Texture {

    private int[] textureId = new int[]{0};
    private int ressourceId;

    public Texture() {
    }

    public Texture(final Texture texture) {
        ressourceId = texture.ressourceId;
        charger();
    }

    public Texture(int ressourceId) {
        this.ressourceId = ressourceId;
    }

    public void setRessource(int ressourceId) {
        this.ressourceId = ressourceId;
    }

    public boolean charger() {

        if (GLES30.glIsTexture(textureId[0])) {
            GLES30.glDeleteTextures(1, textureId, 0);
        }

        GLES30.glGenTextures(1, textureId, 0);
        try {
            ShaderUtilities.loadTexture(ressourceId, textureId, 0);
        } catch (RuntimeException e) {
            Log.e("Load Texture", "ShaderUtilites.loadTexture", e);
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
