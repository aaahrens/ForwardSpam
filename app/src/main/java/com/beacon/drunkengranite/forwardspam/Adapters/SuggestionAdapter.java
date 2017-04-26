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

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.PhoneDisplay>{



    private ArrayList<String> phoneNumbers;

    private addNumber delegate;

    public interface addNumber{
        void addNumber(String input);
    }

    public SuggestionAdapter(ArrayList<String> arrayList, addNumber delegate){
        this.phoneNumbers = arrayList;
        this.delegate = delegate;
    }



    @Override
    public PhoneDisplay onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_display_suggestion, null);
        return new PhoneDisplay(rootView);
    }

    @Override
    public void onBindViewHolder(PhoneDisplay holder, final int position) {
        final String number = phoneNumbers.get(position);
        holder.render(number);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.addNumber(number);
                phoneNumbers.remove(number);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneNumbers.size();
    }

    static class PhoneDisplay extends RecyclerView.ViewHolder{

        TextView name;
        public Button add;

        PhoneDisplay(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.suggest_number);
            add = (Button) itemView.findViewById(R.id.addPhone);
        }
        void render(String phoneNumber){
            name.setText(phoneNumber);
        }
    }
}
