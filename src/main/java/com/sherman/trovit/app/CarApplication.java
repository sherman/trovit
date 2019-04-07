package com.sherman.trovit.app;

import com.sherman.trovit.service.CarService;
import com.sherman.trovit.service.CarServiceImpl;
import com.sherman.trovit.util.Settings;
import org.apache.spark.sql.SparkSession;

import java.io.File;

/**
 * @author Denis M. Gabaydulin
 * @since 07.04.19
 */
public class CarApplication {
    public static void main(String args[]) {
        SparkSession sparkSession = SparkSession.builder()
            .master("local[6]")
            .config("spark.sql.codegen.wholeStage", "false")
            .enableHiveSupport()
            .getOrCreate();

        Settings settings = new Settings(new File("/tmp/deduplicatedData.json"));

        CarService carService = new CarServiceImpl(sparkSession, settings);
        carService.deduplicate();
    }
}
