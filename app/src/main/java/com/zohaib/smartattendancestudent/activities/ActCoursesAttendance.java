package com.zohaib.smartattendancestudent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.zohaib.smartattendancestudent.R;
import com.zohaib.smartattendancestudent.adapters.AdapterCoursesAttendance;
import com.zohaib.smartattendancestudent.models.ModelCourses;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ActCoursesAttendance extends AppCompatActivity {
    RecyclerView rvCoursesAttendance;
    AdapterCoursesAttendance adapterCoursesAttendance;

    ArrayList<ModelCourses> coursesArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_courses_attendance);
        rvCoursesAttendance = findViewById(R.id.rvCoursesAttendanceStudent);
        rvCoursesAttendance.setLayoutManager(new LinearLayoutManager(this));
        rvCoursesAttendance.setHasFixedSize(true);


        coursesArrayList = Paper.book().read("COURSES", new ArrayList<ModelCourses>());
        adapterCoursesAttendance = new AdapterCoursesAttendance(coursesArrayList, ActCoursesAttendance.this, this);
        rvCoursesAttendance.setAdapter(adapterCoursesAttendance);
        adapterCoursesAttendance.notifyDataSetChanged();
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(adapterCoursesAttendance, this);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(rvCoursesAttendance);
        adapterCoursesAttendance.setiOnItemClickListener(new AdapterCoursesAttendance.IOnItemClickListener() {
            @Override
            public void onItemClick(ModelCourses modelCourse) {
                Intent intent = new Intent(ActCoursesAttendance.this, ActMarkAttendance.class);
                intent.putExtra("courseCode", modelCourse.getCourseCode());
                intent.putExtra("courseName", modelCourse.getCourseName());
                startActivity(intent);
            }
        });
    }
}

