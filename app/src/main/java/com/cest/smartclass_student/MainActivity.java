package com.cest.smartclass_student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends Activity {

    @Bind(R.id.main_grv_menu)
    GridView gv_menu;

    CustomAdapter ca;

    private static final int SPACE = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ca = new CustomAdapter(getApplicationContext(),C.menus);
        gv_menu.setAdapter(ca);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;
        private ArrayList<MenuItem> list = null;
        private ViewHolder viewHolder = null;
        private Context mContext = null;

        public CustomAdapter(Context c , ArrayList<MenuItem> arrays){
            this.mContext = c;
            this.inflater = LayoutInflater.from(c);
            this.list = arrays;
        }

        // Adapter가 관리할 Data의 개수를 설정 합니다.
        @Override
        public int getCount() {
            return list.size();
        }

        // Adapter가 관리하는 Data의 Item 의 Position을 <객체> 형태로 얻어 옵니다.
        @Override
        public MenuItem getItem(int position) {
            return list.get(position);
        }

        // Adapter가 관리하는 Data의 Item 의 position 값의 ID 를 얻어 옵니다.
        @Override
        public long getItemId(int position) {
            return position;
        }

        // ListView의 뿌려질 한줄의 Row를 설정 합니다.
        @Override
        public View getView(final int position, View convertview, ViewGroup parent) {

            View v = convertview;

            if(v == null){
                viewHolder = new ViewHolder();
                v = inflater.inflate(R.layout.item_menu, null);
                viewHolder.tv_menu = (TextView)v.findViewById(R.id.item_menu_tv);
                viewHolder.iv_menu = (ImageView)v.findViewById(R.id.item_menu_iv);

                DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPACE, metrics);
                int width = metrics.widthPixels - space;
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(width / 3, width / 3);
                v.setLayoutParams(params);

                v.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)v.getTag();
            }

            viewHolder.tv_menu.setText(getItem(position).getName());
            viewHolder.tv_menu.setTag(position);
            viewHolder.iv_menu.setImageResource(getItem(position).getImg());
            viewHolder.iv_menu.setTag(position);  //Tag 로 식별

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,ShowClassActivity.class);
                    int id = getItem(position).getId();
                    switch(id){
                        case C.MENU_ATTENDANCE:
                            i.putExtra("menu", C.MENU_ATTENDANCE);
                            break;
                        case C.MENU_CHECK_ATTENDANCE:
                            i.putExtra("menu", C.MENU_CHECK_ATTENDANCE);
                            break;
                        case C.MENU_NOTICE:
                            i.putExtra("menu",C.MENU_NOTICE);
                            break;
                    }
                    startActivity(i);
                }
            });
            return v;
        }
        /*
         * ViewHolder
         * getView의 속도 향상을 위해 쓴다.
         * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
         */
        class ViewHolder{
            public TextView tv_menu = null;
            public ImageView iv_menu = null;
        }

        @Override
        protected void finalize() throws Throwable {
            free();
            super.finalize();
        }

        private void free(){
            inflater = null;
            list = null;
            viewHolder = null;
            mContext = null;
        }
    }


}
