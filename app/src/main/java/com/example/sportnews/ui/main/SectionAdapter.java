package com.example.sportnews.ui.main;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportnews.R;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.VH> {
    List<String> sectionList;
    Context context;

    public SectionAdapter(Context context) {
        this.context = context;
        if (sectionList == null) sectionList = new ArrayList<>();
    }

    public void setSectionList(List<String> sectionList) {

        this.sectionList = sectionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SectionAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionAdapter.VH(LayoutInflater.from(context).inflate(R.layout.section_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.VH holder, int position) {
        String countryName = sectionList.get(position);
        holder.sectionName.setText(countryName);
        GradientDrawable companyBackground = (GradientDrawable) holder.sectionName.getBackground();
        companyBackground.setStroke(6, context.getResources().getColor(R.color.colorBlack1), 40f, 8f);
        holder.sectionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null)
                    onAdapterClickListener.onSectionClick(countryName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView sectionName;

        public VH(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_item_name);
        }
    }

    public OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    public interface OnAdapterClickListener {
        void onSectionClick(String sectionName);
    }
}
