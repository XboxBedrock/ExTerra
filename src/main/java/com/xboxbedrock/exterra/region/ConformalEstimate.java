package com.xboxbedrock.exterra.region;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.xboxbedrock.exterra.region.Airocean;
import com.xboxbedrock.exterra.region.ConformalEstimate;
import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.InvertableVectorField;
import com.xboxbedrock.exterra.region.InvertedOrientation;
import com.xboxbedrock.exterra.region.ModifiedAirocean;
import com.xboxbedrock.exterra.region.UprightOrientation;
import com.xboxbedrock.exterra.region.GeographicProjection.Orientation;

public class ConformalEstimate extends Airocean {

    InvertableVectorField forward;
    InvertableVectorField inverse;

    double VECTOR_SCALE_FACTOR = 1/1.1473979730192934;

    public ConformalEstimate () {
        InputStream is = null;


        int sideLength = 256;

        double[][] xs = new double[sideLength + 1][];
        double[][] ys = new double[xs.length][];

        try {
            //is = new FileInputStream("../resources/assets/terra121/data/conformal.txt");
            is = getClass().getClassLoader().getResourceAsStream("sledgehammer/data/conformal.txt");
            Scanner sc = new Scanner(is);

            for (int u = 0; u < xs.length; u++) {
                double[] px = new double[xs.length - u];
                double[] py = new double[xs.length - u];
                xs[u] = px;
                ys[u] = py;
            }

            for (int v = 0; v < xs.length; v++) {
                for (int u = 0; u < xs.length - v; u++) {
                    String line = sc.nextLine();
                    line = line.substring(1, line.length() - 3);
                    String[] split = line.split(", ");
                    xs[u][v] = Double.parseDouble(split[0]) * VECTOR_SCALE_FACTOR;
                    ys[u][v] = Double.parseDouble(split[1]) * VECTOR_SCALE_FACTOR;
                }
            }

            is.close();
        }catch (IOException e) {
            System.err.println("Can't load conformal: "+e);
        }

        inverse = new InvertableVectorField(xs, ys);
    }

    protected double[] triangleTransform(double x, double y, double z) {
        double[] c = super.triangleTransform(x,y,z);

        x = c[0];
        y = c[1];

        c[0] /= ARC;
        c[1] /= ARC;

        c[0] += 0.5;
        c[1] += ROOT3/6;

        //use another interpolated vector to have a really good guess before using newtons method
        //c = forward.getInterpolatedVector(c[0], c[1]);
        //c = inverse.applyNewtonsMethod(x, y, c[0]/ARC + 0.5, c[1]/ARC + ROOT3/6, 1);

        //just use newtons method: slower
        c = inverse.applyNewtonsMethod(x, y, c[0], c[1], 5);//c[0]/ARC + 0.5, c[1]/ARC + ROOT3/6

        c[0] -= 0.5;
        c[1] -= ROOT3/6;

        c[0] *= ARC;
        c[1] *= ARC;


        return c;
    }

    protected double[] inverseTriangleTransform(double x, double y) {


        x /= ARC;
        y /= ARC;

        x += 0.5;
        y += ROOT3/6;

        double[] c = inverse.getInterpolatedVector(x, y);

        return  super.inverseTriangleTransform(c[0],c[1]);
    }

    public double metersPerUnit() {
        return (40075017/(2*Math.PI))/VECTOR_SCALE_FACTOR;
    }
}


