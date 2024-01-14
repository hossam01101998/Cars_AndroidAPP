package be.ehb.eindopdrachthossam.View;
import be.ehb.eindopdrachthossam.Models.CarDAO;
import be.ehb.eindopdrachthossam.Models.CarDataBass;
import be.ehb.eindopdrachthossam.R;
import be.ehb.eindopdrachthossam.Models.Car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import be.ehb.eindopdrachthossam.View.CarViewModel;

public class DetailActivity extends AppCompatActivity {

    /*CarDataBass carDatabase = CarDataBass.getInstance(getApplicationContext());
    CarDAO carDAO = carDatabase.getCarDAO();*/
    private CarViewModel carViewModel;

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

        /*private void editCar(Car car) {
            // Implementa la lógica para cargar la pantalla de edición con los detalles del coche

            // Después de que el usuario realiza las ediciones y guarda los cambios
            carViewModel.updateCar(car);

            // Actualiza la interfaz de usuario (recargando la lista, etc.)
        }*/
    }
}

