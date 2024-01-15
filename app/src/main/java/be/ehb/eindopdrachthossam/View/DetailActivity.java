package be.ehb.eindopdrachthossam.View;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import be.ehb.eindopdrachthossam.R;

public class DetailActivity extends AppCompatActivity {


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView imageView = findViewById(R.id.mPoster);
        TextView rating_tv = findViewById(R.id.mRating);
        TextView name_tv = findViewById(R.id.mName);
        TextView overview_tv = findViewById(R.id.mOverview);

        Bundle bundle = getIntent().getExtras();

        String mName = bundle.getString("name");
        String mPoster = bundle.getString("poster");
        String mOverView = bundle.getString("overview");
        double mRating = bundle.getDouble("rating");

        new DownloadImageTask(imageView).execute(mPoster);

        rating_tv.setText(Double.toString(mRating));
        name_tv.setText(mName);
        overview_tv.setText(mOverView);
    }

    // asyncTask para cargar imágenes en segundo plano
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

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

