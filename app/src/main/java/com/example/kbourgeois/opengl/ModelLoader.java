package com.example.kbourgeois.opengl;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class ModelLoader {

    public static Model3D readOBJFile(final Context c, final String filename) {
        try {
            Log.i("Info", "readOBJFile: Reading file");
            InputStream is = c.getAssets().open(filename);

            java.util.Scanner sc = new java.util.Scanner(is);
            Vector<Float> vertices_tmp = new Vector<>();
            Vector<Float> normals_tmp = new Vector<>();
            Vector<Float> UVs_tmp = new Vector<>();
            Vector<Integer> verticesIndex = new Vector<>();
            Vector<Integer> normalsIndex = new Vector<>();
            Vector<Integer> UVsIndex = new Vector<>();

            while(sc.hasNext()){
                String s = sc.nextLine();
                String[] splited = s.split("\\s");

                switch (splited[0]) {
                    case "v":
                        vertices_tmp.add(Float.valueOf(splited[1]));
                        vertices_tmp.add(Float.valueOf(splited[2]));
                        vertices_tmp.add(Float.valueOf(splited[3]));
                        break;
                    case "vt":
                        UVs_tmp.add(Float.valueOf(splited[1]));
                        UVs_tmp.add(Float.valueOf(splited[2]));
                        break;
                    case "vn":
                        normals_tmp.add(Float.valueOf(splited[1]));
                        normals_tmp.add(Float.valueOf(splited[2]));
                        normals_tmp.add(Float.valueOf(splited[3]));
                        break;
                    case "f":
                        String[] face1 = splited[1].split("/");
                        String[] face2 = splited[2].split("/");
                        String[] face3 = splited[3].split("/");
                        verticesIndex.add(Integer.valueOf(face1[0]));
                        verticesIndex.add(Integer.valueOf(face2[0]));
                        verticesIndex.add(Integer.valueOf(face3[0]));
                        UVsIndex.add(Integer.valueOf(face1[1]));
                        UVsIndex.add(Integer.valueOf(face2[1]));
                        UVsIndex.add(Integer.valueOf(face3[1]));
                        normalsIndex.add(Integer.valueOf(face1[2]));
                        normalsIndex.add(Integer.valueOf(face2[2]));
                        normalsIndex.add(Integer.valueOf(face3[2]));
                        break;
                    default:
                        break;
                }
            }

            is.close();

            float[] vertices = new float[verticesIndex.size() * 3];
            float[] UVs = new float[UVsIndex.size() * 2];
            float[] normals = new float[normalsIndex.size() * 3];
            int[] indices = new int[verticesIndex.size()];

            for (int i = 0; i < verticesIndex.size(); ++i) {
                vertices[i * 3] = vertices_tmp.elementAt((verticesIndex.elementAt(i) - 1) * 3);
                vertices[i * 3 + 1] = vertices_tmp.elementAt((verticesIndex.elementAt(i) - 1) * 3 + 1);
                vertices[i * 3 + 2] = vertices_tmp.elementAt((verticesIndex.elementAt(i) - 1) * 3 + 2);

                UVs[i * 2] = UVs_tmp.elementAt((UVsIndex.elementAt(i) - 1) * 2);
                UVs[i * 2 + 1] = UVs_tmp.elementAt((UVsIndex.elementAt(i) - 1) * 2 + 1);

                normals[i * 3] = normals_tmp.elementAt((normalsIndex.elementAt(i) - 1) * 3);
                normals[i * 3 + 1] = normals_tmp.elementAt((normalsIndex.elementAt(i) - 1) * 3 + 1);
                normals[i * 3 + 2] = normals_tmp.elementAt((normalsIndex.elementAt(i) - 1) * 3 + 2);

                indices[i] = i;
            }

            return new Model3D(vertices, normals, UVs, indices);
        } catch (IOException e) {
            Log.e("Error", "readOBJFile: " + e.getMessage(), e.getCause());
            return null;
        }
    }
}
