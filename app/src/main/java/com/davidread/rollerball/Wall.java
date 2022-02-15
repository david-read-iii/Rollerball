package com.davidread.rollerball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * {@link Wall} represents a moving wall in a Rollerball game. It has methods to move and draw the
 * wall.
 */
public class Wall {

    /**
     * Int constant representing the horizontal velocity of this {@link Wall}.
     */
    public int WALL_SPEED = 10;

    /**
     * Int holding the horizontal distance this {@link Wall} will move this iteration.
     */
    private int mMoveDistance;

    /**
     * {@link Rect} to represent the position of this {@link Wall}.
     */
    private Rect mRect;

    /**
     * Int holding the width of the {@link RollerSurfaceView} displaying this {@link Wall}.
     */
    private int mSurfaceWidth;

    /**
     * {@link Paint} used to draw this {@link Wall} on a {@link Canvas}.
     */
    private Paint mPaint;

    /**
     * Constructs a new {@link Wall}.
     *
     * @param x                     The positional x-coordinate of this {@link Wall}.
     * @param y                     The positional y-coordinate of this {@link Wall}.
     * @param initialDirectionRight Whether the initial direction of this {@link Wall} is right.
     * @param surfaceWidth          The surface width of this {@link Wall}.
     * @param surfaceHeight         The surface height of this {@link Wall}.
     */
    public Wall(int x, int y, boolean initialDirectionRight, int surfaceWidth, int surfaceHeight) {

        mSurfaceWidth = surfaceWidth;

        // Determine wall dimensions based on surface width and height.
        int width = surfaceWidth / 6;
        int height = surfaceHeight / 20;

        // Make sure wall fits completely on the surface.
        x = Math.min(x, surfaceWidth - width);
        y = Math.min(y, surfaceHeight - height);

        // Create wall's rectangle based on location and dimensions.
        mRect = new Rect(x, y, x + width, y + height);

        // Determine how many pixels walls move each iteration.
        mMoveDistance = initialDirectionRight ? WALL_SPEED : -WALL_SPEED;

        // Wall color.
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffffaaff);
    }

    /**
     * Returns the {@link Rect} being used to represent this {@link Wall}.
     *
     * @return The {@link Rect} being used to represent this {@link Wall}.
     */
    public Rect getRect() {
        return mRect;
    }

    /**
     * Moves the position of this {@link Wall} to the given x-coordinate.
     *
     * @param x The x-coordinate to move to.
     */
    public void relocate(int x) {

        // Move wall to a new x location.
        x = Math.min(x, mSurfaceWidth - mRect.width());
        mRect.offsetTo(x, mRect.top);
    }

    /**
     * Moves the position of this {@link Wall} given one second of its velocity.
     */
    public void move() {

        // Move wall right or left
        mRect.offset(mMoveDistance, 0);

        // Bounce wall off surface edges
        if (mRect.right > mSurfaceWidth) {
            mRect.offsetTo(mSurfaceWidth - mRect.width(), mRect.top);
            mMoveDistance *= -1;
        } else if (mRect.left < 0) {
            mRect.offsetTo(0, mRect.top);
            mMoveDistance *= -1;
        }
    }

    /**
     * Draws this {@link Wall} on the passed {@link Canvas}.
     *
     * @param canvas {@link Canvas} on which to draw this {@link Wall}.
     */
    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}