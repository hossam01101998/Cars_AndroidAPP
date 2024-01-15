package be.ehb.eindopdrachthossam.View;

import static be.ehb.eindopdrachthossam.View.CarViewModel.insertCar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import be.ehb.eindopdrachthossam.R;
import be.ehb.eindopdrachthossam.Models.Car;

public class AddCarActivity extends AppCompatActivity {

    public static final String EXTRA_CAR = "be.ehb.eindopdrachthossam.EXTRA_CAR";

    private EditText editTextCarName;
    private EditText editTextCarRating;
    private EditText editTextCarOverview;
    private EditText editTextCarPoster;

    private static final int ADD_CAR_REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextCarName = findViewById(R.id.editTextCarName);
        editTextCarRating = findViewById(R.id.editTextCarRating);
        editTextCarOverview = findViewById(R.id.editTextCarOverview);
        editTextCarPoster = findViewById(R.id.editTextCarPoster);

        Button btnSaveCar = findViewById(R.id.btnSaveCar);
        btnSaveCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCar();
            }
        });
    }

    private void saveCar() {
        String name = editTextCarName.getText().toString().trim();
        String ratingStr = editTextCarRating.getText().toString().trim();
        String overview = editTextCarOverview.getText().toString().trim();
        String poster = editTextCarPoster.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextCarName.setError("Please fill in the car name");
            return;
        }

        if (TextUtils.isEmpty(ratingStr)) {
            editTextCarRating.setError("Please fill in the rating");
            return;
        }

        if (TextUtils.isEmpty(overview)) {
            editTextCarOverview.setError("Please fill in the overview");
            return;
        }

        if (TextUtils.isEmpty(poster)) {
            poster = "https://img.freepik.com/vector-premium/no-hay-foto-disponible-icono-vector-simbolo-imagen-predeterminado-imagen-proximamente-sitio-web-o-aplicacion-movil_87543-10615.jpg";

        }

        try {
            double rating = Double.parseDouble(ratingStr);

            if (rating < 0 || rating > 10) {
                editTextCarRating.setError("Rating must be between 0 and 10");
                return;
            }

            Car newCar = new Car(name, poster, overview, rating);

            // insertamos el nuevo coche en la base de datos
            CarViewModel carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
            carViewModel.insertCar(newCar);

            // devolver el nuevo coche a la actividad principal
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_CAR, newCar);
            setResult(RESULT_OK, resultIntent);

            finish();

        } catch (NumberFormatException e) {
            editTextCarRating.setError("Please enter a valid number for the rating");
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CAR_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Car receivedCar = data.getParcelableExtra(EXTRA_CAR);
                // Agregar el coche recibido a la lista o a la base de datos, si es necesario
                insertCar(receivedCar);
            }
        }
    }

}
