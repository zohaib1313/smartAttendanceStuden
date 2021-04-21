package com.zohaib.smartattendancestudent.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.zohaib.smartattendancestudent.R;
import com.zohaib.smartattendancestudent.models.ModelCourses;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class AdapterCoursesAttendance extends RecyclerView.Adapter<AdapterCoursesAttendance.MyViewHolder> {

    public AdapterCoursesAttendance(ArrayList<ModelCourses> coursesArrayList, Context context, Activity activity) {
        this.coursesArrayList = coursesArrayList;
        this.context = context;
        this.activity = activity;
    }

    ArrayList<ModelCourses> coursesArrayList;
    Context context;
    public IOnItemClickListener iOnItemClickListener;
    private ModelCourses mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    private Activity activity;



    public void setiOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    public interface IOnItemClickListener {
        void onItemClick(ModelCourses modelCourse);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_courses, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        List<Integer> allColors = null;
        try {
            allColors = getAllMaterialColors();
            int randomIndex = new Random().nextInt(allColors.size());
            int randomColor = allColors.get(randomIndex);
            holder.cardView.setCardBackgroundColor(randomColor);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        ModelCourses modelCourses = coursesArrayList.get(position);

        holder.tvCourseName.setText(modelCourses.getCourseName());
        holder.tvCourseCode.setText(modelCourses.getCourseCode());

    }
    private List<Integer> getAllMaterialColors() throws IOException, XmlPullParserException {
        XmlResourceParser xrp = context.getResources().getXml(R.xml.material_design_colors);
        List<Integer> allColors = new ArrayList<>();
        int nextEvent;
        while ((nextEvent = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
            String s = xrp.getName();
            if ("color".equals(s)) {
                String color = xrp.nextText();
                allColors.add(Color.parseColor(color));
            }
        }
        return allColors;
    }
    @Override
    public int getItemCount() {
        return coursesArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseCode, tvCourseName;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView.getRootView();
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnItemClickListener.onItemClick(coursesArrayList.get(getAdapterPosition()));
                }
            });
        }
    }



    public void deleteItem(int position) {
        mRecentlyDeletedItem = coursesArrayList.get(position);
        mRecentlyDeletedItemPosition = position;
        coursesArrayList.remove(position);
        Paper.book().write("COURSES",coursesArrayList);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    public void showUndoSnackbar() {
        View view = activity.findViewById(R.id.actCoursesAttendance);
        Snackbar snackbar = Snackbar.make(view, "Course Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.setActionTextColor(context.getResources().getColor(R.color.design_default_color_on_primary));
        snackbar.show();
    }

    private void undoDelete() {
        coursesArrayList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        Paper.book().write("COURSES", coursesArrayList);
        notifyDataSetChanged();
    }
}
