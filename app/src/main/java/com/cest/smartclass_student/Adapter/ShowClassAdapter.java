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
public class ShowClassAdapter extends RecyclerView.Adapter<ShowClassAdapter.ViewHolder> {
    private JsonArray array;
    private Context context;
    private CustomOnItemClickListener cocl;

    public ShowClassAdapter(Context context, JsonArray array ) {
        this.context = context;
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false), context);

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
        holder.tv_name.setText(json.get("name").getAsString());
        holder.tv_code.setText(json.get("code").getAsString());
        holder.tv_professor.setText(json.get("professor").getAsString());


    }

    public void setCustomOnItemClickListener(CustomOnItemClickListener cocl){
       this.cocl = cocl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_name;
        public TextView tv_code;
        public TextView tv_professor;
        private Context context;


        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            tv_code = (TextView) view.findViewById(R.id.item_class_code);
            tv_name = (TextView) view.findViewById(R.id.item_class_name);
            tv_professor = (TextView) view.findViewById(R.id.item_class_professor);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

           if(cocl!=null){
               cocl.onItemClick(v,getPosition());
           }
        }
    }





}


