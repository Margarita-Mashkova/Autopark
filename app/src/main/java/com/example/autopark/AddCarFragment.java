package com.example.autopark;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.autopark.db.DbHelper;
import com.example.autopark.model.enums.CarStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import kotlinx.coroutines.scheduling.Task;

public class AddCarFragment extends Fragment {
    DbHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_car, container, false);

        EditText editTextName = v.findViewById(R.id.editTextName);
        Spinner spinnerStatus = v.findViewById(R.id.spinnerStatus);

        ArrayAdapter<String> carStatusAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, convertStatuses(CarStatus.values()));
        carStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(carStatusAdapter);
        //spinnerStatus.setPrompt("Статус машины");
        spinnerStatus.setSelection(0);

        EditText editTextClient = v.findViewById(R.id.editTextClient);
        Button buttonAdd = v.findViewById(R.id.buttonAdd);

        Bundle extras = getArguments();
        if (extras != null) {
            editTextName.setText(extras.getString("name"));
            editTextClient.setText(extras.getString("clientFIO"));
            spinnerStatus.setSelection(extras.getInt("carStatus"));
        }

        buttonAdd.setOnClickListener(view -> {
            if (!Objects.equals(editTextName.getText().toString(), "")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", editTextName.getText().toString());
                contentValues.put("clientFIO", editTextClient.getText().toString());
                switch (spinnerStatus.getSelectedItem().toString()){
                    case "Списана":
                        contentValues.put("carStatus", CarStatus.CANCELLED.toString());
                        break;
                    case "В салоне":
                        contentValues.put("carStatus", CarStatus.SALON.toString());
                        break;
                    case "Выдана на руки":
                        contentValues.put("carStatus", CarStatus.CARED.toString());
                        break;
                    default:
                        break;
                }
                if (extras != null) {
                    dbHelper.editCar(extras.getInt("id"), contentValues);
                    getActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                } else {
                    long res = dbHelper.addCar(contentValues);
                    getActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                }

            } else {
                Toast.makeText(getActivity(), "Заполните наименование авто", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private List<String> convertStatuses(CarStatus [] list) {
        List<String> rusStatuses = new ArrayList<>();
        for (CarStatus status : list) {
            switch (status) {
                case CANCELLED:
                    rusStatuses.add("Списана");
                    break;
                case SALON:
                    rusStatuses.add("В салоне");
                    break;
                case CARED:
                    rusStatuses.add("Выдана на руки");
                    break;
                default:
                    break;
            }
        }
        return rusStatuses;
    }
}