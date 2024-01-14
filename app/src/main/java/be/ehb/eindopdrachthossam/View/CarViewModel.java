package be.ehb.eindopdrachthossam.View;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import be.ehb.eindopdrachthossam.Models.CarDataBass;
import be.ehb.eindopdrachthossam.Models.CarDatabaseExecutor;
import be.ehb.eindopdrachthossam.Models.CarDAO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.ehb.eindopdrachthossam.Models.Car;
import be.ehb.eindopdrachthossam.Models.CarDAO;

public class CarViewModel extends AndroidViewModel {

    private static CarDAO carDAO;

    public CarViewModel(Application application) {
        super(application);
        CarDataBass carDatabase = Room.databaseBuilder(application, CarDataBass.class, "cars.db").build();
        carDAO = carDatabase.getCarDAO();
    }



    public static void insertCar(Car car) {
        CarDatabaseExecutor.databaseWriteExecutor.execute(() -> carDAO.insertCar(car));
    }

    public LiveData<List<Car>> getAllCars() {
        return carDAO.getAllCars();
    }
//vamos a probar a buscar por nombre
    public LiveData<List<Car>> searchCarsByName(String searchName) {
        return carDAO.searchCarsByName("%" + searchName + "%");
    }

    public void updateCar(Car car) {
        CarDatabaseExecutor.databaseWriteExecutor.execute(() -> {
            carDAO.updateCar(car);
        });
    }

}
