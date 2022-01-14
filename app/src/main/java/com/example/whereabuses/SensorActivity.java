package com.example.whereabuses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class SensorActivity extends AppCompatActivity {
    private ProximityObserver proximityObserver;
    private RecyclerView busList;
    ArrayList<Bus> buses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        busList = (RecyclerView) findViewById(R.id.busList);
        buses= Bus.createBuses(2);
        BusesAdapter adapter = new BusesAdapter(buses);
        busList.setAdapter(adapter);
        busList.setLayoutManager(new LinearLayoutManager(this));
        EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials("whereabuses-3ap", "205f284382a9f4775cd8a0251221a6f1");
        this.proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                        .onError(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();
        ProximityZone zone = new ProximityZoneBuilder()
                .forTag("whereabuses-3ap")
                .inCustomRange(1)
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        String tag = context.getTag();
                        Bus bus=new Bus(10,210);
                        buses.add(bus);
                        System.out.println( "Welcome to " + tag + "'s desk");
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        Log.d("app", "Bye bye, come again!");
                        return null;
                    }
                })
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        //String tag = contexts.getTag();
                        Bus bus=new Bus(10,310);
                        buses.add(bus);
                        System.out.println( "Welcome to "  + "'s desk");

                        return null;
                    }
                })
                .build();
        proximityObserver.startObserving(zone);

    }
}