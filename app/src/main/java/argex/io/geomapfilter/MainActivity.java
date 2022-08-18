package argex.io.geomapfilter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.fonfon.geohash.GeoHash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import argex.io.geomapfilter.databinding.ActivityMainBinding;

@SuppressLint({"DefaultLocale", "SetTextI18n"})
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
	private GoogleMap googleMap;

	private List<Location> raw_list = new ArrayList<>();
	private BroadcastReceiver broadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Location location = intent.getParcelableExtra(PositionService.POSSER_DATA);
			raw_list.add(location);
			Update();
		}
	};

	int tolerance = 8;
	private void Update() {
		if(googleMap == null) {
			return;
		}
		List<Location> filter_list = new ArrayList<>();
		for(int i = 0; i < raw_list.size(); i++) {
			Location location = raw_list.get(i);
			String p0 = GeoHash.fromLocation(location, tolerance).toString();

			int size = filter_list.size();
			if(size == 0) {
				filter_list.add(location);
			} else if(size == 1) {
				String p1 = GeoHash.fromLocation(filter_list.get(size - 1), tolerance).toString();
				if (p0.equals(p1)) {
					filter_list.add(location);
				}
			} else  {
				String p1 = GeoHash.fromLocation(filter_list.get(size - 1), tolerance).toString();
				String p2 = GeoHash.fromLocation(filter_list.get(size - 2), tolerance).toString();
				if (p0.equals(p1) && p0.equals(p2)) {
					filter_list.add(location);
				}
			}
		}

		PolylineOptions wrong_line = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
		for(int i = 0; i < raw_list.size(); i++) {
			Location location = raw_list.get(i);
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			wrong_line.add(latLng);
		}

		PolylineOptions corrent_line = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
		for(int i = 0; i < filter_list.size(); i++) {
			Location location = filter_list.get(i);
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			corrent_line.add(latLng);
		}
		binding.control.setText(String.format("Raw:%d - Filter:%d", raw_list.size(), filter_list.size()));

		googleMap.clear();
		googleMap.addPolyline(wrong_line);
		if(binding.isFilter.isChecked()) {
			googleMap.addPolyline(corrent_line);
		}
		if(filter_list.size() != 0) {
			Location location = filter_list.get(filter_list.size() - 1);
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Cur");
			googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
			googleMap.addMarker(markerOptions);
		}
	}

	private ActivityMainBinding binding;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


		PositionService.CheckPermission(this);

		binding.decrement.setOnClickListener(this);
		binding.increment.setOnClickListener(this);
		binding.tolerance.setText(Integer.toString(tolerance));
		binding.isFilter.setOnCheckedChangeListener(this);
		binding.isFilter.setChecked(true);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, PositionService.class);
		startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(PositionService.POSSER_LOCATION);
		registerReceiver(broadcast, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcast);
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		this.googleMap = googleMap;
		// --
	}


	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.increment) {
			tolerance++;
		} else if(view.getId() == R.id.decrement) {
			tolerance--;
		}
		if(tolerance < 0) {
			tolerance = 1;
		} else if(tolerance > 22) {
			tolerance = 22;
		}
		binding.tolerance.setText(Integer.toString(tolerance));
		Update();
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		Update();
	}
}
