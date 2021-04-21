package com.zohaib.smartattendancestudent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.zohaib.smartattendancestudent.AlertDialogManager;
import com.zohaib.smartattendancestudent.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActMarkAttendance extends AppCompatActivity {
    String courseCode, courseName;
    public static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    public String SERVICE_ID = "786";
    String myEndPointName;
    String studentName, studentRoll, studentDeviceId;


    Button btnEndSession;
    TextView tvDate, tvStatus;
    private String date;


///////////////////discoverer/////////////////


    @Override
    protected void onPause() {
        super.onPause();
        Nearby.getConnectionsClient(ActMarkAttendance.this).stopDiscovery();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDiscovery();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_mark_attendance);
        tvDate = findViewById(R.id.textView5);
        tvStatus = findViewById(R.id.textView6);
        btnEndSession = findViewById(R.id.button4);

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText(date);


        courseCode = getIntent().getStringExtra("courseCode");
        courseName = getIntent().getStringExtra("courseName");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.students_pref), MODE_PRIVATE);
        studentName = sharedPreferences.getString(getString(R.string.key_name), "");
        studentRoll = sharedPreferences.getString(getString(R.string.key_roll), "");
        studentDeviceId = sharedPreferences.getString(getString(R.string.key_deviceId), "");
        if (!studentName.equals("") && !studentRoll.equals("") && !studentDeviceId.equals("")) {
            SERVICE_ID = courseCode;
            myEndPointName = studentName.toUpperCase() + "@" + studentRoll.toUpperCase() + "@" + studentDeviceId;
            startDiscovery();
        }


        Log.d("TAGGG", myEndPointName + " my end point ");

    }

    private void startDiscovery() {
        tvStatus.setText("Discovering...");
        Toast.makeText(this, "Discovery Started", Toast.LENGTH_SHORT).show();
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(ActMarkAttendance.this).
                startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
                    @Override
                    public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                        Log.d("TAGGG", discoveredEndpointInfo.getEndpointName() + " iend point found ");
                        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        tvStatus.setText("found "+discoveredEndpointInfo.getEndpointName());
                        Toast.makeText(ActMarkAttendance.this, "Course Found " + discoveredEndpointInfo.getEndpointName(), Toast.LENGTH_SHORT).show();
                        ///security/////
                        if (courseCode.equals(discoveredEndpointInfo.getEndpointName())) {
                            requestConnection(endpointId);

                        }


                    }

                    @Override
                    public void onEndpointLost(@NonNull String s) {
                        // disconnected
                        Log.d("TAGGG", s.toString() + " end pnt lost " + s);

                    }
                }, discoveryOptions);
    }

    private void requestConnection(String endpointId) {
        tvStatus.setText("Requesting connection.... ");
        Toast.makeText(ActMarkAttendance.this, "Requesting Connection ", Toast.LENGTH_SHORT).show();

        Nearby.getConnectionsClient(ActMarkAttendance.this).
                requestConnection(myEndPointName, endpointId, new ConnectionLifecycleCallback() {
                            @Override
                            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                                tvStatus.setText("Initiating connection...");
                                Log.d("TAGGG", endpointId.toString() + " connection inititated d ");
                                Log.d("TAGGG", endpointId.toString() + " inoft::: d " + connectionInfo.getEndpointName().toString());
                                Toast.makeText(ActMarkAttendance.this, "Connected Initiated with " + courseName, Toast.LENGTH_SHORT).show();

                                Nearby.getConnectionsClient(ActMarkAttendance.this).acceptConnection(endpointId, new PayloadCallback() {
                                    @Override
                                    public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

                                        tvStatus.setText("Attendance marked present...");
                                        Log.d("TAGGG", s.toString() + " Payload received discovery " + payload.toString());
                                        Toast.makeText(ActMarkAttendance.this, "Attendance Marked in " + courseName, Toast.LENGTH_SHORT).show();

                                        AlertDialogManager.showAlertDialog(ActMarkAttendance.this, "Attendance Marked ", "Attendance marked present in course " + courseName, true);
                                        Nearby.getConnectionsClient(ActMarkAttendance.this).stopDiscovery();


                                    }

                                    @Override
                                    public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                                        Log.d("TAGGG", s.toString() + " payload transfer update discover");

                                    }
                                });
                            }

                            @Override
                            public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                                switch (connectionResolution.getStatus().getStatusCode()) {
                                    case ConnectionsStatusCodes.STATUS_OK:
                                        // We're connected! Can now start sending and receiving data.
                                        Log.d("TAGGG", s.toString() + " connection result ok d" + s);
                                        tvStatus.setText("Connection Established...");
                                        Toast.makeText(ActMarkAttendance.this, "Connected with " + courseName, Toast.LENGTH_SHORT).show();
                                        break;

                                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                                        // The connection was rejected by one or both sides.
                                        Log.d("TAGGG", s.toString() + " connection result rejected d" + s);
                                        tvStatus.setText("Connection rejected...");
                                        Toast.makeText(ActMarkAttendance.this, "Connected rejected " + courseName, Toast.LENGTH_SHORT).show();
                                        break;
                                    case ConnectionsStatusCodes.STATUS_ERROR:
                                        // The connection broke before it was able to be accepted.
                                        Log.d("TAGGG", s.toString() + " connection result err d" + s);
                                        Toast.makeText(ActMarkAttendance.this, "Connected error " + s, Toast.LENGTH_SHORT).show();
                                        tvStatus.setText("Connection error... "+s);
                                        break;
                                    default:
                                        // Unknown status code
                                        Log.d("TAGGG", s.toString() + " connection result unknwn d" + s);

                                }
                            }

                            @Override
                            public void onDisconnected(@NonNull String s) {
                                Log.d("TAGGG", s.toString() + " discnntd   d" + s);
                                Toast.makeText(ActMarkAttendance.this, "Disconnected with " + courseName, Toast.LENGTH_SHORT).show();
                                tvStatus.setText("Disconnected..."+s);
                            }
                        }
                );
    }
}