package com.example.restaurantapp.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.activities.EditReservationActivity;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.Reservation;
import com.example.restaurantapp.R;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private final List<Reservation> reservations;

    public ReservationAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    //populate screen with viewholders
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the RecyclerView item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    //populate viewholers
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation r = reservations.get(position);

        //formatting
        holder.details.setText(r.surname + " " + r.date + " " + r.time + " (" + r.guests + " guests)");

        //edit button listener
        holder.edit.setOnClickListener(v -> {

            //start edit reservation activity
            Intent intent = new Intent(v.getContext(), EditReservationActivity.class);

            //pass reservation id to edit reservation
            intent.putExtra("id", r.id);
            v.getContext().startActivity(intent);
        });

        //check for cancel button pressed
        holder.cancel.setOnClickListener(v -> {

            //confirmation dialogue
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Cancel reservation")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        //run database operation in background
                        new Thread(() -> {
                            AppDatabase.getInstance(v.getContext())

                                    //delete reservation instance
                                    .reservationDao()
                                    .delete(r);
                        }).start();
                    })

                    //clears dialogue only
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    //gets number of reservations
    @Override
    public int getItemCount() {
        return reservations.size();
    }

    //define viewholder object
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView details;
        Button edit, cancel;

        //viewholder contents
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            details = itemView.findViewById(R.id.details);
            edit = itemView.findViewById(R.id.editBtn);
            cancel = itemView.findViewById(R.id.cancelBtn);
        }
    }
}