/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.example.mapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GroundOverlayDemoActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    private static final int TRANSPARENCY_MAX = 100;

    private static final LatLng NEWARK = new LatLng(40.714086, -74.228697);

    private static final LatLng NEAR_NEWARK =
        new LatLng(NEWARK.latitude - 0.001, NEWARK.longitude - 0.025);

    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();

    private GroundOverlay mGroundOverlay;

    private GroundOverlay mGroundOverlayRotated;

    private SeekBar mTransparencyBar;

    private int mCurrentEntry = 0;

    private final int size = 20;
    GroundOverlay[][] groundOverlays = new GroundOverlay[size][size];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ground_overlay_demo);

        mTransparencyBar = (SeekBar) findViewById(R.id.transparencySeekBar);
        mTransparencyBar.setMax(TRANSPARENCY_MAX);
        mTransparencyBar.setProgress(0);

        SupportMapFragment mapFragment =
            (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, 11));
        mImages.clear();
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.newark_nj_1922));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.badge_sa));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                LatLng latLng = new LatLng(
                    NEAR_NEWARK.latitude + 0.004 * i,
                    NEAR_NEWARK.longitude + 0.006 * j
                );
                groundOverlays[i][j] = map.addGroundOverlay(new GroundOverlayOptions()
                    .image(mImages.get(1)).anchor(0, 1)
                    .position(latLng, 430f, 302f)
                );
            }
        }
        updateZIndexes();

        mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
            .image(mImages.get(mCurrentEntry)).anchor(0, 1)
            .zIndex(0.5f)
            .position(NEWARK, 8600f, 6500f));
    }

    public void switchImage(View view) {
        updateZIndexes();
        mCurrentEntry = (mCurrentEntry + 1) % mImages.size();
        mGroundOverlay.setImage(mImages.get(mCurrentEntry));
    }

    private void updateZIndexes() {
        for (GroundOverlay[] groundOverlays1 : groundOverlays) {
            for (GroundOverlay groundOverlay : groundOverlays1) {
                if (groundOverlay != null) {
                    groundOverlay.setZIndex((float) Math.random());
                }
            }
        }
    }
}
