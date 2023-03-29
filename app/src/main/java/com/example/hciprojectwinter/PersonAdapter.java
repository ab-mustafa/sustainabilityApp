package com.example.hciprojectwinter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonAdapter extends ArrayAdapter<Person> {
    private Context mContext;
    private int mResource;



    public PersonAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Person> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView txtName = convertView.findViewById(R.id.txtName);

        TextView txtRank = convertView.findViewById(R.id.txtRank);

        TextView txtRankNum = convertView.findViewById(R.id.txtRankNum);

        TextView txtScore = convertView.findViewById(R.id.txtScore);

        TextView txtScoreNum = convertView.findViewById(R.id.txtScoreNum);

        txtName.setText(getItem(position).getName());

        txtRankNum.setText(Integer.toString(getItem(position).getRank()));

        txtScoreNum.setText(Integer.toString(getItem(position).getScore()));


        return convertView;
    }
}
