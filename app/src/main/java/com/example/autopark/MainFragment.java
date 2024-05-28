package com.example.autopark;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.autopark.db.DbHelper;
import com.example.autopark.json.JsonHelper;
import com.example.autopark.model.Car;
import com.example.autopark.model.enums.CarStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainFragment extends Fragment {
    DbHelper dbHelper;
    JsonHelper jsonHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(getActivity());
        jsonHelper = new JsonHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        Button buttonAdd = v.findViewById(R.id.buttonAdd);
        Button buttonEdit = v.findViewById(R.id.buttonEdit);
        Button buttonReport = v.findViewById(R.id.buttonReport);
        Button buttonImport = v.findViewById(R.id.buttonImportJson);
        Button buttonExport = v.findViewById(R.id.buttonExportJson);
        ListView listViewCars = v.findViewById(R.id.listViewCars);
        Button btnDeleteAll = v.findViewById(R.id.buttonDeleteAll);

        Bundle extras = getArguments();
        List<Car> cars = dbHelper.findAllCars();
        List<String> stringListCars;
        if (extras != null) {
            stringListCars = extras.getStringArrayList("cars");
            extras = null;
        }else {
            stringListCars = convertFromCarsToString(cars);
        }
        ArrayAdapter<String> carsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item_choice, stringListCars);
        listViewCars.setAdapter(carsAdapter);

        buttonAdd.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragContainer, AddCarFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        });

        buttonEdit.setOnClickListener(view -> {
            int indList = listViewCars.getCheckedItemPosition();
            if(indList != -1) {
                Car selectedCar = cars.get(indList);
                Bundle bundle = new Bundle();
                bundle.putInt("id", selectedCar.getId());
                bundle.putString("name", selectedCar.getName());
                bundle.putString("clientFIO", selectedCar.getClientFIO());
                bundle.putInt("carStatus", CarStatus.valueOf(selectedCar.getCarStatus().toString()).ordinal());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragContainer, AddCarFragment.class, bundle)
                        .addToBackStack(null)
                        .commit();
            }else {
                Toast.makeText(getActivity(), "Сначала выберите машину в списке", Toast.LENGTH_SHORT).show();
            }
        });

        buttonReport.setOnClickListener(view -> {
            Map<CarStatus, Integer> report = new HashMap<>();
            for (Car car : cars) {
                CarStatus status = car.getCarStatus();
                if (!report.containsKey(status)) {
                    report.put(status, 1);
                } else {
                    report.put(status, report.get(status) + 1);
                }
            }
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            intent.putExtra("canceled", report.get(CarStatus.CANCELLED));
            intent.putExtra("salon", report.get(CarStatus.SALON));
            intent.putExtra("cared", report.get(CarStatus.CARED));
            startActivity(intent);
        });

        buttonExport.setOnClickListener(view -> {
            jsonHelper.exportToJson(cars, getContext());
        });

        buttonImport.setOnClickListener(view -> {
            cars.clear();
            cars.addAll(jsonHelper.importFromJson(getContext()));
            updateList(cars);
        });

//        btnDeleteAll.setOnClickListener(view -> {
//            dbHelper.deleteAll();
//            updateList(new ArrayList<>());
//        });

        return v;
    }

    private void updateList(List<Car> cars){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("cars", new ArrayList<>(convertFromCarsToString(cars)));
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, MainFragment.class, bundle)
                .commit();
    }

    private List<String> convertFromCarsToString(List<Car> cars) {
        List<String> carsString = new ArrayList<>();
        for (Car car : cars) {
            StringBuilder sb = new StringBuilder();
            //sb.append(car.getId());
            sb.append(String.format("Наименование: %s\nКлиент: %s\nСтатус: ", car.getName(), car.getClientFIO()));
            switch (car.carStatus) {
                case CANCELLED:
                    sb.append("Списана");
                    break;
                case SALON:
                    sb.append("В салоне");
                    break;
                case CARED:
                    sb.append("Выдана на руки");
                    break;
                default:
                    break;
            }
            carsString.add(sb.toString());
        }
        return carsString;
    }
}