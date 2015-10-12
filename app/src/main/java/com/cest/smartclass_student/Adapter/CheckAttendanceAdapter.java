package com.cest.smartclass_student.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cest.smartclass_student.CustomOnItemClickListener;
import com.cest.smartclass_student.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by pc on 2015-09-27.
 */
public class CheckAttendanceAdapter  extends RecyclerView.Adapter<CheckAttendanceAdapter.ViewHolder> {
    private JsonArray array;
    private Context context;
    private CustomOnItemClickListener cocl;

    public CheckAttendanceAdapter(Context context, JsonArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false), context);

        return vh;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public JsonObject getItem(int position) {
        return array.get(position).getAsJsonObject();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d("gtd", "here --- " + position);
        JsonObject json = array.get(position).getAsJsonObject();


        holder.tv_date.setText(json.get("start").getAsString());

        int result = json.get("result").getAsInt();
        if(result==1){
            holder.tv_result.setText("출석");
        }else if(result ==2){
            holder.tv_result.setText("지각");
        }else{
            holder.tv_result.setText("결석");
        }


    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_date;
        public TextView tv_result;
        private Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            tv_date = (TextView) view.findViewById(R.id.item_attendance_date);
            tv_result = (TextView) view.findViewById(R.id.item_attendance_result);
        }

    }


}


