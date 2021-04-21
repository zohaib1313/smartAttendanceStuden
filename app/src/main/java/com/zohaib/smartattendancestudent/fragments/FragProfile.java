package com.zohaib.smartattendancestudent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaib.smartattendancestudent.MainActivity;
import com.zohaib.smartattendancestudent.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class FragProfile extends Fragment {


    public FragProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    SharedPreferences sharedPreferences;
    EditText etName, etRoll, etContact;
    Button btnSubmit;
    TextView tvDeviceId;


   // String deviceId, rollNo, name, contact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_profile, container, false);
        etName = view.findViewById(R.id.tvProfName);
        etRoll = view.findViewById(R.id.tvProfRoll);
        etContact = view.findViewById(R.id.tvProfContact);
        btnSubmit = view.findViewById(R.id.btnProfSubmit);
        tvDeviceId = view.findViewById(R.id.tvDeviceId);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.students_pref), Context.MODE_PRIVATE);
//        deviceId = sharedPreferences.getString(getString(R.string.key_deviceId), "");
//        rollNo = sharedPreferences.getString(getString(R.string.key_roll), "");
//        name = sharedPreferences.getString(getString(R.string.key_deviceId), "");
//        contact = sharedPreferences.getString(getString(R.string.key_deviceId), "");

        if (!MainActivity.deviceId.equals("") && !MainActivity.contact.equals("") && !MainActivity.roll.equals("") && !MainActivity.name.equals("")) {
            etName.setText(MainActivity.name);
            etRoll.setText(MainActivity.roll);
            etContact.setText(MainActivity.contact);

        }
        tvDeviceId.setText(MainActivity.deviceId);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, roll, contact;

                name = etName.getText().toString().toUpperCase();
                roll = etRoll.getText().toString().toUpperCase();
                contact = etContact.getText().toString().toUpperCase();
                if (name.isEmpty() || roll.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(getString(R.string.students_pref), MODE_PRIVATE).edit();
                editor.putString(getString(R.string.key_name), name);
                editor.putString(getString(R.string.key_roll), roll);
                editor.putString(getString(R.string.key_contact), contact);
                editor.commit();
                Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
                Intent intent = getActivity().getIntent();
                startActivity(intent);

            }
        });


        return view;
    }
}