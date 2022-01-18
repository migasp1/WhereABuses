package com.example.whereabuses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusesAdapter extends RecyclerView.Adapter<BusesAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button messageButton;
        public int nCarreira;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.carreira_textview);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
            messageButton.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    Intent busActivityIntent = new Intent(view.getContext(), InsideBusActivity.class);
                    String  string=nameTextView.getText().toString();
                    busActivityIntent.putExtra("Carreira",string);
                    view.getContext().startActivity(busActivityIntent);

                    System.out.println("BUTTON CLICKED:" + string);
                }
            });
        }


    }
    // Store a member variable for the contacts
    private List<Bus> mBuses;

    // Pass in the contact array into the constructor
    public BusesAdapter(List<Bus> buses) {
        mBuses = buses;
    }
    @Override
    public BusesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View busView = inflater.inflate(R.layout.item_bus, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(busView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(BusesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Bus bus = mBuses.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(Integer.toString(bus.getCarreira()));
        Button button = holder.messageButton;
        button.setText("ESCOLHER");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mBuses.size();
    }

}
