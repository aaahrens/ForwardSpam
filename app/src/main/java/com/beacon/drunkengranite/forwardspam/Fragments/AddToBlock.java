package com.beacon.drunkengranite.forwardspam.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.drunkengranite.forwardspam.Adapters.SuggestionAdapter;
import com.beacon.drunkengranite.forwardspam.Async.GetPhoneSuggestions;
import com.beacon.drunkengranite.forwardspam.MainActivity;
import com.beacon.drunkengranite.forwardspam.R;
import com.beacon.drunkengranite.forwardspam.logic.PhoneLogic;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToBlock extends Fragment implements SuggestionAdapter.addNumber{


    public onResponse delegate;
    RecyclerView recyclerView;
    ArrayList<String> phoneSuggestions;
    SuggestionAdapter suggestionAdapter;
    private static final int PERMISSIONS = 200;


    public interface onResponse {
        void addToList(String number);
    }

    public AddToBlock() {

    }

    public static AddToBlock createNewAddBlock(onResponse delegate) {
        AddToBlock toReturn = new AddToBlock();
        toReturn.delegate = delegate;
        return toReturn;
    }

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        getCallSuggestions();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View toReturn = inflater.inflate(R.layout.fragment_add_to_block, container, false);
        ImageButton backButton = (ImageButton) toReturn.findViewById(R.id.back_button);
        final MainActivity main = (MainActivity) getActivity();
        final EditText phoneInput = (EditText) toReturn.findViewById(R.id.input);
        recyclerView = (RecyclerView) toReturn.findViewById(R.id.phone_suggestions);


        phoneSuggestions = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(phoneSuggestions, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(suggestionAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.inflateFAB();

                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        phoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case 5: {
                        String number = String.valueOf(textView.getText());
                        if (PhoneLogic.isPhoneNumber(number)) {
                            delegate.addToList(number);
                            textView.setText("");
                            Toast.makeText(main, "Blocking: " + number, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Toast.makeText(main, "is not valid phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                System.out.println(i);
                return false;
            }
        });
        return toReturn;
    }


    private void getCallSuggestions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getContext().checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, PERMISSIONS);

        }else{
            new GetPhoneSuggestions(new GetPhoneSuggestions.onComplete() {
                @Override
                public void getCallback(ArrayList<String> numbers) {
                    phoneSuggestions.addAll(numbers);
                    suggestionAdapter.notifyDataSetChanged();
                }
            }).execute(getContext());

        }
    }




    @Override
    public void addNumber(String input) {
        delegate.addToList(input);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS) {
            if(grantResults.length == 2){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getCallSuggestions();
                }
            }
        }
    }
}
