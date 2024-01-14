package be.ehb.eindopdrachthossam.Models;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Car.class}, version = 1)
public abstract class CarDataBass extends RoomDatabase{

    private static CarDataBass Instance;

    public static CarDataBass getInstance(Context context) {
        if(Instance == null){
            synchronized (CarDataBass.class) {
                if (Instance == null)
                    Instance = Room.databaseBuilder(
                                    context,
                                    CarDataBass.class,
                                    "cars.db")
                            .build();
            };
        }
        return Instance;
    }

    public abstract CarDAO getCarDAO();
}
