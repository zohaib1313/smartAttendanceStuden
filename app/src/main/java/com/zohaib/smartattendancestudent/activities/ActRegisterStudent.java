package com.zohaib.smartattendancestudent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.zohaib.smartattendancestudent.AlertDialogManager;
import com.zohaib.smartattendancestudent.MainActivity;
import com.zohaib.smartattendancestudent.R;
import com.zohaib.smartattendancestudent.models.ModelCourses;
import com.zohaib.smartattendancestudent.models.ModelStudents;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ActRegisterStudent extends AppCompatActivity {

///////////advertiser/////////////

    Button btnEndBroadCast;

    String courseCode;
    String courseName;
    public static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    public String SERVICE_ID = "786";

    ArrayList<ModelStudents> studentsArrayList = new ArrayList<>();
    ArrayList<ModelCourses> coursesArrayList = new ArrayList<>();

    //name+"@"+rollno+"@"+deviceId
    String advertiserEndPointName;


    @Override
    protected void onPause() {
        Nearby.getConnectionsClient(ActRegisterStudent.this).stopAdvertising();
        Nearby.getConnectionsClient(ActRegisterStudent.this).stopAllEndpoints();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startAdvertising();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        btnEndBroadCast = findViewById(R.id.button3);
        btnEndBroadCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nearby.getConnectionsClient(ActRegisterStudent.this).stopAdvertising();
                Nearby.getConnectionsClient(ActRegisterStudent.this).stopAllEndpoints();
                finish();
            }
        });

        advertiserEndPointName = MainActivity.name + "@" + MainActivity.roll + "@" + MainActivity.deviceId;


    }

    private void startAdvertising() {
        Toast.makeText(this, "BroadCast Started", Toast.LENGTH_SHORT).show();
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(this).startAdvertising(advertiserEndPointName, SERVICE_ID, new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
                Log.d("TAGGG", s.toString() + " connection inititated " + s);
                Log.d("TAGGG", s.toString() + " inoft::: " + connectionInfo.getEndpointName().toString());

                String endPointName = connectionInfo.getEndpointName();
                String[] splitEndPointName = endPointName.split("@");

                String courseCode = splitEndPointName[0];
                String courseName = splitEndPointName[1];
                Toast.makeText(ActRegisterStudent.this, "Connection Initiated with " + courseName, Toast.LENGTH_SHORT).show();

                Nearby.getConnectionsClient(ActRegisterStudent.this).acceptConnection(
                        s, new PayloadCallback() {
                            @Override
                            public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

                                Log.d("TAGGG", s.toString() + " Payload received " + payload.toString().toString());

                                boolean isAlreadyEnrolled = false;


                                ArrayList<ModelCourses> coursesArrayList = Paper.book().read("COURSES", new ArrayList<ModelCourses>());
                                for (ModelCourses modelCourse : coursesArrayList) {

                                    if (modelCourse.getCourseCode().equals(courseCode)) {
                                        isAlreadyEnrolled = true;
                                        break;
                                    }
                                }


                                if(isAlreadyEnrolled)
                                {
                                    AlertDialogManager.showAlertDialog(ActRegisterStudent.this, "Success", "You are already registered in " + courseName, true);


                                }else {
                                    Toast.makeText(ActRegisterStudent.this, "Congrats!! you are now registered in " + courseName, Toast.LENGTH_SHORT).show();
                                    coursesArrayList.add(new ModelCourses(courseCode, courseName));
                                    Paper.book().write("COURSES", coursesArrayList);
                                    AlertDialogManager.showAlertDialog(ActRegisterStudent.this, "Success", "You got registered in " + courseName, true);


                                }
                            }

                            @Override
                            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
                                Log.d("TAGGG", s.toString() + " payload transfer update");

                            }
                        }
                );
            }

            @Override
            public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {

                Log.d("TAGGG", s.toString() + " connection result " + s);
                switch (connectionResolution.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK:
                        // We're connected! Can now start sending and receiving data.
                        Log.d("TAGGG", s.toString() + " connection result ok " + s);
                        Toast.makeText(ActRegisterStudent.this, "Connection Established with " + courseName, Toast.LENGTH_SHORT).show();

                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        // The connection was rejected by one or both sides.
                        Log.d("TAGGG", s.toString() + " connection result rejected" + s);
                        Toast.makeText(ActRegisterStudent.this, "Connection Rejected with " + courseName, Toast.LENGTH_SHORT).show();
                        AlertDialogManager.showAlertDialog(ActRegisterStudent.this, "Rejected", "You got rejected in " + courseName, false);

                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        // The connection broke before it was able to be accepted.
                        Toast.makeText(ActRegisterStudent.this, "Connection Error with " + courseName, Toast.LENGTH_SHORT).show();
                        AlertDialogManager.showAlertDialog(ActRegisterStudent.this, "Error", " " + s, false);

                        Log.d("TAGGG", s.toString() + " connection result err " + s);
                        break;
                    default:
                        // Unknown status code
                }
            }

            @Override
            public void onDisconnected(@NonNull String s) {
                Toast.makeText(ActRegisterStudent.this, "Connection Disconnected with " + courseName, Toast.LENGTH_SHORT).show();
                Log.d("TAGGG", s.toString() + " disconnected");
                startAdvertising();
            }

        }, advertisingOptions);

    }
}