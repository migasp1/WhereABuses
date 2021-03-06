package com.example.whereabuses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    DocumentReference documentReference = rootRef.collection("Carreiras").document("Routes");
    String item;
    List<String> carreiras = new ArrayList<>(Arrays.asList("Seleciona uma opção"));
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, carreiras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        rootRef.collection("Carreiras").document("Routes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            ArrayList<String> arrList = new ArrayList<String>();
                            arrList = (ArrayList) documentSnapshot.get("Rotas");

                            for(int i=0;i<arrList.size();i++){
                                //Traversing the list
                                String s  = arrList.get(i);
                                carreiras.add(s);
                            }
                        }
                    }
                });

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinner.setOnItemSelectedListener(this);

    }


    public void searchButton(View view){
        Intent mapIntent = new Intent(this, GoogleMapsActivity.class);
        mapIntent.putExtra("carreira",item);
        if(item == "Seleciona uma opção"){
            Toast.makeText(getApplicationContext(), "Selecione uma carreira!", Toast.LENGTH_SHORT).show();
        }
        else{
            startActivity(mapIntent);
        }

    }
    public void onBusButton(View view){
        Intent sensorIntent = new Intent(this, SensorActivity.class);
        startActivity(sensorIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}