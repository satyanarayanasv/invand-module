package com.satya.invandmodule.time_picker;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.satya.invandmodule.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by sivakrishna on 11/10/17.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<LabelerData> dateDataList;
    public ITimerInterface anInterface;

    private boolean isHours = true;
    public static  int VIEW_TYPE_PADDING = 1;
    public static  int VIEW_TYPE_ITEM = 2;
    private int paddingWidthDate = 0;

    private int selectedItem = -1;

    public TimeAdapter(ArrayList<LabelerData> dateData, int paddingWidthDate) {
        this.dateDataList = dateData;
        this.paddingWidthDate = paddingWidthDate;

    }


    public void setSelecteditem(int selecteditem ,  boolean isHours) {
        this.isHours = isHours;
        this.selectedItem = selecteditem;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_item_,
                    parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_item_,
                    parent, false);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            layoutParams.width = paddingWidthDate;
            view.setLayoutParams(layoutParams);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LabelerData labelerDate = dateDataList.get(position);
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            holder.tvDate.setText(labelerDate.getNumber());
            holder.tvDate.setVisibility(View.VISIBLE);

            Log.d(TAG, "default " + position + ", selected " + selectedItem);
            if (position == selectedItem) {
                holder.tvDate.setTextColor(Color.WHITE);
                holder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, isHours ? 20 : 16);
                Log.d(TAG, "center" + position);


            } else {
                holder.tvDate.setTextColor(Color.LTGRAY);
                holder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, isHours ? 18 : 14);
            }
        } else {
            holder.tvDate.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        LabelerData labelerDate = dateDataList.get(position);
        if (labelerDate.getType() == VIEW_TYPE_PADDING) {
            return VIEW_TYPE_PADDING;
        } else {
            return VIEW_TYPE_ITEM;
        }

    }

    @Override
    public int getItemCount() {
        return dateDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView)itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anInterface.itemSelectedInAdapter(getAdapterPosition(),isHours);
                }
            });
        }
    }
}
