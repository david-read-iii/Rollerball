package com.davidread.rollerball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * {@link Ball} represents the ball within a Rollerball game. It has methods to move the ball, draw
 * the ball, and determine if a ball collides with a wall.
 */
public class Ball {

    /**
     * Int constant representing the radius of this {@link Ball}.
     */
    public final int RADIUS = 100;

    /**
     * {@link Paint} used to draw this {@link Ball} on a {@link Canvas}.
     */
    private Paint mPaint;

    /**
     * {@link PointF} to hold the position coordinates of this {@link Ball}.
     */
    private PointF mCenter;

    /**
     * Int holding the width of the {@link RollerSurfaceView} displaying this {@link Ball}.
     */
    private int mSurfaceWidth;

    /**
     * Int holding the height of the {@link RollerSurfaceView} displaying this {@link Ball}.
     */
    private int mSurfaceHeight;

    /**
     * Constructs a new {@link Ball}.
     *
     * @param surfaceWidth  The surface width of this {@link Ball}.
     * @param surfaceHeight The surface height of this {@link Ball}.
     */
    public Ball(int surfaceWidth, int surfaceHeight) {

        // Ball must stay within confines of surface.
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        // Set initial position.
        mCenter = new PointF(RADIUS, RADIUS);

        // Ball color.
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffaaaaff);
    }

    /**
     * Set the center position of this {@link Ball}.
     *
     * @param x x-coordinate of the center position.
     * @param y y-coordinate of the center position.
     */
    public void setCenter(int x, int y) {

        // Move circle center.
        mCenter.x = x;
        mCenter.y = y;
    }

    /**
     * Moves the position of this {@link Ball} given one second of the passed velocity.
     *
     * @param velocity {@link PointF} whose {@link PointF#x} is the x-velocity and whose
     *                 {@link PointF#y} is the y-velocity.
     */
    public void move(PointF velocity) {

        // Move ball's center by velocity.
        mCenter.offset(-velocity.x, velocity.y);

        // Don't go too far down or up.
        if (mCenter.y > mSurfaceHeight - RADIUS) {
            mCenter.y = mSurfaceHeight - RADIUS;
        } else if (mCenter.y < RADIUS) {
            mCenter.y = RADIUS;
        }

        // Don't go too far right or left.
        if (mCenter.x > mSurfaceWidth - RADIUS) {
            mCenter.x = mSurfaceWidth - RADIUS;
        } else if (mCenter.x < RADIUS) {
            mCenter.x = RADIUS;
        }
    }

    /**
     * Draws this {@link Ball} on the passed {@link Canvas}.
     *
     * @param canvas {@link Canvas} on which to draw this {@link Ball}.
     */
    public void draw(Canvas canvas) {
        canvas.drawCircle(mCenter.x, mCenter.y, RADIUS, mPaint);
    }

    /**
     * Returns true if this {@link Ball} is intersecting with a passed {@link Wall}.
     *
     * @param wall {@link Wall} we are checking.
     * @return True if this {@link Ball} and {@link Wall} are intersecting.
     */
    public boolean intersects(Wall wall) {

        // Find point on wall that is closest to ball center.
        Rect rect = wall.getRect();
        int nearestX = Math.max(rect.left, Math.min((int) mCenter.x, rect.right));
        int nearestY = Math.max(rect.top, Math.min((int) mCenter.y, rect.bottom));

        // Measure distance from nearest point to ball center.
        int deltaX = (int) mCenter.x - nearestX;
        int deltaY = (int) mCenter.y - nearestY;

        // Return true if distance from ball center to nearest point is less than ball radius.
        return (deltaX * deltaX + deltaY * deltaY) < (RADIUS * RADIUS);
    }

    /**
     * Returns the bottom-most y-coordinate that this {@link Ball} takes up.
     *
     * @return The bottom-most y-coordinate that this {@link Ball} takes up.
     */
    public int getBottom() {

        // Return bottom of the ball.
        return (int) mCenter.y + RADIUS;
    }
}