package com.xboxbedrock.exterra.region;

import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.ProjectionTransform;
import com.xboxbedrock.exterra.region.Airocean;
import com.xboxbedrock.exterra.region.ConformalEstimate;
import com.xboxbedrock.exterra.region.GeographicProjection;
import com.xboxbedrock.exterra.region.InvertableVectorField;
import com.xboxbedrock.exterra.region.InvertedOrientation;
import com.xboxbedrock.exterra.region.ModifiedAirocean;
import com.xboxbedrock.exterra.region.GeographicProjection.Orientation;

public abstract class ProjectionTransform extends GeographicProjection {
    protected GeographicProjection input;

    public ProjectionTransform(GeographicProjection input) {
        this.input = input;
    }

    public boolean upright() {
        return input.upright();
    }

    public double[] bounds() {
        return input.bounds();
    }

    public double metersPerUnit() {
        return input.metersPerUnit();
    }
}

