package com.beacon.drunkengranite.forwardspam.Async;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.stream.Collectors;

import static java.security.AccessController.getContext;

/**
 * Created by drunkengranite on 1/27/17.
 */

public class GetPhoneSuggestions extends AsyncTask<Context, Void, ArrayList<String>> {


    public onComplete delegate;

    public interface onComplete {
        void getCallback(ArrayList<String> numbers);
    }

    public GetPhoneSuggestions(onComplete delegate) {
        this.delegate = delegate;
    }


    @Override
    protected ArrayList<String> doInBackground(Context... contexts) {
        ContentResolver cursorMaker = contexts[0].getContentResolver();
        ArrayList<String> callLogNumbers = new ArrayList<>();

        Cursor callsCursor = cursorMaker.query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");

        Cursor contactsCursor = cursorMaker.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (ActivityCompat.checkSelfPermission(contexts[0], Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return new ArrayList<>();
        }

        /*
            gets full call log
         */
        int contactsNumberColumn = callsCursor.getColumnIndex(CallLog.Calls.NUMBER);

        while (callsCursor.moveToNext()) {
            callLogNumbers.add(callsCursor.getString(contactsNumberColumn));
        }
        callsCursor.close();


        int idColumn = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID);

        if (contactsCursor.moveToFirst()) {
            while (contactsCursor.moveToNext()) {

                String currentContactID = contactsCursor.getString(idColumn);

                        /*
                            now to match phone to ID
                         */

                Cursor numberCursor = cursorMaker.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ currentContactID, null, null);

                //just in case a contact has more than one phone contact
                while(numberCursor.moveToNext()){
                    String cNumber = numberCursor.getString(
                            numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    if(callLogNumbers.contains(cNumber)){
                        callLogNumbers.remove(cNumber);
                    }
                }
                numberCursor.close();
            }
        }
        contactsCursor.close();



        return callLogNumbers;
    }
    @Override
    protected void onPostExecute(ArrayList<String> input){
        delegate.getCallback(input);
    }
}
