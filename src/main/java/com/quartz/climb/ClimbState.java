package com.quartz.climb;

/** Shared per-client climbing state. */
public final class ClimbState {
    public boolean climbing;
    public boolean leftHand;
    public int gripTicks;
    public double wallX;
    public double wallY;
    public double wallZ;
    public float wallNormalX;
    public float wallNormalY;
    public float wallNormalZ;

    public void start(double x, double y, double z, float nx, float ny, float nz) {
        climbing = true;
        leftHand = true;
        gripTicks = 0;
        wallX = x;
        wallY = y;
        wallZ = z;
        wallNormalX = nx;
        wallNormalY = ny;
        wallNormalZ = nz;
    }

    public void stop() {
        climbing = false;
        gripTicks = 0;
    }
}
