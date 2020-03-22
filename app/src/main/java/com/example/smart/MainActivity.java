package com.example.smart;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Button h,p,d,f,e,g;
    double lat,lon;
    private static final String SMS_SENT_INTENT_FILTER = "com.smart.sms_send";
    private static final String SMS_DELIVERED_INTENT_FILTER = "com.smart.sms_delivered";
    int PERMISSION_ID = 44;
    DatabaseReference databaselocation;
    String n,value;
    Thread t=null;
    public String mesg,mesg1,mesg2,mesg3;
    private AdView mAdView;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h=(Button)findViewById(R.id.btnhosp);
        p=(Button)findViewById(R.id.btnpoc);
        d=(Button)findViewById(R.id.btndetails);
        f=(Button)findViewById(R.id.btnsta);
        e=(Button)findViewById(R.id.btnec);
        databaselocation= FirebaseDatabase.getInstance().getReference("location");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User/Emergency_Contact");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            private String TAG = "DB";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value == null){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contact();
                        }
                    },2000);
                }
                else {
                    contact();
                }


            }
        });


        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetails();
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findhospitals();
            }
        });


        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findpolice();
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findfire();
            }
        });


    }




    private void contact() {
        //n=details.e;




         mesg="Met with an accident at : "+"http://www.google.com/maps/place/"+lat+lon;
         mesg1="Hospitals near crash site "+"https://www.google.co.in/maps/search/hospitals/@"+lat+","+lon+","+"5z";
         mesg2="Polic Station near crash site "+"https://www.google.co.in/maps/search/police/@"+lat+","+lon+","+"5z";//+"Nearby Fire Station found at "+"https://www.google.co.in/maps/search/FireStation/@"+lat+","+lon+","+"5z";
         mesg3="Nearby Fire Station found at "+"https://www.google.co.in/maps/search/Fire+station/@"+lat+","+lon+","+"5z";
        Intent i=new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + value));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(i);
        //PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT_INTENT_FILTER), 0);
        //PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED_INTENT_FILTER), 0);

        //SmsManager shoms = SmsManager.getDefault();
        /*
        shoms.sendTextMessage(n, null, mesg, sentPI, deliveredPI);
        shoms.sendTextMessage(n, null, mesg1, sentPI, deliveredPI);
        shoms.sendTextMessage(n, null, mesg2, sentPI, deliveredPI);*/
        //shoms.sendTextMessage(n, null, mesg3, sentPI, deliveredPI);
       runthread3();
        runthread2();
        runthread1();
        runthread();


    }

    private void runthread3() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT_INTENT_FILTER), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED_INTENT_FILTER), 0);
                SmsManager shoms = SmsManager.getDefault();
                //shoms.sendTextMessage(value, null, mesg, sentPI, deliveredPI);
                shoms.sendTextMessage(value, null, mesg, sentPI, deliveredPI);

            }
        });
    }

    private void runthread2() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT_INTENT_FILTER), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED_INTENT_FILTER), 0);
                SmsManager shoms = SmsManager.getDefault();
                shoms.sendTextMessage(value, null, mesg1, sentPI, deliveredPI);
                //shoms.sendTextMessage(value, null, mesg3, sentPI, deliveredPI);
            }
        });
    }

    private void runthread1() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT_INTENT_FILTER), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED_INTENT_FILTER), 0);
                SmsManager shoms = SmsManager.getDefault();
               // shoms.sendTextMessage(value, null, mesg2, sentPI, deliveredPI);
                shoms.sendTextMessage(value, null, mesg2, sentPI, deliveredPI);
            }
        });
    }

    private void runthread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT_INTENT_FILTER), 0);
                PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED_INTENT_FILTER), 0);
                SmsManager shoms = SmsManager.getDefault();
                shoms.sendTextMessage(value, null, mesg3, sentPI, deliveredPI);
                //shoms.sendTextMessage(value, null, mesg1, sentPI, deliveredPI);

            }
        });
    }

    private void loadDetails() {
        Intent intent=new Intent(this,details.class);
        startActivity(intent);
    }

    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat=location.getLatitude();
                                    lon=location.getLongitude();
                                }
                            }

                        }
                );

            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
    private void findhospitals() {
        Uri gmmIntentUri = Uri.parse("geo:"+lat+lon+"?q=hospitals");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
    private void findpolice() {
        Uri gmmIntentUri = Uri.parse("geo:"+lat+lon+"?q=police");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
    private void findfire() {
        Uri gmmIntentUri = Uri.parse("geo:"+lat+lon+"?q=fire station");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }















}
