package com.davidread.rollerball;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * {@link MainActivity} represents a user interface that allows the user to play a Rollerball game.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * Int constant representing the threshold at which an accelerometer magnitude difference should
     * be considered a device shake.
     */
    private final int SHAKE_THRESHOLD = 100;

    /**
     * {@link SensorManager} for accessing the device's sensors.
     */
    private SensorManager mSensorManager;

    /**
     * {@link Sensor} for accessing the device's accelerometer data.
     */
    private Sensor mAccelerometer;

    /**
     * {@link RollerSurfaceView} to display the UI of the Rollergame.
     */
    private RollerSurfaceView mSurfaceView;

    /**
     * Float holding the last reported accelerometer magnitude.
     */
    private float mLastAcceleration = SensorManager.GRAVITY_EARTH;

    /**
     * Invoked once when {@link MainActivity} is initially created. It simply initializes member
     * variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSurfaceView = findViewById(R.id.rollerSurface);
        mSurfaceView.setOnClickListener(view -> mSurfaceView.shake());
    }

    /**
     * Invoked when {@link MainActivity} enters the foreground. It simply registers the listener
     * implemented in this activity with {@link #mSensorManager}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Invoked when {@link MainActivity} leaves the foreground. It simply unregisters the listener
     * set in {@link #onResume()}.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    /**
     * Invoked when a new sensor event occurs. It notifies {@link #mSurfaceView} of the new
     * accelerometer values and device shakes.
     *
     * @param sensorEvent The {@link SensorEvent} that occurred.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Get accelerometer values.
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Move the ball.
        mSurfaceView.changeAcceleration(x, y);

        // Find magnitude of acceleration.
        float currentAcceleration = x * x + y * y + z * z;

        // Calculate difference between 2 readings.
        float delta = currentAcceleration - mLastAcceleration;

        // Save for next time.
        mLastAcceleration = currentAcceleration;

        // Detect shake.
        if (Math.abs(delta) > SHAKE_THRESHOLD) {
            mSurfaceView.shake();
        }
    }

    /**
     * Invoked when the accuracy of the registered sensor has changed. This does nothing.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}