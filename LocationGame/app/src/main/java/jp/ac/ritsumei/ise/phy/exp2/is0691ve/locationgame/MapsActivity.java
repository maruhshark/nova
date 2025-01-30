package jp.ac.ritsumei.ise.phy.exp2.is0691ve.locationgame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // ポリゴンとその状態を管理する
    private List<Polygon> polygons = new ArrayList<>();
    private HashMap<Polygon, Boolean> polygonCapturedStatus = new HashMap<>();

    // 点数管理
    private int score = 0;
    private TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 点数表示用TextViewの参照を取得
        scoreText = findViewById(R.id.score_text);

        // Googleマップの準備
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 位置情報クライアントを初期化
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // プレイヤーの位置を監視するコールバックを設定
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    checkPlayerInPolygons(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        };
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 現在地の表示を有効化
        enableMyLocation();

        // ポリゴンを描画
        drawPolygons();

        // 位置情報の更新を開始
        startLocationUpdates();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void drawPolygons() {
        // 既存の三角形の座標
        List<LatLng> polygon1 = new ArrayList<>();
        polygon1.add(new LatLng(34.810409, 135.561055)); // 頂点1（北）
        polygon1.add(new LatLng(34.809300, 135.561505)); // 頂点2（南東）
        polygon1.add(new LatLng(34.809300, 135.560605)); // 頂点3（南西）

        Polygon polygon1Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon1)
                .fillColor(0x55FF0000) // 半透明の赤色
                .strokeColor(0xFFFF0000) // 赤色の枠線
                .strokeWidth(5));

        polygons.add(polygon1Obj);
        polygonCapturedStatus.put(polygon1Obj, false);

        List<LatLng> polygon2 = new ArrayList<>();
        polygon2.add(new LatLng(34.809295, 135.561055)); // 頂点1（北）
        polygon2.add(new LatLng(34.808220, 135.561525)); // 頂点2（南東）
        polygon2.add(new LatLng(34.808220, 135.560585)); // 頂点3（南西）

        Polygon polygon2Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon2)
                .fillColor(0x55FF0000) // 半透明の赤色
                .strokeColor(0xFFFF0000) // 赤色の枠線
                .strokeWidth(5));

        polygons.add(polygon2Obj);
        polygonCapturedStatus.put(polygon2Obj, false);

        // 新しいポリゴン（3〜7）を岩倉公園に追加
        List<LatLng> polygon3 = new ArrayList<>();
        polygon3.add(new LatLng(34.810880, 135.562630)); // 頂点1
        polygon3.add(new LatLng(34.810800, 135.562680)); // 頂点2
        polygon3.add(new LatLng(34.810800, 135.562580)); // 頂点3

        Polygon polygon3Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon3)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon3Obj);
        polygonCapturedStatus.put(polygon3Obj, false);

        List<LatLng> polygon4 = new ArrayList<>();
        polygon4.add(new LatLng(34.810780, 135.563630));
        polygon4.add(new LatLng(34.810700, 135.563680));
        polygon4.add(new LatLng(34.810700, 135.563580));

        Polygon polygon4Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon4)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon4Obj);
        polygonCapturedStatus.put(polygon4Obj, false);

        List<LatLng> polygon5 = new ArrayList<>();
        polygon5.add(new LatLng(34.810450, 135.563130));
        polygon5.add(new LatLng(34.810370, 135.563180));
        polygon5.add(new LatLng(34.810370, 135.563080));

        Polygon polygon5Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon5)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon5Obj);
        polygonCapturedStatus.put(polygon5Obj, false);

        List<LatLng> polygon6 = new ArrayList<>();
        polygon6.add(new LatLng(34.810400, 135.562130));
        polygon6.add(new LatLng(34.810320, 135.562180));
        polygon6.add(new LatLng(34.810320, 135.562080));

        Polygon polygon6Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon6)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon6Obj);
        polygonCapturedStatus.put(polygon6Obj, false);

        List<LatLng> polygon7 = new ArrayList<>();
        polygon7.add(new LatLng(34.811450, 135.562300));
        polygon7.add(new LatLng(34.811370, 135.562350));
        polygon7.add(new LatLng(34.811370, 135.562250));

        Polygon polygon7Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon7)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon7Obj);
        polygonCapturedStatus.put(polygon7Obj, false);

        // 新しいポリゴン（8,9）を駐輪場辺りに追加
        List<LatLng> polygon8 = new ArrayList<>();
        polygon8.add(new LatLng(34.811880, 135.560800));
        polygon8.add(new LatLng(34.811800, 135.560850));
        polygon8.add(new LatLng(34.811800, 135.560750));

        Polygon polygon8Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon8)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon8Obj);
        polygonCapturedStatus.put(polygon8Obj, false);

        List<LatLng> polygon9 = new ArrayList<>();
        polygon9.add(new LatLng(34.811780, 135.561250));
        polygon9.add(new LatLng(34.811700, 135.561300));
        polygon9.add(new LatLng(34.811700, 135.561200));

        Polygon polygon9Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon9)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon9Obj);
        polygonCapturedStatus.put(polygon9Obj, false);

        // 新しいポリゴン（10～12）をH棟周辺に追加
        List<LatLng> polygon10 = new ArrayList<>();
        polygon10.add(new LatLng(34.809000, 135.562000));
        polygon10.add(new LatLng(34.808920, 135.562050));
        polygon10.add(new LatLng(34.808920, 135.561950));

        Polygon polygon10Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon10)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon10Obj);
        polygonCapturedStatus.put(polygon10Obj, false);

        List<LatLng> polygon11 = new ArrayList<>();
        polygon11.add(new LatLng(34.808250, 135.561980)); // 頂点1
        polygon11.add(new LatLng(34.808170, 135.562030)); // 頂点2
        polygon11.add(new LatLng(34.808170, 135.561930)); // 頂点3

        Polygon polygon11Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon11)
                .fillColor(0x55FF0000) // 半透明の赤色
                .strokeColor(0xFFFF0000) // 赤色の枠線
                .strokeWidth(5));

        polygons.add(polygon11Obj);
        polygonCapturedStatus.put(polygon11Obj, false);

        List<LatLng> polygon12 = new ArrayList<>();
        polygon12.add(new LatLng(34.808000, 135.561500));
        polygon12.add(new LatLng(34.807920, 135.561550));
        polygon12.add(new LatLng(34.807920, 135.561450));

        Polygon polygon12Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon12)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon12Obj);
        polygonCapturedStatus.put(polygon12Obj, false);

        // 新しいポリゴン（13～15）を廊下に追加
        List<LatLng> polygon13 = new ArrayList<>();
        polygon13.add(new LatLng(34.810220, 135.560650));
        polygon13.add(new LatLng(34.810140, 135.560700));
        polygon13.add(new LatLng(34.810140, 135.560600));

        Polygon polygon13Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon13)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon13Obj);
        polygonCapturedStatus.put(polygon13Obj, false);

        List<LatLng> polygon14 = new ArrayList<>();
        polygon14.add(new LatLng(34.809700, 135.560900));
        polygon14.add(new LatLng(34.809620, 135.560950));
        polygon14.add(new LatLng(34.809620, 135.560850));

        Polygon polygon14Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon14)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon14Obj);
        polygonCapturedStatus.put(polygon14Obj, false);

        List<LatLng> polygon15 = new ArrayList<>();
        polygon15.add(new LatLng(34.809800, 135.561900));
        polygon15.add(new LatLng(34.809720, 135.561950));
        polygon15.add(new LatLng(34.809720, 135.561850));

        Polygon polygon15Obj = mMap.addPolygon(new PolygonOptions()
                .addAll(polygon15)
                .fillColor(0x55FF0000)
                .strokeColor(0xFFFF0000)
                .strokeWidth(5));

        polygons.add(polygon15Obj);
        polygonCapturedStatus.put(polygon15Obj, false);
    }

    private void checkPlayerInPolygons(LatLng playerLocation) {
        for (Polygon polygon : polygons) {
            if (!polygonCapturedStatus.get(polygon)) {
                // プレイヤーがポリゴン内にいるか判定
                if (PolyUtil.containsLocation(playerLocation, polygon.getPoints(), false)) {
                    // 奪取済みに変更
                    polygon.setFillColor(0x5500FF00); // 半透明の緑色
                    polygon.setStrokeColor(0xFF00FF00); // 緑色の枠線
                    polygonCapturedStatus.put(polygon, true);

                    // 点数を加算
                    score += 10;

                    // UIを更新
                    scoreText.setText("Score: " + score);
                }
            }
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }
}