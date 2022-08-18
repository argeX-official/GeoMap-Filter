package argex.io.geomapfilter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.List;

@SuppressLint("MissingPermission")
public class PositionService extends Service implements LocationListener {
	public static final String POSSER_LOCATION = "argex.io.gpumapfilter.POSSER_LOCATION";
	public static final String POSSER_DATA = "argex.io.gpumapfilter.POSSER_DATA";

	private static final String TAG = PositionService.class.getSimpleName();
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 4; // 100 msec
	private LocationManager locationManager;
	private Location location;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.w(TAG, "Call:: onStartCommand");
		try {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Log.w(TAG, "Call:: is_gps_enabled");
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				onLocationChanged(location);
				return START_STICKY;
			} else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				Log.w(TAG, "Call:: is_network_enabled");
				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				onLocationChanged(location);
				return START_STICKY;
			}
			Log.w(TAG,"Call: not defined");
		} catch (Exception e) {
			Log.w(TAG, "Exception:: " + e.getMessage());
			e.printStackTrace();
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.w(TAG, "Call:: onDestroy");
		super.onDestroy();
		locationManager.removeUpdates(this);
	}


	@Override
	public void onLocationChanged(@NonNull Location location) {
		this.location = location;
		Log.w(TAG, String.format("Location: %.4f %.4f - %.4f",
				location.getLongitude(), location.getLatitude(), location.getAccuracy()));
		SendBroadcast(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void SendBroadcast(Location location) {
		Intent intent = new Intent();
		intent.setAction(POSSER_LOCATION);
		intent.putExtra(POSSER_DATA, location);
		sendBroadcast(intent);
	}


	public static boolean CheckPermission(Activity activity) {
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
			ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			//return TODO;
			return true;
		}
		return false;
	}

	private static void showGPSSetting(Activity activity) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		activity.startActivity(intent);
	}


}
