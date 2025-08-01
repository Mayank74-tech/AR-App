package com.java.arapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.math.Vector3;

public class ARActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable cubeRenderable;
    private int selectedDrill;
    private static final int RC_PERMISSION = 1001;
    private boolean cameraPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // Check ARCore availability
        if (!checkARCoreAvailability()) {
            return;
        }

        // Request camera permission
        requestCameraPermission();

        // Initialize AR Fragment
        try {
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragmentContainer);
            if (arFragment == null) {
                throw new IllegalStateException("AR Fragment not found in layout");
            }
        } catch (ClassCastException e) {
            Toast.makeText(this, "AR Fragment initialization failed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        selectedDrill = getIntent().getIntExtra("selectedDrill", 0);
        buildCube();
    }

    private boolean checkARCoreAvailability() {
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isTransient()) {
            Toast.makeText(this, "ARCore is temporarily unavailable", Toast.LENGTH_LONG).show();
        }
        if (!availability.isSupported()) {
            Toast.makeText(this, "ARCore is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }
        return true;
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    RC_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            cameraPermissionGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!cameraPermissionGranted) {
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void installARCore() {
        try {
            switch (ArCoreApk.getInstance().requestInstall(this, !cameraPermissionGranted)) {
                case INSTALLED:
                    break;
                case INSTALL_REQUESTED:

            }
        } catch (UnavailableException e) {
            Toast.makeText(this, "ARCore unavailable: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void buildCube() {
        MaterialFactory.makeOpaqueWithColor(this, getDrillColor(selectedDrill))
                .thenAccept(material -> {
                    runOnUiThread(() -> {
                        try {
                            cubeRenderable = ShapeFactory.makeCube(
                                    new Vector3(0.1f, 0.1f, 0.1f),
                                    new Vector3(0f, 0.05f, 0f),
                                    material
                            );
                            setupTapListener();
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to create cube: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .exceptionally(throwable -> {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Failed to create material: " + throwable.getMessage(),
                                    Toast.LENGTH_LONG).show());
                    return null;
                });
    }

    private void setupTapListener() {
        if (arFragment == null || cubeRenderable == null) {
            return;
        }

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if (!cameraPermissionGranted) {
                return;
            }

            try {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                node.setParent(anchorNode);
                node.setRenderable(cubeRenderable);
                node.select();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to place object: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private Color getDrillColor(int index) {
        switch (index) {
            case 0: return new Color(1.0f, 0f, 0f, 1.0f);    // Red
            case 1: return new Color(0f, 1.0f, 0f, 1.0f);     // Green
            case 2: return new Color(0f, 0f, 1.0f, 1.0f);     // Blue
            default: return new Color(0.5f, 0.5f, 0.5f, 1.0f); // Gray
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arFragment != null && cameraPermissionGranted) {
            try {
                if (arFragment.getArSceneView().getSession() == null) {
                    installARCore();
                }
                arFragment.getArSceneView().resume();
            } catch (Exception e) {
                Toast.makeText(this, "AR session failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (arFragment != null) {
            arFragment.getArSceneView().pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (arFragment != null) {
            arFragment.getArSceneView().destroy();
        }
    }
}