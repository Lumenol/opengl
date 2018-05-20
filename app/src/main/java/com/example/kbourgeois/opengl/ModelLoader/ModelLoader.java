package com.example.kbourgeois.opengl.ModelLoader;

import android.content.Context;
import android.util.Log;

import com.example.kbourgeois.opengl.GameObect.Model3D;
import com.example.kbourgeois.opengl.GameObect.Shader;
import com.example.kbourgeois.opengl.GameObect.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ModelLoader {

    private static Model3D makeModel(Vector<Float> vertices_tmp, Vector<Float> normals_tmp, Vector<Float> UVs_tmp, Vector<Integer> verticesIndex, Vector<Integer> normalsIndex, Vector<Integer> UVsIndex, String name) {
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
        if (indices.length > 0) {
            return new Model3D(vertices, normals, UVs, indices, name);
        } else {
            return null;
        }
    }

    public static Map<String, Model3D> readOBJFile(final Context c, final String filename) {
        return readOBJFile(c, filename, null);
    }

    public static Map<String, Model3D> readOBJFile(final Context c, final String filename, Shader shader) {
        Map<String, Model3D> model3DS = new HashMap<>();

        Map<String, Materiel> materiels = new HashMap<>();

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

            int cpt = 2;

            String materiel = "";

            String name1 = "", name2 = "";

            while (sc.hasNext()) {
                while (sc.hasNext() && cpt > 0) {
                    String s = sc.nextLine();
                    String[] splited = s.split("\\s");

                    switch (splited[0]) {

                        case "mtllib":
                            try {
                                materiels = Materiel.readMateriel(c.getAssets().open(splited[1]));
                            } catch (IOException e) {
                                Log.e("ModelReader", "Materiel", e);
                            }
                            break;

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
                        case "o":
                            if (cpt > 1) {
                                name1 = name2 = splited[1];
                            } else {
                                name1 = name2;
                                name2 = splited[1];
                            }
                            cpt--;
                            break;

                        case "usemtl":
                            materiel = splited[1];
                            break;

                        default:
                            break;
                    }
                }
                if (cpt > 0) {
                    name1 = name2;
                }
                Model3D model3D = makeModel(vertices_tmp, normals_tmp, UVs_tmp, verticesIndex, normalsIndex, UVsIndex, name1);
                if (model3D != null) {
                    Materiel mtl = materiels.get(materiel);
                    if (mtl != null) {
                        model3D.setTexture(new Texture(c, mtl.getMap_Kd()));
                    }
                    model3D.setShader(shader);
                    model3DS.put(model3D.getName(), model3D);
                }

                materiel = "";

                verticesIndex.clear();
                normalsIndex.clear();
                UVsIndex.clear();

                cpt = 1;
            }

            is.close();
        } catch (IOException e) {
            Log.e("Error", "readOBJFile: " + e.getMessage(), e.getCause());
            return null;
        }

        return model3DS;
    }
}
