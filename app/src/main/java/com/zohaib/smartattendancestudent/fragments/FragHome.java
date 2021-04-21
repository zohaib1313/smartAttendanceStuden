package com.zohaib.smartattendancestudent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaib.smartattendancestudent.AlertDialogManager;
import com.zohaib.smartattendancestudent.MainActivity;
import com.zohaib.smartattendancestudent.R;
import com.zohaib.smartattendancestudent.activities.ActCoursesAttendance;
import com.zohaib.smartattendancestudent.activities.ActRegisterStudent;

import java.util.Objects;


public class FragHome extends Fragment {

    //

    public FragHome() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    Button btnRegisterStudent, btnCoursesAttendance;
    TextView tvName, tvRoll;
  //  String deviceId, rollNo, name, contact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_home, container, false);
        btnRegisterStudent = view.findViewById(R.id.button);
        btnCoursesAttendance = view.findViewById(R.id.button2);
        tvName = view.findViewById(R.id.textView2);
        tvRoll = view.findViewById(R.id.textView3);

//        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(getString(R.string.students_pref), Context.MODE_PRIVATE);
//        deviceId = sharedPreferences.getString(getString(R.string.key_deviceId), "");
//        rollNo = sharedPreferences.getString(getString(R.string.key_roll), "");
//        name = sharedPreferences.getString(getString(R.string.key_deviceId), "");
//        contact = sharedPreferences.getString(getString(R.string.key_deviceId), "");


        if (MainActivity.deviceId.equals("") || MainActivity.roll.equals("") || MainActivity.name.equals("") || MainActivity.contact.equals("")) {
            tvName.setText("Set your");
            tvRoll.setText("profile");
            btnCoursesAttendance.setVisibility(View.GONE);
            btnRegisterStudent.setVisibility(View.GONE);
        } else {
            tvName.setText(MainActivity.name);
            tvRoll.setText(MainActivity.roll);
            btnCoursesAttendance.setVisibility(View.VISIBLE);
            btnRegisterStudent.setVisibility(View.VISIBLE);
        }


        btnRegisterStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActRegisterStudent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnCoursesAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActCoursesAttendance.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        return view;
    }
}