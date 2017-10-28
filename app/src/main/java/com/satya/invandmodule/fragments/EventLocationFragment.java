package com.satya.invandmodule.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.satya.invandmodule.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventLocationFragment extends ValidatesToMove implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public GoogleMap mMap;
    public static final int REQUEST_LOCATION = 100;
    public LocationManager manager;
    public Criteria criteria;
    public Location location;
    public String provider;
    public MarkerOptions markerOptions;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG_ADDRESS = "results";

    JSONArray user = null;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public Button button;
    PlaceAutocompleteFragment autocompleteFragment;

//    public FusedLocationProviderClient mFusedLocationProviderClient


    public EventLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_location, container, false);
//        button = (Button)view.findViewById(R.id.button);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);


        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search your Place Here .");
        ImageView searchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_location_on_gary_24dp));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                showLocation(place);
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        return view;
    }

    private void showLocation(Place place) {

        LatLng latLng = place.getLatLng();
        if (markerOptions == null) {
            markerOptions = new MarkerOptions();
        }
        markerOptions.position(latLng);
//        markerOptions.title((String)place.getName());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        autocompleteFragment.setText((String) place.getAddress());
        markerOptions.title((String) place.getAddress());

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.f));


    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        settingsrequest();

    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


//                    settingsrequest();
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(getContext(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
//                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }

            }
            case REQUEST_CHECK_SETTINGS:
                switch (requestCode) {
                    case Activity.RESULT_OK:
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
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
                    case Activity.RESULT_CANCELED:
                        settingsrequest();
                        break;

                }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION )
                    == PackageManager.PERMISSION_GRANTED ) {
                //Location Permission already granted
                if (mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                mMap.setMyLocationEnabled(true);

//                setMap();
            } else {
                //Request Location Permission
                checkPermissions();
            }
        }
        else {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            mMap.setMyLocationEnabled(true);

        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(markerOptions == null){
                    markerOptions = new MarkerOptions();
                }
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                mCurrLocationMarker = mMap.addMarker(markerOptions);
                showAddress(latLng);
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.0f));

            }
        });


    }
    public void showAddress(LatLng latLng){
        JsonParse parse = new JsonParse();
        parse.latLng = latLng;
        parse.execute();
//        Geocoder geocoder;
//        List<Address> addresses ;
//        geocoder = new Geocoder(getContext(), Locale.getDefault());
//        try {
//                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//                if (knownName != null && knownName.length() > 0) {
//                    autocompleteFragment.setText((String) address);
//                    markerOptions.title((String) address);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


    }



    public void  checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();

            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval();
//        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "onConnectionSuspended ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "onConnectionFailed ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(markerOptions == null){
             markerOptions = new MarkerOptions();
        }
        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        showAddress(latLng);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.0f));

    }

    @Override
    public boolean isShowNext() {
        return false;
    }

    @Override
    public boolean isShowBack() {
        return false;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }


    public class JsonParse  extends AsyncTask<String, String, JSONObject> {
        LatLng latLng ;
        private Exception exception;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        protected JSONObject doInBackground(String... urls) {
            JSONObject json = null;
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latLng.latitude+","+latLng.longitude+"&sensor=true%22";
            JsonParser jParser = new JsonParser();

                // Getting JSON from URL
            try {
                 json = jParser.getJSONFromUrl(url);
                return json;
            }catch (Exception e){
                e.printStackTrace();
            }


            return json;
        }

        protected void onPostExecute(JSONObject json) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (json != null) {
                try {
                    // Getting JSON Array
                    user = json.getJSONArray(TAG_ADDRESS);
                    if (user != null) {
                        JSONObject c = user.getJSONObject(0);

                        if (c != null) {
                            String address = c.getString("formatted_address");
//                Toast.makeText(getContext(),address,Toast.LENGTH_SHORT).show();
                            autocompleteFragment.setText((String) address);
                            markerOptions.title((String) address);

                        }
                    }

                    // Storing  JSON item in a Variable


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class JsonParser{
        public JSONObject getJSONFromUrl(String strings) {
            JSONObject topLevel = null;
            // Making HTTP request
            try {
                URL url = new URL(strings);
                // defaultHttpClient
                HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(httpClient.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                 topLevel = new JSONObject(builder.toString());
//                 main = topLevel.getJSONObject("results");

                httpClient.disconnect();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            // return JSON String
            return topLevel;

        }
    }


}
