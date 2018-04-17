package com.example.kbourgeois.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Shader {

    private int[] idShader = new int[]{0, 0};
    private int programID = 0;
    private Context context = null;
    private String vertexSource = "";
    private String fragmentSource = "";

    public Shader() {
    }

    public int getUniformLocation(String name) {
        return GLES30.glGetUniformLocation(getProgramID(), name);
    }

    public int getAttribLocation(String name) {
        return GLES30.glGetAttribLocation(getProgramID(), name);
    }

    public Shader(final Shader shader) {
        context = shader.context;
        vertexSource = shader.vertexSource;
        fragmentSource = shader.fragmentSource;
        if (!charger()) {
            throw new RuntimeException("Erreur de chargement.");
        }
    }

    private boolean charger() {
        for (int i = 0; i < idShader.length; i++) {
            if (GLES30.glIsShader(idShader[i])) {
                GLES30.glDeleteShader(idShader[i]);
            }
        }

        if (GLES30.glIsProgram(getProgramID())) {
            GLES30.glDeleteShader(getProgramID());
        }

        if (!compilerShader(0, GLES30.GL_VERTEX_SHADER, vertexSource)) {
            return false;
        }
        if (!compilerShader(1, GLES30.GL_FRAGMENT_SHADER, fragmentSource)) {
            return false;
        }

        programID = GLES30.glCreateProgram();

        for (int i = 0; i < 2; i++) {
            GLES30.glAttachShader(getProgramID(), idShader[i]);
        }

        GLES30.glBindAttribLocation(getProgramID(), 0, "vPosition");
        GLES30.glBindAttribLocation(getProgramID(), 1, "vNormal");
        GLES30.glBindAttribLocation(getProgramID(), 2, "vTexCoord");

        GLES30.glLinkProgram(getProgramID());

        int[] params = new int[]{0};

        GLES30.glGetProgramiv(getProgramID(), GLES30.GL_LINK_STATUS, params, 0);

        if (params[0] != GLES30.GL_TRUE) {
            String erreur = GLES30.glGetShaderInfoLog(getProgramID());
            Log.e("Shader", erreur);
            GLES30.glDeleteProgram(getProgramID());
            return false;
        }

        return true;
    }

    private boolean compilerShader(int index, int type, String fichierSource) {
        idShader[index] = GLES30.glCreateShader(type);

        if (idShader[index] == 0) {
            Log.e("Shader", "Erreur, le type de shader (" + type + ") n'existe pas");
            return false;
        }

        StringBuffer codeSource = new StringBuffer();

        try {
            InputStream inputStream = context.getAssets().open(fichierSource);
            Scanner scanner = new Scanner(inputStream);

            if (scanner.hasNext()) {
                codeSource.append(scanner.nextLine());
                codeSource.append("\n");
            }

            inputStream.close();

        } catch (IOException e) {
            Log.e("Shader", "Compilation", e);
            GLES30.glDeleteShader(idShader[index]);
            return false;
        }

        GLES30.glShaderSource(idShader[index], codeSource.toString());
        GLES30.glCompileShader(idShader[index]);

        int[] erreurCompilation = new int[]{0};

        GLES30.glGetShaderiv(idShader[index], GLES20.GL_COMPILE_STATUS, erreurCompilation, 0);

        if (erreurCompilation[0] != GLES30.GL_TRUE) {
            String erreur = GLES30.glGetShaderInfoLog(idShader[index]);
            Log.e("Shader", erreur);
            GLES30.glDeleteShader(idShader[index]);
            return false;
        }
        return true;
    }

    public int getProgramID() {
        return programID;
    }

    public Shader(Context context, String vertexSource, String fragmentSource) {
        this.context = context;
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
        if (!charger()) {
            throw new RuntimeException("Erreur de chargement.");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        GLES30.glDeleteShader(idShader[0]);
        GLES30.glDeleteShader(idShader[1]);
        GLES30.glDeleteProgram(getProgramID());
        super.finalize();
    }
}
