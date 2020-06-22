package com.huxq17.floatball.nopermission;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.huxq17.floatball.libarary.FloatBallManager;
import com.huxq17.floatball.libarary.floatball.FloatBallCfg;
import com.huxq17.floatball.libarary.menu.FloatMenuCfg;
import com.huxq17.floatball.libarary.menu.MenuItem;
import com.huxq17.floatball.libarary.utils.BackGroudSeletor;
import com.huxq17.floatball.libarary.utils.DensityUtil;

public class MainActivity extends Activity {


    private static final int OVERLAY_PERMISSION_CODE = 0x1111;

    public void showFloatBall(View v) {
//        mFloatballManager.show();
        setFullScreen(v);
        showAndHideFloatingBall(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        //只有activity被添加到windowmanager上以后才可以调用show方法。
//        mFloatballManager.show();
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        mFloatballManager.hide();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    private void exitFullScreen() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        isfull = false;
    }

    private boolean isfull = false;

    public void setFullScreen(View view) {
        if (isfull == true) {
            exitFullScreen();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isfull = true;
        }
    }

    public void showAndHideFloatingBall(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(this, FloatingBallService.class);
                startService(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            }
        } else {
            Intent intent = new Intent(this, FloatingBallService.class);
            startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (OVERLAY_PERMISSION_CODE == requestCode) {
            Intent intent = new Intent(this, FloatingBallService.class);
            startService(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, FloatingBallService.class);
        stopService(intent);
        super.onDestroy();
    }
}
