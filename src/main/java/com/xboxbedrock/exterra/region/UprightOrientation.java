package com.xboxbedrock.exterra.region;

import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.ProjectionTransform;
import com.xboxbedrock.exterra.region.Airocean;
import com.xboxbedrock.exterra.region.ConformalEstimate;
import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.InvertableVectorField;
import com.xboxbedrock.exterra.region.InvertedOrientation;
import com.xboxbedrock.exterra.region.ModifiedAirocean;
import com.xboxbedrock.exterra.region.UprightOrientation;
import com.xboxbedrock.exterra.region.GeographicProjection.Orientation;

public class UprightOrientation extends ProjectionTransform {

    public UprightOrientation (GeographicProjection input) {
        super(input);
    }

    public double[] toGeo(double x, double y) {
        return input.toGeo(x, -y);
    }

    public double[] fromGeo(double lon, double lat) {
        double[] p = input.fromGeo(lon, lat);
        p[1] = -p[1];
        return p;
    }

    public boolean upright() {
        return !input.upright();
    }

    public double[] bounds() {
        double[] b = input.bounds();
        return new double[] {b[0],-b[3],b[2],-b[1]};
    }
}
