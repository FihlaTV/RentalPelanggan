package com.example.meita.rentalpelanggan.PetaRental;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.MenuPencarian.RentalModel;
import com.example.meita.rentalpelanggan.MenuProfilRental.ProfilRental;
import com.example.meita.rentalpelanggan.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PetaRental extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GeoQueryEventListener,
        GoogleMap.OnCameraChangeListener {
    Double nearByDistanceRadius;
    Double latitude, longitude;
    String idRental;

    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "LokasiRentalActivity";
    private DatabaseReference mDatabase;
    GeoQuery geoQuery;
    GeoFire geoFire;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermissionGranted;
    CameraPosition cameraPosition;
    GoogleMap mGoogleMap;
    LatLng latLng;
    private Circle searchCircle;
    private Map<String, Marker> markers;
    private HashMap<Marker, RentalModel> markerRental;

    private LatLng mDefaultLocation = new LatLng(-7.982630999999983, 112.63088100000004);

    RelativeLayout myLocationButton;
    TextView textViewKeteranganRental;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_rental);

        nearByDistanceRadius = getIntent().getDoubleExtra("radius", 0);

        myLocationButton = (RelativeLayout) findViewById(R.id.myLocationButton);
        textViewKeteranganRental = (TextView) findViewById(R.id.textViewKeterangan);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Map Initialization
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // setup markers
        this.markers = new HashMap<>();
        this.markerRental = new HashMap<>();

        cekGPS();

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PetaRental.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(PetaRental.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PetaRental.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                    return;
                } else {
                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        latitude = mLastLocation.getLatitude();
                        longitude = mLastLocation.getLongitude();
                        latLng = new LatLng(latitude, longitude);
                        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .snippet("Lokasi Anda Sekarang")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Lokasi Anda"));

                    }
                }
            }
        });

        //Geofire
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("geofire"));
        setGeofire(geoFire);
        try {
            geoQuery = getGeofire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), nearByDistanceRadius);
            geoQuery.addGeoQueryEventListener(PetaRental.this);
            //LogUtils.i("geo status: geo queery started in oncreate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cekGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PetaRental.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                return;
            } else {
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null){
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    latLng = new LatLng(latitude, longitude);
                    cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .snippet("Lokasi Anda Sekarang")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Lokasi Anda"));

                }
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        if (location != null){
            textViewKeteranganRental.setVisibility(View.GONE);

            //add marker
            final Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps)).flat(true))
                    ;
            marker.setFlat(true);
            marker.setAnchor(0.5f, 0.5f);
            this.markers.put(key, marker);

            mDatabase.child("geofire").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        RentalModel rentalModel = userSnapshot.getValue(RentalModel.class);

                        markerRental.put(marker,rentalModel);
                        Log.d(TAG, "data" + rentalModel);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Log.d(TAG, "data" + markerRental);
        }

    }

    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            // if (markers.size() == 0) {
            //Car not shown change it to NO CARS AVAILABLE
//                setMlijo.setText("NO CARS AVAILABLE");
//                setMlijo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            //   }
            marker.remove();
            this.markers.remove(key);
            //  LogUtils.i("Check marker is empty" + markers.isEmpty());
        }
        textViewKeteranganRental.setVisibility(View.VISIBLE);
        textViewKeteranganRental.setText("Tidak Ada Rental Kendaraan disekitar lokasi Anda");
        textViewKeteranganRental.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        Marker marker = this.markers.get(key);
        if (marker != null){
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("geofire"));
            setGeofire(geoFire);
            geoQuery = getGeofire().queryAtLocation(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()), nearByDistanceRadius);
            if (this.geoQuery != null){
                this.geoQuery.setCenter(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                //dynamic radius
                this.geoQuery.setRadius(nearByDistanceRadius);
                this.geoQuery.addGeoQueryEventListener(PetaRental.this);
            }else {
                Toast.makeText(PetaRental.this, "geoquery null", Toast.LENGTH_SHORT).show();
            }


            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            latLng = new LatLng(latitude, longitude);
            cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/1000);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final RentalModel rentalModel = this.markerRental.get(marker);//model gak dapet
        final Dialog dialog = new Dialog(this);
        Log.d(TAG, "data" + markerRental);
        dialog.setContentView(R.layout.custom_marker_rental);
        idRental = rentalModel.getIdRental();
        TextView namaRental = (TextView) dialog.findViewById(R.id.textViewNamaRental);
        TextView alamatRental = (TextView) dialog.findViewById(R.id.textViewAlamatRental);
        TextView nmrTelpon = (TextView) dialog.findViewById(R.id.textViewNomorTelepon);
        Button detail = (Button) dialog.findViewById(R.id.btnLihatDetail);
        namaRental.setText(rentalModel.getNama_rental());
        alamatRental.setText(rentalModel.getAlamat_rental());
        nmrTelpon.setText(rentalModel.getNotelfon_rental());

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetaRental.this, ProfilRental.class);
                intent.putExtra("idRental", idRental);
                startActivity(intent);
            }
        });

        dialog.show();

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;  // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng latLng1 = mGoogleMap.getCameraPosition().target;
                latitude = latLng1.latitude;
                longitude = latLng1.longitude;
            }
        });

        this.searchCircle = this.mGoogleMap.addCircle(new CircleOptions().center(mDefaultLocation).radius(1000));
        this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));

        this.mGoogleMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public GeoFire getGeofire() {
        return geoFire;
    }

    public void setGeofire(GeoFire geofire) {
        this.geoFire = geofire;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    // Animation handler for old APIs without animation support
    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }
}
