package com.example.whereabuses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Utils.AnimationUtils;
import Utils.MapUtils;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private LatLng latLng;
    private Marker movingBusMarker;
    private LatLng previousLatLng;
    private LatLng currentLatLng;
    int index = 0;
    private Handler handler;
    private Handler handler2;
    String provider;
    private final long MIN_TIME = 1000;
    private final float MIN_DISTANCE = 0.5F;
    double lat1;
    double lon1;
    Dialog materialDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng startLocation = new LatLng(32.7502, 114.7655);
            mMap.setIndoorEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 1F));

            locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }

            };
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);

            if (!mMap.isMyLocationEnabled())
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            provider = lm.getBestProvider(criteria, true);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location myLocation = lm.getLastKnownLocation(provider);


            if (myLocation == null) {

                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                provider = lm.getBestProvider(criteria, false);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                lat1 = myLocation.getLatitude();
                lon1 = myLocation.getLongitude();



                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18), 1500, null);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location myLocation) {

                        double latitude = myLocation.getLatitude();

                        double longitude = myLocation.getLongitude();

                        LatLng latLng = new LatLng(latitude, longitude);


                    }
                });
            }


            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    showMovingBus();
                    getMarkers();
                }
            }, 3000);

            mMap.getUiSettings().setZoomControlsEnabled(true);


            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if(marker.equals(movingBusMarker)) {
                        GoogleMapsActivity activity = (GoogleMapsActivity) getActivity();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Autocarro " + activity.getCarreira());
                        builder.setItems(new CharSequence[]{ "Lotação Máxima: 71"}, null)
                                .setPositiveButton("Ir para a sala de chat", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(activity.getApplicationContext(),ChatActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setIcon(R.drawable.busicon);
                        builder.create().show();
                    }
                    return true;
                }
            });
        }

    };

    volatile boolean shutdown = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler2.removeCallbacks(null);
        handler2 = null;
        handler.removeCallbacks(null);
        handler = null;

    }

    private void showMovingBus() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        GoogleMapsActivity activity = (GoogleMapsActivity) getActivity();

        //DocumentReference documentReference = rootRef.collection(activity.getCarreira()).document("Bus1");
        handler = new Handler(Looper.getMainLooper());
        Runnable runnable =
                new Runnable() {
                    public void run() {
                        try {
                            rootRef.collection(activity.getCarreira()).document("Bus1").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        if (documentSnapshot.getData() != null) {
                                            GeoPoint geoPoint = (GeoPoint) documentSnapshot.get("local2");
                                            LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                                            updateCarLocation(location);
                                        } else {

                                        }
                                    }

                                }
                            });

                            handler.postDelayed(this, 5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
        handler.postDelayed(runnable, 1000);

    }

    private void setMarker(LatLng latLngM, String type) {
        BitmapDescriptor slowTraffic = null;
        BitmapDescriptor crash = null;
        try {
            slowTraffic = BitmapDescriptorFactory.fromBitmap(MapUtils.getSlowTrafficIcon(getContext()));
            crash = BitmapDescriptorFactory.fromBitmap(MapUtils.getAccidentIcon(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (slowTraffic == null || crash == null) {
            return;
        } else {
            switch (type) {
                case "Acidente":
                    if (latLngM != null) {

                        MarkerOptions mo = new MarkerOptions().title(type).position(latLngM).icon(crash);
                        mMap.addMarker(mo);
                    }
                    break;
                case "Trânsito":
                    if (latLngM != null) {
                        MarkerOptions mo = new MarkerOptions().title(type).position(latLngM).icon(slowTraffic);
                        mMap.addMarker(mo);

                        break;
                    }
            }
        }

    }

    private void getMarkers() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        GoogleMapsActivity activity = (GoogleMapsActivity) getActivity();
        handler2 = new Handler(Looper.getMainLooper());
        Runnable runnable =
                new Runnable() {
                    public void run() {
                        try {
                            rootRef.collection(activity.getCarreira()).document("Markers").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        if (documentSnapshot.getData() != null) {

                                            Map<String, Object> map = documentSnapshot.getData();

                                            Iterator<Map.Entry<String, Object>> itr = map.entrySet().iterator();
                                            while (itr.hasNext()) {
                                                HashMap<String, Map.Entry<String, GeoPoint>> entry = (HashMap<String, Map.Entry<String, GeoPoint>>) itr.next().getValue();

                                                Object objeto = entry.get("key");
                                                String key = (String) objeto;
                                                GeoPoint tab = (GeoPoint) entry.get("value");

                                                LatLng latLngM = new LatLng(tab.getLatitude(), tab.getLongitude());
                                                setMarker(latLngM, key);
                                            }

                                        } else {
                                        }
                                    }

                                }
                            });

                            handler2.postDelayed(this, 5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
        handler2.postDelayed(runnable, 1000);
    }

    private void updateCarLocation(LatLng latLngCar) {
        if (movingBusMarker == null) {
            movingBusMarker = addCarMarkerAndGet(latLngCar);
        }
        if (previousLatLng == null) {
            currentLatLng = latLngCar;
            previousLatLng = currentLatLng;
            movingBusMarker.setPosition(currentLatLng);
            movingBusMarker.setAnchor(0.5f, 0.5f);

        } else {
            previousLatLng = currentLatLng;
            currentLatLng = latLngCar;
            ValueAnimator valueAnimator = AnimationUtils.carAnimator();
            valueAnimator.addUpdateListener(valueAnimator1 -> {
                if (currentLatLng != null && previousLatLng != null) {
                    Float multiplier = valueAnimator1.getAnimatedFraction();
                    LatLng nextLocation = new LatLng(
                            multiplier * currentLatLng.latitude + (1 - multiplier) * previousLatLng.latitude,
                            multiplier * currentLatLng.longitude + (1 - multiplier) * previousLatLng.longitude
                    );
                    movingBusMarker.setPosition(nextLocation);
                    movingBusMarker.setAnchor(0.5f, 0.5f);
                }

            });
            valueAnimator.start();
        }

    }

    private Marker addCarMarkerAndGet(LatLng latLngCar) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getBusIcon(getContext()));
        return mMap.addMarker(new MarkerOptions().position(latLngCar).flat(true).icon(bitmapDescriptor));
    }


}