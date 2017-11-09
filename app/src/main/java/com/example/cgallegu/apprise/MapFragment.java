package com.example.cgallegu.apprise;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    public static final String EXTRA_MESSAGE = "com.example.cgallegu.apprise.MESSAGE";
    public static final String DESCRIPTION = "com.example.cgallegu.apprise.MESSAGE";
    private String kmlname;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        kmlname = getArguments().getString("NAME");
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    private void moveCameraToKml(KmlLayer kmlLayer, GoogleMap mMap) {
        //Retrieve the first container in the KML layer
        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        KmlPlacemark placemark;
        KmlPolygon polygon;
        int cont = 1;
        Iterator itr = container.getPlacemarks().iterator();
        while(true){
            placemark = (KmlPlacemark) itr.next();
            try {
                polygon = (KmlPolygon) placemark.getGeometry();
                break;
            }
            catch (Exception e){

            }

        }
        //Retrieve a polygon object in a placemark
        //Create LatLngBounds of the outer coordinates of the polygon
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;


        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 1));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        /*if( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }*/

        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap = googleMap;

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        KmlLayer kmlLayer = null;
        try {

            //kmlLayer = new KmlLayer(mMap, R.raw.campus, getActivity().getApplicationContext());
            String path2 = "data/data/com.example.cgallegu.apprise/files/ExtractLoc";
            String kml_route = path2 + "/" + kmlname;
            FileInputStream stream = new FileInputStream(kml_route);
            kmlLayer = new KmlLayer(mMap, stream, getActivity().getApplicationContext());

            kmlLayer.addLayerToMap();
            //kmlLayer.getContainerFeature("document").getProperty("name");
            moveCameraToKml(kmlLayer,mMap);

            kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {
                    Intent intent = new Intent(getActivity(), KmlInfoActivity.class);
                    Bundle characteristics = new Bundle();
                    String message = feature.getProperty("name");
                    String message2 = feature.getProperty("description");
                    characteristics.putString("EXTRA_MESSAGE", message);
                    characteristics.putString("DESCRIPTION", message2);
                    intent.putExtras(characteristics);

                    if(message2 != null){
                        startActivity(intent);
                    }
                    //intent.putExtra(EXTRA_DESCRIPTION, kmlLayer.getContainerFeature("document").getProperty("name"));

                }
            });
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
