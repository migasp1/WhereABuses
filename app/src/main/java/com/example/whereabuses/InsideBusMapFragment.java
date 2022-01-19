package com.example.whereabuses;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utils.MapUtils;

//Inside fragment container
public class InsideBusMapFragment extends Fragment {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    String provider;
    private final long MIN_TIME = 1000;
    private final float MIN_DISTANCE = 0.5F;
    double lat1;
    double lon1;
    private Location myLocation;
    private LatLng latLng;
    public ArrayList<MarkerOptions> locationArrayList = new ArrayList<MarkerOptions>();
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    int counter = 0;


    public void setMarkerOnMap(int input){
        InsideBusActivity activity = (InsideBusActivity) getActivity();
        String carreira2 = activity.carreira;
        DocumentReference documentReference = rootRef.collection(carreira2).document("Markers");
        BitmapDescriptor slowTraffic =BitmapDescriptorFactory.fromBitmap(MapUtils.getSlowTrafficIcon(getContext()));
        BitmapDescriptor crash =BitmapDescriptorFactory.fromBitmap(MapUtils.getAccidentIcon(getContext()));
        switch (input) {
            case 0:
                if(latLng != null) {

                    MarkerOptions mo = new MarkerOptions().title("Acidente").position(latLng).icon(crash);
                    GeoPoint markerlocal = new GeoPoint(latLng.latitude, latLng.longitude);
                    Map<String, Map.Entry<String, GeoPoint>> actionMap = new HashMap<String, Map.Entry<String, GeoPoint>>();
                    Map.Entry<String, GeoPoint> entry =
                            new AbstractMap.SimpleEntry<String, GeoPoint>("Acidente",markerlocal);

                    actionMap.put("Marker" + counter ,entry);
                    mMap.addMarker(mo);
                    documentReference.set(actionMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {

                            counter++;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
                break;
            case 1:
                if(latLng != null) {
                    MarkerOptions mo = new MarkerOptions().title("Trânsito").position(latLng).icon(slowTraffic);
                    mMap.addMarker(mo);
                    GeoPoint markerlocal = new GeoPoint(latLng.latitude, latLng.longitude);
                    Map<String, Map.Entry<String, GeoPoint>> actionMap = new HashMap<String, Map.Entry<String, GeoPoint>>();
                    Map.Entry<String, GeoPoint> entry =
                            new AbstractMap.SimpleEntry<String, GeoPoint>("Trânsito",markerlocal);

                    actionMap.put("Marker" + counter ,entry);

                    mMap.addMarker(mo);


                    documentReference.set(actionMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            //System.out.println("SUCESSO A ENVIAR PARA FIREBASE");
                            counter++;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //System.out.println("PROBLEMA A ENVIAR PARA FIREBASE");
                        }
                    });
                }
                break;
        }
    }

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
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
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
            myLocation = lm.getLastKnownLocation(provider);


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
        }
    };
}