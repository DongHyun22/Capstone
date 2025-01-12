package com.team_seven.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.team_seven.tannae.R;
import com.team_seven.tannae.sub.InnerDB;
import com.team_seven.tannae.sub.Toaster;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

// << ServiceReq Activity >>
public class ServiceReqActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private TextView tvOrigin, tvDestination;
    private Button btnServiceReq;
    private Switch switchShare;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private boolean locationType = true;
    private String originLocation, destinationLocation;
    private double originX = 0, originY = 0, destinationX = 0, destinationY = 0;

    // < onCreate >
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicereq);
        setViews();
        setEventListeners();
        setMapView();
    }

    private void setViews() {
        btnServiceReq = findViewById(R.id.btn_request_servicereq);
        tvOrigin = findViewById(R.id.tv_origin_servicereq);
        tvDestination = findViewById(R.id.tv_destination_servicereq);
        (switchShare = findViewById(R.id.switch_share_servicereq)).setChecked(true);
        findViewById(R.id.btn_back_servicereq).setOnClickListener(v -> onBackPressed());
        ((RadioGroup) findViewById(R.id.rg_location_servicereq)).setOnCheckedChangeListener((group, checkedId) -> locationType = checkedId == R.id.rb_origin_servicereq);
    }

    private void setEventListeners() {
        btnServiceReq.setOnClickListener(v -> {
            if (!(originLocation == null || destinationLocation == null))
                try {
                    mapViewContainer.removeView(mapView);
                    startActivity(new Intent(getApplicationContext(), NavigationActivity.class).putExtra("type", false).putExtra("data", (new JSONObject()
                            .put("start", new JSONObject().put("name", originLocation).put("x", originX).put("y", originY))
                            .put("end", new JSONObject().put("name", destinationLocation).put("x", destinationX).put("y", destinationY))
                            .put("share", switchShare.isChecked()).put("user", InnerDB.getUser())).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            else
                Toaster.show(getApplicationContext(), "주변에 정차할 수 있는 도로가 없습니다.\n올바른 위치를 입력해주세요.");
        });
    }

    private void setMapView() {
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.map_view_servicereq);

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.1761175, 126.9058167), true);
        mapView.setZoomLevel(2, true);
        mapViewContainer.addView(mapView);
    }

    // < BackPress >
    @Override
    public void onBackPressed() {
        mapViewContainer.removeView(mapView);
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    // < Map Methods >
    @Override
    public void onMapViewInitialized(MapView mapView) {
        MapPoint mapPoint = mapView.getMapCenterPoint();
        MapReverseGeoCoder mapGeoCoder = new MapReverseGeoCoder("be32c53145962ae88db090324e2223b0",
                mapPoint, this, ServiceReqActivity.this);
        mapGeoCoder.startFindingAddress();

        if (locationType) {
            originX = mapPoint.getMapPointGeoCoord().longitude;
            originY = mapPoint.getMapPointGeoCoord().latitude;
        } else {
            destinationX = mapPoint.getMapPointGeoCoord().longitude;
            destinationY = mapPoint.getMapPointGeoCoord().latitude;
        }
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        mapPoint = mapView.getMapCenterPoint();
        MapReverseGeoCoder mapGeoCoder = new MapReverseGeoCoder("be32c53145962ae88db090324e2223b0",
                mapPoint, this, ServiceReqActivity.this);
        mapGeoCoder.startFindingAddress();

        if (locationType) {
            originX = mapPoint.getMapPointGeoCoord().longitude;
            originY = mapPoint.getMapPointGeoCoord().latitude;
        } else {
            destinationX = mapPoint.getMapPointGeoCoord().longitude;
            destinationY = mapPoint.getMapPointGeoCoord().latitude;
        }
    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        if (locationType) {
            tvOrigin.setText(s);
            originLocation = s;
        } else {
            tvDestination.setText(s);
            destinationLocation = s;
        }
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        if (locationType) {
            tvOrigin.setText("올바른 지역이 아닙니다.");
            originLocation = null;
        } else {
            tvDestination.setText("올바른 지역이 아닙니다.");
            destinationLocation = null;
        }
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        mapPoint = mapView.getMapCenterPoint();
        MapReverseGeoCoder mapGeoCoder = new MapReverseGeoCoder("be32c53145962ae88db090324e2223b0",
                mapPoint, this, ServiceReqActivity.this);
        mapGeoCoder.startFindingAddress();

        if (locationType) {
            originX = mapPoint.getMapPointGeoCoord().longitude;
            originY = mapPoint.getMapPointGeoCoord().latitude;
        } else {
            destinationX = mapPoint.getMapPointGeoCoord().longitude;
            destinationY = mapPoint.getMapPointGeoCoord().latitude;
        }
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
