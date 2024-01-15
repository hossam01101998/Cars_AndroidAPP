package be.ehb.eindopdrachthossam.Others;

import be.ehb.eindopdrachthossam.Models.Car;
import be.ehb.eindopdrachthossam.View.DetailActivity;
import be.ehb.eindopdrachthossam.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder>{

    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList){
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new CarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarHolder holder, int position) {
        Car car = carList.get(position);
        holder.name.setText(car.getName());
        holder.overview.setText(car.getOverview());
        holder.rating.setText(car.getRating().toString());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("name", car.getName());
                bundle.putString("poster", car.getPoster());
                bundle.putString("overview", car.getOverview());
                bundle.putDouble("rating", car.getRating());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        new DownloadImageTask(holder.imageView).execute(car.getPoster());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CarHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name, overview, rating;
        ConstraintLayout constraintLayout;

        public CarHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mPoster);
            name = itemView.findViewById(R.id.mName);
            overview = itemView.findViewById(R.id.mOverview);
            rating = itemView.findViewById(R.id.mRating);
            constraintLayout = itemView.findViewById(R.id.main_layout);
        }
    }

    // asyncTask para cargar imágenes en segundo plano
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<ImageView> imageViewReference;

        DownloadImageTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (imageViewReference != null && result != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }

}
