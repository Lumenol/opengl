package com.example.kbourgeois.opengl.ModelLoader;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Materiel {

    private String name;
    private double Ns, Ni, d;
    private int Ka, Kd, Ks;
    private int illum;
    private String map_Kd;

    public static Map<String, Materiel> readMateriel(InputStream is) {

        Map<String, Materiel> materiels = new HashMap<>();

        Scanner sc = new Scanner(is);

        Materiel materiel = null;

        while (sc.hasNext()) {
            String s = sc.nextLine();
            String[] split = s.split("\\s");
            switch (split[0]) {
                case "newmtl":
                    if (materiel != null) {
                        materiels.put(materiel.getName(), materiel);
                    }
                    materiel = new Materiel();
                    materiel.name = split[1];
                    break;

                case "Ns":
                    materiel.Ns = Float.parseFloat(split[1]);
                    break;

                case "Ka": {
                    float r = Float.parseFloat(split[1]);
                    float g = Float.parseFloat(split[2]);
                    float b = Float.parseFloat(split[3]);

                    materiel.Ka = colorConverter(1, r, g, b);
                }
                break;

                case "Kd": {
                    float r = Float.parseFloat(split[1]);
                    float g = Float.parseFloat(split[2]);
                    float b = Float.parseFloat(split[3]);

                    materiel.Kd = colorConverter(1, r, g, b);
                }
                break;

                case "Ks": {
                    float r = Float.parseFloat(split[1]);
                    float g = Float.parseFloat(split[2]);
                    float b = Float.parseFloat(split[3]);

                    materiel.Ks = colorConverter(1, r, g, b);
                }
                break;

                case "Ni":
                    materiel.Ni = Float.parseFloat(split[1]);
                    break;


                case "d":
                    materiel.d = Float.parseFloat(split[1]);
                    break;


                case "illum":
                    materiel.illum = Integer.parseInt(split[1]);
                    break;

                case "map_Kd":
                    materiel.map_Kd = split[1];
                    break;
            }
        }

        if (materiel != null) {
            materiels.put(materiel.getName(), materiel);
        }
        sc.close();
        return materiels;

    }

    private static int colorConverter(float a, float r, float g, float b) {
        return (255 & 0xff) << 24 | ((int) (r * 255) & 0xff) << 16 | ((int) (g * 255) & 0xff) << 8 | ((int) (b * 255) & 0xff);
    }

    public String getName() {
        return name;
    }

    public double getNs() {
        return Ns;
    }

    public double getNi() {
        return Ni;
    }

    public double getD() {
        return d;
    }

    public int getKa() {
        return Ka;
    }

    public int getKd() {
        return Kd;
    }

    public int getKs() {
        return Ks;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

    public String getMap_Kd() {
        return map_Kd;
    }

    public void setMap_Kd(String map_Kd) {
        this.map_Kd = map_Kd;
    }
}
