package com.davidread.rollerball;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.SurfaceHolder;

/**
 * {@link RollerThread} is used to execute a game loop for a Rollerball game on a background thread.
 * {@link #mRollerGame} is continually updated with new game state and {@link #mSurfaceHolder} is
 * continually updated to display the UI associated with that game state.
 */
public class RollerThread extends Thread {

    /**
     * {@link SurfaceHolder} being rendered in this {@link RollerThread} on a background thread.
     */
    private SurfaceHolder mSurfaceHolder;

    /**
     * {@link RollerGame} containing the logic and game state of the Rollerball game. Is updated
     * in this {@link RollerThread} on a background thread.
     */
    private RollerGame mRollerGame;

    /**
     * Whether this {@link RollerThread} is executing its operations in {@link #run()}.
     */
    private boolean mThreadRunning;

    /**
     * {@link PointF} to pass accelerometer values to {@link #mRollerGame} in a single object.
     */
    private PointF mVelocity;

    /**
     * Constructs a new {@link RollerThread}.
     *
     * @param holder {@link SurfaceHolder} being rendered in this {@link RollerThread} on a
     *               background thread.
     */
    public RollerThread(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mThreadRunning = true;

        mVelocity = new PointF();

        // Create a ball with boundaries determined by SurfaceView.
        Canvas canvas = mSurfaceHolder.lockCanvas();
        mRollerGame = new RollerGame(canvas.getWidth(), canvas.getHeight());
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * Invoked when this {@link RollerThread} should run its operations on a background thread. It
     * initiates a game loop that continually updates game logic in {@link #mRollerGame} and game
     * UI in {@link #mSurfaceHolder}.
     */
    @Override
    public void run() {
        try {
            while (mThreadRunning) {
                Canvas canvas = mSurfaceHolder.lockCanvas();
                mRollerGame.update(mVelocity);
                mRollerGame.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        } catch (NullPointerException ex) {
            // In case canvas is destroyed while thread is running.
            ex.printStackTrace();
        }
    }

    /**
     * Puts new accelerometer values in {@link #mVelocity}.
     *
     * @param xForce Accelerometer x-value.
     * @param yForce Accelerometer y-value.
     */
    public void changeAcceleration(float xForce, float yForce) {
        mVelocity.x = xForce;
        mVelocity.y = yForce;
    }

    /**
     * Stops the background thread executing in this {@link RollerThread}.
     */
    public void stopThread() {
        mThreadRunning = false;
    }

    /**
     * Notifies {@link #mRollerGame} of a device shake.
     */
    public void shake() {
        mRollerGame.newGame();
    }
}