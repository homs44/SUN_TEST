package com.cest.smartclass_student.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cest.smartclass_student.CustomOnItemClickListener;
import com.cest.smartclass_student.R;
import com.cest.smartclass_student.support.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by pc on 2015-10-13.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private JsonArray array;
    private Context context;
    private CustomOnItemClickListener cocl;

    public NoticeAdapter(Context context, JsonArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false), context);

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

        JsonObject json = array.get(position).getAsJsonObject();
        Util.log(json.toString());
        holder.tv_title.setText(json.get("title").getAsString());
        holder.tv_created.setText(json.get("created").getAsString());
        holder.tv_professor.setText(json.get("name").getAsString());


    }

    public void setCustomOnItemClickListener(CustomOnItemClickListener cocl){
        this.cocl = cocl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_title;
        public TextView tv_created;
        public TextView tv_professor;
        private Context context;


        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            tv_title = (TextView) view.findViewById(R.id.item_notice_title);
            tv_created = (TextView) view.findViewById(R.id.item_notice_created);
            tv_professor = (TextView) view.findViewById(R.id.item_notice_professor);
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


