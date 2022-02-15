package com.davidread.rollerball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * {@link RollerGame} represents the Rollerball game with a single ball and three walls. It has
 * methods for placing the walls in random vertical locations and determining when the game is over.
 */
public class RollerGame {

    /**
     * Int constant representing the number of walls to spawn in a game.
     */
    public final int NUM_WALLS = 3;

    /**
     * {@link Ball} used to represent the ball and draw it.
     */
    private Ball mBall;

    /**
     * {@link ArrayList} of {@link Wall}s used to represent the walls and draw them.
     */
    private ArrayList<Wall> mWalls;

    /**
     * Int holding the width of the {@link RollerSurfaceView} displaying this game.
     */
    private int mSurfaceWidth;

    /**
     * Int holding the height of the {@link RollerSurfaceView} displaying this game.
     */
    private int mSurfaceHeight;

    /**
     * {@link Paint} used to paint text.
     */
    private Paint mPaint;

    /**
     * Whether the game is over.
     */
    private boolean mGameOver;

    /**
     * {@link Random} used to generate random wall initial positions.
     */
    private Random mRandom;

    /**
     * Constructs a new {@link RollerGame}.
     *
     * @param surfaceWidth  The width of the {@link RollerSurfaceView} displaying this game.
     * @param surfaceHeight The height of the {@link RollerSurfaceView} displaying this game.
     */
    public RollerGame(int surfaceWidth, int surfaceHeight) {
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        mRandom = new Random();

        // For drawing text.
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(90);
        mPaint.setColor(Color.RED);

        mBall = new Ball(mSurfaceWidth, mSurfaceHeight);

        int wallY = mSurfaceHeight / (NUM_WALLS + 1);
        mWalls = new ArrayList<>();

        // Add walls at random locations, and alternate initial direction.
        for (int c = 1; c <= NUM_WALLS; c++) {
            boolean initialRight = c % 2 == 0;
            mWalls.add(new Wall(mRandom.nextInt(mSurfaceWidth), wallY * c,
                    initialRight, mSurfaceWidth, mSurfaceHeight));
        }

        newGame();
    }

    /**
     * Resets the state of this {@link RollerGame}.
     */
    public void newGame() {
        mGameOver = false;

        // Reset ball at the top of the screen.
        mBall.setCenter(mSurfaceWidth / 2, mBall.RADIUS + 10);

        // Reset walls at random spots.
        for (Wall wall : mWalls) {
            wall.relocate(mRandom.nextInt(mSurfaceWidth));
        }
    }

    /**
     * Updates the state of this {@link RollerGame} given an {@link PointF} of accelerometer values.
     *
     * @param velocity {@link PointF} of accelerometer values.
     */
    public void update(PointF velocity) {

        if (mGameOver) return;

        // Move ball and walls.
        mBall.move(velocity);
        for (Wall wall : mWalls) {
            wall.move();
        }

        // Check for collision.
        for (Wall wall : mWalls) {
            if (mBall.intersects(wall)) {
                mGameOver = true;
            }
        }

        // Check for win.
        if (mBall.getBottom() >= mSurfaceHeight) {
            mGameOver = true;
        }
    }

    /**
     * Draws the UI elements of this {@link RollerGame} onto the passed {@link Canvas}.
     *
     * @param canvas {@link Canvas} on which to draw the UI elements.
     */
    public void draw(Canvas canvas) {

        // Wipe canvas clean.
        canvas.drawColor(Color.WHITE);

        // Draw ball and walls.
        mBall.draw(canvas);
        for (Wall wall : mWalls) {
            wall.draw(canvas);
        }

        // User win?
        if (mBall.getBottom() >= mSurfaceHeight) {
            String text = "You won!";
            Rect textBounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, mSurfaceWidth / 2f - textBounds.exactCenterX(),
                    mSurfaceHeight / 2f - textBounds.exactCenterY(), mPaint);
        }
    }
}