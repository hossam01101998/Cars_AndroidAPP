package be.ehb.eindopdrachthossam.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao

public interface CarDAO {
        @Insert
        void insertCar(Car car);

        @Query("SELECT * FROM Car")
        LiveData<List<Car>> getAllCars();

        @Update
        void updateCar(Car car);

        @Delete
        void deleteCar(Car n);

        @Query("SELECT * FROM Car WHERE name LIKE :searchName")
        LiveData<List<Car>> searchCarsByName(String searchName);


}


