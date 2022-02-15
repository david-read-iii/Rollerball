package com.davidread.rollerball;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * {@link RollerSurfaceView} is a custom view that represents the user interface of a Rollerball
 * game.
 */
public class RollerSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * {@link RollerThread} that runs operations on a background thread that display game elements
     * on this {@link RollerSurfaceView}.
     */
    private RollerThread mRollerThread;

    /**
     * Constructs a new {@link RollerSurfaceView}.
     *
     * @param context {@link Context} for the superclass.
     * @param attrs   {@link AttributeSet} for the superclass.
     */
    public RollerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    /**
     * Invoked immediately after this {@link SurfaceHolder} is created. It passes the
     * {@link SurfaceHolder} to {@link #mRollerThread} so its operations can be done on a
     * background thread.
     *
     * @param holder {@link SurfaceHolder} whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRollerThread = new RollerThread(holder);
        mRollerThread.start();
    }

    /**
     * Invoked immediately after any structural changes (format or size) have been made to the
     * {@link SurfaceHolder}. It does nothing.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Invoked immediately before a {@link SurfaceHolder} is being destroyed. It stops the thread
     * started in {@link #mRollerThread}.
     *
     * @param holder {@link SurfaceHolder} whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRollerThread.stopThread();
    }

    /**
     * Notifies {@link #mRollerThread} of new accelerometer values.
     *
     * @param x Accelerometer x-value.
     * @param y Accelerometer y-value.
     */
    public void changeAcceleration(float x, float y) {
        if (mRollerThread != null) {
            mRollerThread.changeAcceleration(x, y);
        }
    }

    /**
     * Notifies {@link #mRollerThread} of a device shake.
     */
    public void shake() {
        if (mRollerThread != null) {
            mRollerThread.shake();
        }
    }
}