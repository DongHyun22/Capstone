package com.example.tannae.activity.main_service;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;

import com.example.tannae.R;
import com.example.tannae.activity.user_service.UserServiceListActivity;
import com.example.tannae.network.Network;
import com.example.tannae.sub.InnerDB;
import com.example.tannae.sub.Toaster;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

// << Main Activity >>
public class MainActivity extends AppCompatActivity {
    private FloatingActionButton reqBtn;
    private long backKeyPressedTime = 0;
    private Toolbar toolbar;
    private ActionMenuItemView drive;
    private BottomAppBar bottomAppBar;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setEventListeners();
        if (!Network.socket.isActive())
            Network.socket.connect();

        (mapView = new MapView(this)).setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.566406178655534, 126.97786868931414), true);
        mapView.setZoomLevel(2, true);
        (mapViewContainer = findViewById(R.id.map_view_main)).addView(mapView);
    }

    // < Register views >
    private void setViews() {
        reqBtn = findViewById(R.id.req_button_main);
        bottomAppBar = findViewById(R.id.bottomAppBar_main);
        (drive = findViewById(R.id.item_drive_menu)).setVisibility(InnerDB.sp.getInt("drive", 0) == 1 ? View.VISIBLE : View.INVISIBLE);
        setSupportActionBar((toolbar = findViewById(R.id.topAppBar_main)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void setEventListeners() {
        reqBtn.setOnClickListener(v -> {
            startActivity(InnerDB.sp.getInt("state", 0) == 1
                    ? new Intent(getApplicationContext(), NavigationActivity.class).putExtra("type", false)
                    : new Intent(getApplicationContext(), ServiceReqActivity.class));
            mapViewContainer.removeView(mapView);
        });

        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.item_drive_menu) {
                mapViewContainer.removeView(mapView);
                startActivity(new Intent(getApplicationContext(), NavigationActivity.class).putExtra("type", true));
                return true;
            }
            return false;
        });

        toolbar.setNavigationOnClickListener(v -> {
            mapViewContainer.removeView(mapView);
            startActivity(new Intent(getApplicationContext(), UserServiceListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    // < BackPress >
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toaster.show(getApplicationContext(), "종료하려면 한번 더 누르세요.");
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}