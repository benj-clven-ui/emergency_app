package com.myapp.emergency;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapterFam extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;


    public MyAdapterFam(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_family_list_layout, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setTextColor( Color.BLACK );
        listItemText.setAllCaps( true );

        listItemText.setText(list.get(position));

        Button callBtn = (Button)view.findViewById(R.id.call_btn);
        Button editBtn = (Button)view.findViewById(R.id.edit_btn);

        callBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                list.get( position);
                int id_To_Search = position + 1;


                Bundle dataBundle = new Bundle();
                dataBundle.putInt( "id", id_To_Search );

                Intent intent = new Intent( context.getApplicationContext(), FamilyCall.class );

                intent.putExtras( dataBundle );
                context.startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.get( position);
                int id_To_Search = position + 1;


                Bundle dataBundle = new Bundle();
                dataBundle.putInt( "id", id_To_Search );

                Intent intent = new Intent( context.getApplicationContext(), FamilyEdit.class );

                intent.putExtras( dataBundle );
                context.startActivity(intent);
            }
        });

        return view;
    }

}