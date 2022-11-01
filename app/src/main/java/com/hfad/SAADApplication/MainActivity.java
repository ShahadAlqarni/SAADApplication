package com.hfad.SAADApplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    CheckBox family;
    CheckBox red;
    CheckBox police;
    String email, id, message, R_number;
    FirebaseAuth firebaseAuth;

    static MainActivity INSTANCE;

    //location
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private boolean firstTimeFlag = true;

    private final View.OnClickListener clickListener = view -> {
        if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null)
            animateCamera(currentLocation);
    };

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            showMarker(currentLocation);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        INSTANCE = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        Intent intent3 = getIntent();
        email = intent3.getStringExtra("email");
        id = intent3.getStringExtra("userID");
        //R_number = intent3.getStringExtra("r_number");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("Drivers");
        Log.v("USERID", userRef.getKey());


        //send data to relevent
        firebaseAuth = FirebaseAuth.getInstance();
        Query userQuery = userRef.orderByChild("email").equalTo(email);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String driverName = "" + ds.child("driverFullName").getValue();
                    String bloodType = "" + ds.child("driver_BloodType").getValue();
                    String phoneNumber = "" + ds.child("driver_PhoneNumber").getValue();
                    String carColore = "" + ds.child("carColorDriver").getValue();
                    String carCompany = "" + ds.child("carCompanyDriver").getValue();
                    String carId = "" + ds.child("carIDDriver").getValue();
                    String illness = "" + ds.child("driver_Illness").getValue();
                     R_number = "" + ds.child("relativeNumberDB").getValue(String.class).trim();

                    message = "Driver name is " + driverName + "\n driver blood Type is " + bloodType + "\n driver phone Number is " + phoneNumber
                            + "\n driver car Colore is " + carColore + "\n driver car Company is " + carCompany + "\n driver car Id is " + carId
                            + "\n driver have illness " + illness;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //connect with sensor
        //method check all time
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase.getReference();
        HashMap<String, Object> restValue = new HashMap<>();
        reference.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String crasgFornt = "" + snapshot.child("crash_front").getValue();
                String crashBack = "" + snapshot.child("crash_back").getValue();
                String fliped = "" + snapshot.child("car_fliped").getValue();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (crasgFornt.equals("1") || crashBack.equals("1") || fliped.equals("1")) {
                        btn_notice();

                        //change value to zero
                        restValue.put("crash_front", "0");
                        restValue.put("crash_back", "0");
                        restValue.put("car_fliped", "0");
                        reference.child("test").updateChildren(restValue);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent home = new Intent(MainActivity.this, MainActivity.class);
                        home.putExtra("email", email);
                        home.putExtra("userID", id);
                        home.putExtra("R_number", R_number);
                        Log.v("DATA", id);
                        startActivity(home);
                        break;
                    case R.id.nav_update:
                        Intent update = new Intent(getApplicationContext(), updateProfile.class);
                        update.putExtra("email", email);
                        update.putExtra("userID", id);
                        update.putExtra("R_number", R_number);
                        Log.v("DATA", id);
                        startActivity(update);
                        break;
                    case R.id.nav_about:
                        Intent about = new Intent(MainActivity.this, aboutApp.class);
                        about.putExtra("email", email);
                        about.putExtra("userID", id);
                        about.putExtra("R_number", R_number);
                        Log.v("DATA", id);
                        startActivity(about);
                        break;
                    case R.id.nav_logOut:
                        LogOutDr();
                        break;
                }
                return false;
            }
        });
        family = findViewById(R.id.family);
        red = findViewById(R.id.red);
        police = findViewById(R.id.police);

        //CheckBox working
        Button btn_save = findViewById(R.id.button_send);
        btn_save.setOnClickListener(view -> {
            if (family.isChecked()) {
                sendToFamily();
            }
            if (red.isChecked() || police.isChecked()) {
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(MainActivity.this, "connot send", Toast.LENGTH_LONG).show();
                } else {
                    String red1 = "false", police1 = "false";
                    if (red.isChecked()) {
                        red1 = "true";
                    }
                    if (police.isChecked()) {
                        police1 = "true";
                    }
                    String url = "http://maps.google.com/maps?\nsaddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                    send_data(message, red1, police1, url);
                }
            }
        });

        //location
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        supportMapFragment.getMapAsync(this);
        findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);
    }

    private void send_data(String message, String red, String police, String url) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(id, new db_send(red, message, police, id, url));
        databaseReference.child("new_alert").updateChildren(hashMap);
        databaseReference.child("all_alert").updateChildren(hashMap);

    }

    private void checkUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String idb = user.getUid();
        } else {
        }
    }

    @Override
    protected void onStart() {
        checkUser();
        super.onStart();
    }

    public void LogOutDr() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle("LogOut");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Close!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,startPage.class));
                System.exit(0);                finish();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();

    }

    ///
    public void btn_notice() {
        String idNotice = "my_channel_id_01";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(idNotice);
            if (channel == null) {
                channel = new NotificationChannel(idNotice, "Channel Title", NotificationManager.IMPORTANCE_HIGH);

                //config notification channel
                channel.setDescription("[Channel description]");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("email", email);
        notificationIntent.putExtra("userID", id);
        notificationIntent.putExtra("R_number",R_number);
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, idNotice)
                .setSmallIcon(R.drawable.saad)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.saad))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.saad))
                        .bigLargeIcon(null))
                .setContentTitle("Do you need help?")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false)//true touch on notification menu dismissed, but swipe to dismiss
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());

        //id to generate new notification in list notification menu
        m.notify(1, builder.build());
        double location = currentLocation.getLatitude();
        double location2 = currentLocation.getLongitude();
        String locationS = String.valueOf(location);
        String location2s = String.valueOf((double) location2);
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.putExtra("Latitude", locationS);
        intent.putExtra("Longitude", location2s);
        intent.putExtra("R_number", R_number);
        intent.putExtra("email", email);
        intent.putExtra("userID", id);
        startActivity(intent); //this has to be after the putExtra call
    }

    //location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null)
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng));
        else
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        googleMap = null;
    }

    // send to Family
    public void sendToFamily() {
        String uri = "http://maps.google.com/maps?saddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("I had an accident please help me. \n" +
                "My location is: \n ");
        smsBody.append(Uri.parse(uri));
        smsManager.sendTextMessage("+966-"+R_number, null, smsBody.toString(), null, null);
    }

    public String getData() {
        return this.id;
    }
}

