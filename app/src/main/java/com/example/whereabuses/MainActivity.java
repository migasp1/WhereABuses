package com.example.whereabuses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    DocumentReference documentReference = rootRef.collection("Carreiras").document("Routes");

    List<String> carreiras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buses_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (findViewById(R.id.mainLayout) != null) {
            switch (item.getItemId()) {
                case R.id.optionSettings:
                    Intent optionsIntent = new Intent(this, Settings.class);
                    startActivity(optionsIntent);
            }
        }
        return true;
    }


    public void searchButton(View view){
        Intent mapIntent = new Intent(this, GoogleMapsActivity.class);
        startActivity(mapIntent);
    }
    public void onBusButton(View view){
        Intent sensorIntent = new Intent(this, SensorActivity.class);
        startActivity(sensorIntent);
    }
}