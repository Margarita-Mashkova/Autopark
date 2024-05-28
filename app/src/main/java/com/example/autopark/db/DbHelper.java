package com.example.autopark.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.autopark.model.Car;
import com.example.autopark.model.enums.CarStatus;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;
    private final String tableName = "car";

    public DbHelper(@Nullable Context context) {
        super(context, "AutoparkDb", null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + tableName +
                "(" +
                "id integer primary key autoincrement," +
                "name text," +
                "clientFIO text," +
                "carStatus text" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public Car findCar(int id){
        if(!database.isOpen()){
            database = getReadableDatabase();
        }
        Cursor cursor = database.query(tableName, null, "where id = ?",
                new String[] {Integer.toString(id)}, null, null, null);
        Car car = new Car();
        if(cursor != null){
            while (cursor.moveToNext()){
                String name = cursor.getString(Math.max(cursor.getColumnIndex("name"), 0));
                String clientFio = cursor.getString(Math.max(cursor.getColumnIndex("clientFIO"), 0));
                String carStatus = cursor.getString(Math.max(cursor.getColumnIndex("carStatus"), 0));
                return new Car(id, name, clientFio, CarStatus.valueOf(carStatus));
            }
        }
        return car;
    }

    // Возвращает номер строки в случае успешной вставки, -1 - неудачи
    public long addCar(ContentValues contentValues) {
        if (!database.isOpen()) {
            database = this.getWritableDatabase();
        } else if (database.isReadOnly()) {
            database = this.getWritableDatabase();
        }
        return database.insert(tableName, null, contentValues);
    }

    public void editCar(int id, ContentValues contentValues) {
        if (!database.isOpen()) {
            database = this.getWritableDatabase();
        }else if (database.isReadOnly()) {
            database = this.getWritableDatabase();
        }

        int cursor = database.update(tableName, contentValues, "id = ?",
                new String[]{Integer.toString(id)});
    }

    public List<Car> findAllCars() {
        if (!database.isOpen()) {
            database = this.getReadableDatabase();
        }
        List<Car> carList = new ArrayList<>();
        Cursor cursor = database.query(tableName, null, null, null,
                null, null, "id asc");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(Math.max(cursor.getColumnIndex("id"), 0));
                String name = cursor.getString(Math.max(cursor.getColumnIndex("name"), 0));
                String clientFio = cursor.getString(Math.max(cursor.getColumnIndex("clientFIO"), 0));
                String carStatus = cursor.getString(Math.max(cursor.getColumnIndex("carStatus"), 0));
                Car car = new Car(id, name, clientFio, CarStatus.valueOf(carStatus));
                carList.add(car);
            }
            cursor.close();
        }
        return carList;
    }

    public void deleteAll(){
        database.delete(tableName, null, null);
    }
}
