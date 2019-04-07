package com.sherman.trovit.service;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.sherman.trovit.domain.CarAdGroup;
import com.sherman.trovit.domain.CarAdUniqueKey;
import com.sherman.trovit.util.Settings;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.spark.sql.functions.col;

/**
 * @author Denis M. Gabaydulin
 * @since 07.04.19
 */
public class CarServiceImpl implements CarService, Serializable {
    private static final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private transient final SparkSession sparkSession;
    private transient final Settings settings;

    private transient final StructType groupSchema = new StructType()
        .add(
            "key",
            new StructType()
                .add("make", DataTypes.StringType)
                .add("model", DataTypes.StringType)
                .add("year", DataTypes.IntegerType)
                .add("mileage", DataTypes.IntegerType)
        )
        .add("uniqueKeys", DataTypes.createArrayType(DataTypes.StringType));


    public CarServiceImpl(SparkSession sparkSession, Settings settings) {
        this.sparkSession = sparkSession;
        this.settings = settings;
    }

    @Override
    public void deduplicate() {
        String carsData = Resources.getResource("cars.json").getFile();
        Dataset<Row> cars = sparkSession.read().json(carsData);

        cars.printSchema();

        JavaRDD<Row> deduplicated = cars
            .select(
                col("uniqueId"),
                col("make"),
                col("model"),
                col("year"),
                col("mileage")
            )
            .filter(col("make").isNotNull()) // drop appox. 13102 elts
            .repartition(col("uniqueId")) // required for uniform shuffling and to guarantee that all elements with given uniqueId are processed in a single partition.
            .javaRDD()
            .groupBy(
                (Function<Row, CarAdUniqueKey>) row -> {
                    CarAdUniqueKey key = new CarAdUniqueKey();
                    key.setUniqueId(row.getString(0));
                    key.setMake(row.getString(1));
                    key.setModel(row.getString(2));
                    key.setYear((int)row.getLong(3));
                    key.setMileage((int)row.getLong(4));
                    return key;
                }
            )
            .flatMap(
                (FlatMapFunction<Tuple2<CarAdUniqueKey, Iterable<Row>>, CarAdGroup>) tuple -> {
                    Set<String> ids = StreamSupport.stream(tuple._2.spliterator(), false)
                        .map(v -> v.getAs("uniqueId").toString())
                        .collect(Collectors.toSet());

                    CarAdGroup carGroup = new CarAdGroup();
                    carGroup.setKey(tuple._1);
                    carGroup.setUniqueKeys(ids);

                    return ImmutableList.of(carGroup).iterator();
                }
            ).map(
                (Function<CarAdGroup, Row>) carAdGroup -> RowFactory.create(
                    RowFactory.create(
                        carAdGroup.getKey().getMake(),
                        carAdGroup.getKey().getModel(),
                        carAdGroup.getKey().getYear(),
                        carAdGroup.getKey().getMileage()
                    ),
                    carAdGroup.getUniqueKeys().toArray()
                )
            );

        Dataset<Row> df = sparkSession.createDataFrame(deduplicated, groupSchema);

        df
            .coalesce(1)
            .write()
            .mode(SaveMode.Overwrite)
            .json(settings.getResultPath().getAbsolutePath());
    }
}
