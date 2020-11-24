package com.example.leitorderss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(@NonNull Context context, int resource, @NonNull List<FeedEntry> applications){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        viewHolder.txtName.setText(currentApp.getName());
        viewHolder.txtAutor.setText(currentApp.getArtist());
        viewHolder.txtSumario.setText(currentApp.getSummary());

        return  convertView;
    }

    private class ViewHolder {
        final TextView txtName;
        final TextView txtAutor;
        final TextView txtSumario;

        ViewHolder(View v){
            this.txtName = v.findViewById(R.id.txtName);
            this.txtAutor = v.findViewById(R.id.txtAutor);
            this.txtSumario = v.findViewById(R.id.txtSumario);
        }
    }
}
