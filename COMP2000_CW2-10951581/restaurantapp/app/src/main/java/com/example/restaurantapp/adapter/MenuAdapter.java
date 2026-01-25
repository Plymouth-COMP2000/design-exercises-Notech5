package com.example.restaurantapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.activities.MenuActivity;
import com.example.restaurantapp.activities.EditMenuItemActivity;
import com.example.restaurantapp.activities.ViewMenuItemActivity;
import com.example.restaurantapp.data.AppDatabase;
import com.example.restaurantapp.model.CustomerMenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<CustomerMenuItem> menuList;
    private boolean isStaff;
    private Context context;

    public MenuAdapter(Context context, List<CustomerMenuItem> menuList, boolean isStaff) {
        this.context = context;
        this.menuList = menuList;
        this.isStaff = isStaff;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        CustomerMenuItem item = menuList.get(position);

        holder.name.setText(item.name);
        holder.price.setText(String.format("$%.2f", item.price));

        //enforce user roles
        if (isStaff) {

            //only show edit and delete buttons for staff
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.viewButton.setVisibility(View.GONE);

            //edit button listener
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditMenuItemActivity.class);
                intent.putExtra("menuId", item.id);
                context.startActivity(intent);
            });

            //delete button listener
            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Menu Item")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            //run on thread to avoid freezing
                            new Thread(() -> {
                                AppDatabase.getInstance(context).menuDao().delete(item);

                                //update UI on main
                                ((MenuActivity) context).runOnUiThread(() -> {
                                    menuList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, menuList.size());
                                });
                            }).start();
                        })

                        //show dialogue box
                        .setNegativeButton("No", null)
                        .show();
            });

        //user is customer
        } else {

            //hide edit and delete buttons
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

            //show view button
            holder.viewButton.setVisibility(View.VISIBLE);

            //view button goes to screen which allows user
            //to view the full details  of a menu item
            holder.viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewMenuItemActivity.class);

                //with the selected menu item
                intent.putExtra("menuId", item.id);
                context.startActivity(intent);
            });
        }
    }

    //get menu length
    @Override
    public int getItemCount() {
        return menuList.size();
    }

    //update menu screen
    public void updateMenu(List<CustomerMenuItem> newMenu) {
        this.menuList = newMenu;
        notifyDataSetChanged();
    }

    //menu view holder layout for recycler view
    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button editButton, deleteButton, viewButton;

        //name, price, edit button, delete button, view button (left to right)
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.menuName);
            price = itemView.findViewById(R.id.menuPrice);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}