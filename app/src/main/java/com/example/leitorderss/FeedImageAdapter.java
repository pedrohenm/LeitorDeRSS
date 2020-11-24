package com.example.leitorderss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FeedImageAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedImageAdapter(@NonNull Context context, int resource, @NonNull List<FeedEntry> applications){
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
        viewHolder.txtData.setText(currentApp.getReleaseDate());

        new DownloadImageTask(viewHolder.imageView).execute(currentApp.getImgUrl());

        return  convertView;
    }

    private class ViewHolder {
        final TextView txtName;
        final TextView txtAutor;
        final TextView txtData;
        final ImageView imageView;

        ViewHolder(View v){
            this.txtName = v.findViewById(R.id.txtNomeImagem);
            this.txtAutor = v.findViewById(R.id.txtAutorImagem);
            this.txtData = v.findViewById(R.id.txtDataImagem);
            this.imageView = v.findViewById(R.id.imgView);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];

            Bitmap bmp = null;
            try{
                InputStream inputStream = new URL(url).openStream();
                bmp = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bmImage.setImageBitmap(bitmap);
        }
    }
}
