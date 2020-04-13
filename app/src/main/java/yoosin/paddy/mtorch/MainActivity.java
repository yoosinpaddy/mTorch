package yoosin.paddy.mtorch;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Global global;
    boolean isTorchOn = false;
    TextView textview;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global = new Global();

        textview = findViewById(R.id.textview);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
        textview.setText(formatter.format(today));
        blink();
//        startService(new Intent(this, ShakeDetectService.class));
    }

    private void blink() {
        textview = findViewById(R.id.textview);
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat formatter2 = new SimpleDateFormat("HH mm");
        boolean isVisible = false;
        final Handler hander = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(550);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!textview.getText().toString().contains(":")) {
                            textview.setText(formatter.format(new Date()));
                        } else {
                            textview.setText(formatter2.format(new Date()));
                        }
                        blink();
                    }
                });
            }
        }).start();
    }

    public void toggle(View view) {
        Button button = (Button) view;
        ImageView imageView = findViewById(R.id.icon);
        if (button.getText().equals("Switch On")) {
            button.setText(R.string.switch_off_text);
            button.setBackground(getResources().getDrawable(R.drawable.turnoff));
            imageView.setAlpha((float) 0.5);
            torchToggle("on");
        } else {
            button.setText(R.string.switch_on_text);
            button.setBackground(getResources().getDrawable(R.drawable.turnon));
            imageView.setAlpha((float) 1.0);
            torchToggle("off");
        }
    }

    private void torchToggle(String command) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                }
                if (camManager != null) {
                    if (command.equals("on")) {
                        camManager.setTorchMode(cameraId, true);   // Turn ON
                        isTorchOn = true;
                    } else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                        isTorchOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }

    public void goToSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
