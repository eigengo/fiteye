package org.eigengo.fiteye.meal;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.TextView;
import com.google.zxing.Result;
import com.google.zxing.client.result.*;
import org.eigengo.fiteye.R;

import java.io.IOException;

public final class LogActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = LogActivity.class.getSimpleName();
    private static final String SCAN_ACTION = "com.google.zxing.client.android.SCAN";

    private boolean hasSurface;
    private boolean returnResult;
    private SurfaceHolder holderWithCallback;
    private Camera camera;
    private DecodeRunnable decodeRunnable;
    private Result result;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // returnResult should be true if activity was started using
        // startActivityForResult() with SCAN_ACTION intent
        Intent intent = getIntent();
        this.returnResult = intent != null && SCAN_ACTION.equals(intent.getAction());

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder?");
        }
        if (this.hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            this.holderWithCallback = surfaceHolder;
        }
    }

    @Override
    public synchronized void onPause() {
        this.result = null;
        if (this.decodeRunnable != null) {
            this.decodeRunnable.stop();
            this.decodeRunnable = null;
        }
        if (this.camera != null) {
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
        }
        if (this.holderWithCallback != null) {
            this.holderWithCallback.removeCallback(this);
            this.holderWithCallback = null;
        }
        super.onPause();
    }

    @Override
    public synchronized void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "Surface created");
        this.holderWithCallback = null;
        if (!this.hasSurface) {
            this.hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public synchronized void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    @Override
    public synchronized void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "Surface destroyed");
        this.holderWithCallback = null;
        this.hasSurface = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.result != null) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    handleResult(this.result);
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    reset();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initCamera(SurfaceHolder holder) {
        if (this.camera != null) {
            throw new IllegalStateException("Camera not null on initialization");
        }
        this.camera = Camera.open();
        if (this.camera == null) {
            throw new IllegalStateException("Camera is null");
        }

        CameraConfigurationManager.configure(this.camera);

        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Cannot start preview", e);
        }

        this.decodeRunnable = new DecodeRunnable(this, this.camera);
        new Thread(this.decodeRunnable).start();
        reset();
    }

    void setResult(Result result) {
        if (this.returnResult) {
            Intent scanResult = new Intent(SCAN_ACTION);
            scanResult.putExtra("SCAN_RESULT", result.getText());
            setResult(RESULT_OK, scanResult);
            finish();
        } else {
            TextView statusView = (TextView) findViewById(R.id.status_view);
            String text = result.getText();
            statusView.setText(text);
            statusView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Math.max(14, 56 - text.length() / 4));
            statusView.setVisibility(View.VISIBLE);
            this.result = result;
        }
    }

    private void handleResult(Result result) {
        ParsedResult parsed = ResultParser.parseResult(result);
        Intent intent;
        if (parsed.getType() == ParsedResultType.URI) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(((URIParsedResult) parsed).getURI()));
        } else {
            intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra("query", ((TextParsedResult) parsed).getText());
        }
        startActivity(intent);
    }

    private synchronized void reset() {
        TextView statusView = (TextView) findViewById(R.id.status_view);
        statusView.setVisibility(View.GONE);
        this.result = null;
        this.decodeRunnable.startScanning();
    }

}