package com.beacon.drunkengranite.forwardspam.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beacon.drunkengranite.forwardspam.R;

import java.util.ArrayList;

/**
 * Created by drunkengranite on 1/25/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.PhoneDisplay>{



    private ArrayList<String> phoneNumbers;

    private removeNumber delegate;

    public interface removeNumber{
        void removeNumber(String input);
    }

    public HomeAdapter(ArrayList<String> arrayList, removeNumber delegate){
        this.phoneNumbers = arrayList;
        this.delegate = delegate;
    }


    @Override
    public PhoneDisplay onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_display_added, null);
        return new PhoneDisplay(rootView);
    }

    @Override
    public void onBindViewHolder(PhoneDisplay holder, final int position) {
        final String number = phoneNumbers.get(position);
        holder.render(number);
        holder.removeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.removeNumber(number);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneNumbers.size();
    }

    static class PhoneDisplay extends RecyclerView.ViewHolder{

        TextView number;
        Button removeNumber;

        PhoneDisplay(View itemView) {
            super(itemView);
            this.number = (TextView) itemView.findViewById(R.id.number);
            this.removeNumber = (Button) itemView.findViewById(R.id.removeButton);
        }
        void render(String phoneNumber){
            number.setText(phoneNumber);
        }
    }
}
