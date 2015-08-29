package com.fernandaochoa.lab2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


//Controlling a Camera's Flash Light

public class MainActivity extends AppCompatActivity {
    //Declare my new fields
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First check if device is supporting flashlight or not

        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            //This device does not support flash, show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Closing the application
                    finish();
                }
            });
            alert.show();
        }
        getCamera();

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

    }
    //Create method getCamera() to initialize the camera and param

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                param = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }

        }
    }

    //Turning On Flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || param == null) {
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Parameters.FLASH_MODE_TORCH);
            param.setParameters(param);
            camera.startPreview();
            isFlashOn = true;

            Log.v("Fernanda Ochoa", "Flash has been turned on ...");
        }
    }

    //Turning Off Flash

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || param == null) {
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn = false;

            Log.v("Fernanda Ochoa", "Flash has been turned off ...");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
