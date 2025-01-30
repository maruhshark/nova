package jp.ac.ritsumei.ise.phy.exp2.is0691ve.locationgame;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.OnNmeaMessageListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.ParcelFileDescriptor;
import java.io.FileInputStream;
import java.io.FileDescriptor;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "jp.ac.ritsumei.ise.phy.exp2.is0691ve.USB_PERMISSION";
    private UsbManager usbManager;
    private UsbDevice gnssDevice;
    private Button map_view;
    private boolean isReceiving = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        detectGnssDevice();

        map_view = findViewById(R.id.map_view);
        map_view.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }

    private void detectGnssDevice() {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            if (isGnssDevice(device)) {
                gnssDevice = device;
                requestUsbPermission();
                return;
            }
        }
        Toast.makeText(this, "GNSSデバイスが見つかりません", Toast.LENGTH_LONG).show();
    }

    private boolean isGnssDevice(UsbDevice device) {
        return device.getVendorId() == 1234 && device.getProductId() == 5678;
    }

    private void requestUsbPermission() {
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        usbManager.requestPermission(gnssDevice, permissionIntent);
    }

    private void startReceivingNmeaData() {
        new Thread(() -> {
            try {
                UsbDeviceConnection connection = usbManager.openDevice(gnssDevice);
                if (connection == null) {
                    Log.e("USB", "デバイス接続に失敗しました");
                    return;
                }

                // getFileDescriptor() で取得した int を ParcelFileDescriptor に変換
                ParcelFileDescriptor pfd = ParcelFileDescriptor.adoptFd(connection.getFileDescriptor());
                FileDescriptor fd = pfd.getFileDescriptor();

                if (fd == null) {
                    Log.e("USB", "FileDescriptorを取得できませんでした");
                    return;
                }

                InputStream inputStream = new FileInputStream(fd);
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder nmeaData = new StringBuilder();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    nmeaData.append(new String(buffer, 0, bytesRead));
                    processNmeaData(nmeaData.toString());
                    nmeaData.setLength(0);
                }

                inputStream.close();
                pfd.close(); // ParcelFileDescriptor を閉じる
                connection.close();
            } catch (Exception e) {
                Log.e("USB", "NMEAデータの受信エラー", e);
            }
        }).start();
    }

    private void processNmeaData(String nmeaSentence) {
        double[] latLng = parseGPGGA(nmeaSentence);
        if (latLng != null) {
            double latitude = latLng[0];
            double longitude = latLng[1];

            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Nullable
    private double[] parseGPGGA(String nmeaSentence) {
        Pattern pattern = Pattern.compile("\\$GPGGA,\\d+\\.\\d+,([0-9]+\\.?[0-9]*),([NS]),([0-9]+\\.?[0-9]*),([EW]),.*");
        Matcher matcher = pattern.matcher(nmeaSentence);
        if (matcher.find()) {
            double latitude = convertToDecimalDegrees(matcher.group(1), matcher.group(2));
            double longitude = convertToDecimalDegrees(matcher.group(3), matcher.group(4));
            return new double[]{latitude, longitude};
        }
        return null;
    }

    private double convertToDecimalDegrees(String coordinate, String direction) {
        double value = Double.parseDouble(coordinate);
        int degrees = (int) (value / 100);
        double minutes = value % 100;
        double decimalDegrees = degrees + (minutes / 60.0);
        if (direction.equals("S") || direction.equals("W")) {
            decimalDegrees *= -1;
        }
        return decimalDegrees;
    }
}