package com.sherman.trovit.domain;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author Denis M. Gabaydulin
 * @since 07.04.19
 */
public class CarAdUniqueKey implements Serializable {
    private String uniqueId;
    private String make;
    private String model;
    private int year;
    private int mileage;

    public CarAdUniqueKey() {
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarAdUniqueKey that = (CarAdUniqueKey) o;

        // uniqueId is a strongest feature
        if (uniqueId != null && that.uniqueId != null && Objects.equal(uniqueId, that.uniqueId)) {
            return true;
        }

        // (year > 0 == that.year > 0) OR (year could be zero, but mileage MUST be > 0)
        return (year > 0 && that.year > 0 && year == that.year) || ((year == 0 || that.year == 0) && mileage > 0 && that.mileage > 0)
            && mileage == that.mileage
            && Objects.equal(make.toUpperCase(), that.make.toUpperCase())
            && ((model == null || that.model == null) || Objects.equal(model.toUpperCase(), that.model.toUpperCase()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueId, make, model, year, mileage);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("make", make)
            .add("model", model)
            .add("year", year)
            .add("mileage", mileage)
            .toString();
    }
}
