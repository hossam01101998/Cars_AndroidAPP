package be.ehb.eindopdrachthossam.View;
import static be.ehb.eindopdrachthossam.View.AddCarActivity.EXTRA_CAR;
import static be.ehb.eindopdrachthossam.View.CarViewModel.insertCar;

import be.ehb.eindopdrachthossam.Models.Car;
import be.ehb.eindopdrachthossam.Models.CarDAO;
import be.ehb.eindopdrachthossam.Models.CarDataBass;
import be.ehb.eindopdrachthossam.R;
import be.ehb.eindopdrachthossam.Others.CarAdapter;
import be.ehb.eindopdrachthossam.Others.VolleySingleton;
import be.ehb.eindopdrachthossam.View.CarViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<Car> carList;

    //for the roomdatabase
    private CarDAO carDAO;
    private CarViewModel carViewModel;
    private LiveData<List<Car>> allCars;






    /*CarDataBass carDatabase = CarDataBass.getInstance(getApplicationContext());
    CarDAO carDAO = carDatabase.getCarDAO();*/

    /*Car newCar = new Car("Car Name", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTutU2xxmImYqoo3f3_l6Yti3Yg3E7o2B2Mowz7YeQ8Og&s",
            "Overview", 4.5);*/

    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //miramos primero si hay algo escrito en la busqueda
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Realizar la búsqueda cada vez que cambie el texto
                String searchTerm = charSequence.toString().trim();
                searchCars(searchTerm);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        carList = new ArrayList<>();

        //for the roomdatabase
        // inicializar ViewModel y observar cambios en la lista de coches
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        allCars = carViewModel.getAllCars();
        allCars.observe(this, cars -> {
            // actualizar la interfaz de usuario cuando la lista de coches cambie
            CarAdapter adapter = new CarAdapter(MainActivity.this, cars);
            recyclerView.setAdapter(adapter);
        });


        // verificamos si la base de datos está vacía antes de cargar la API
        carViewModel.getAllCars().observe(this, cars -> {
            if (cars != null && !cars.isEmpty()) {
                // La base de datos ya tiene datos, no es necesario descargar de la API
                Toast.makeText(MainActivity.this, "Database has already data", Toast.LENGTH_SHORT).show();
            } else {
                // la base de datos está vacía, descargar de la API
                fetchCars();
            }
        });

        //boton de añadir abajo
        FloatingActionButton fabAddCar = findViewById(R.id.fabAddCar);
        fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lógica para manejar el clic del botón flotante "Añadir Coche"
                openAddCarActivity();
            }
        });

    }
    private void openAddCarActivity() {
        Intent intent = new Intent(this, AddCarActivity.class);
        startActivity(intent);

    }


    //fetches the cars from the json file API
    private void fetchCars() {
        //String url = "https://raw.githubusercontent.com/hossam01101998/Generated-Json/main/moviesresult.json";
        String url ="https://raw.githubusercontent.com/hossam01101998/Cars-API/main/bestcars.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("JSON Response", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject movieObject = response.getJSONObject(i);
                        String name = movieObject.getString("name");
                        String poster = movieObject.getString("poster");
                        String overview = movieObject.getString("overview");
                        Double rating = movieObject.getDouble("rating");

                        Car car = new Car(name, poster, overview, rating);
                        carList.add(car);
                    }

                    // inserta los coches en la base de datos
                    for (Car car : carList) {
                        insertCar(car);
                    }

                    CarAdapter adapter = new CarAdapter(MainActivity.this, carList);
                    recyclerView.setAdapter(adapter);

                    // muestra un mensaje solo si la lista de coches no está vacía
                    if (!carList.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Cars charged succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "The list of cars is empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "ERROR processing JSON file", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void searchCars(String searchTerm) {
        // Utilizar el ViewModel para buscar coches por nombre
        carViewModel.searchCarsByName(searchTerm).observe(this, cars -> {
            // Actualizar la interfaz de usuario con los resultados de la búsqueda
            CarAdapter adapter = new CarAdapter(MainActivity.this, cars);
            recyclerView.setAdapter(adapter);
        });
    }



}

