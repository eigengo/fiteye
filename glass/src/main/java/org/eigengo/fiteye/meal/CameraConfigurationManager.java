
/*
 * Copyright (C) 2014 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eigengo.fiteye.meal;

import android.hardware.Camera;
import com.google.zxing.client.android.camera.CameraConfigurationUtils;

/**
 * @author Sean Owen
 */
final class CameraConfigurationManager {

    static final int ZOOM = 5;

    private CameraConfigurationManager() {
    }

    static void configure(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(640, 360);
        //parameters.setPreviewSize(1280, 720);
        //parameters.setPreviewSize(1920, 1080);
        configureAdvanced(parameters);
        camera.setParameters(parameters);
    }

    private static void configureAdvanced(Camera.Parameters parameters) {
        CameraConfigurationUtils.setBestPreviewFPS(parameters);
        CameraConfigurationUtils.setBarcodeSceneMode(parameters);
        CameraConfigurationUtils.setVideoStabilization(parameters);
        CameraConfigurationUtils.setMetering(parameters);
        CameraConfigurationUtils.setFocusArea(parameters);
        CameraConfigurationUtils.setBestExposure(parameters, false);
        CameraConfigurationUtils.setInvertColor(parameters);
        CameraConfigurationUtils.setZoom(parameters, ZOOM);
    }

}