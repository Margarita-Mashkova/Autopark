package com.example.autopark.json;

import android.content.Context;

import com.example.autopark.model.Car;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonHelper {
    private final String FILE_NAME = "data.json";

    public void exportToJson(List<Car> cars, Context context) {
        Gson gson = new Gson();
        JsonItems jsonItems = new JsonItems();
        jsonItems.setCars(cars);
        String joinSting = gson.toJson(jsonItems);

        try (FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fileOutputStream.write(joinSting.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Car> importFromJson(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream)) {
            Gson gson = new Gson();
            JsonItems items = gson.fromJson(inputStreamReader, JsonItems.class);
            return items.getCars();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class JsonItems{
        private List<Car> cars;

        public List<Car> getCars() {
            return cars;
        }

        public void setCars(List<Car> cars) {
            this.cars = cars;
        }
    }
}
