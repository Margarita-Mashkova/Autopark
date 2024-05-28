package com.example.autopark.model;

import com.example.autopark.model.enums.CarStatus;

import java.util.Objects;


public class Car {

    public int id;
    public String name;
    public String clientFIO;
    public CarStatus carStatus;

    public Car(int id, String name, String clientFIO, CarStatus carStatus) {
        this.id = id;
        this.name = name;
        this.clientFIO = clientFIO;
        this.carStatus = carStatus;
    }

    public Car() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientFIO() {
        return clientFIO;
    }

    public void setClientFIO(String clientFIO) {
        this.clientFIO = clientFIO;
    }

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && Objects.equals(name, car.name) && Objects.equals(clientFIO, car.clientFIO) && carStatus == car.carStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clientFIO, carStatus);
    }
}
