package be.ehb.eindopdrachthossam.Models;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarDatabaseExecutor {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
