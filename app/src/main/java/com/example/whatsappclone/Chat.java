package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private Button buttonSendMessage;
    private ListView listChat;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private EditText edtMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
        listChat = (ListView) findViewById(R.id.listViewChat);
        arrayAdapter = new ArrayAdapter(Chat.this, android.R.layout.simple_expandable_list_item_1, arrayList);

        edtMessage = (EditText)findViewById(R.id.editTextMessage);
        arrayList = new ArrayList();

        arrayAdapter = new ArrayAdapter(Chat.this, android.R.layout.simple_expandable_list_item_1, arrayList);

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Chat");

        ArrayList<String> strCol = new ArrayList<>();
        strCol.add(ParseUser.getCurrentUser().getUsername());
        strCol.add(getIntent().getExtras().get("waTargetRecipient").toString());

        parseQuery.whereContainedIn("waSender", strCol);
        parseQuery.whereContainedIn("waTargetRecipient", strCol);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        arrayList.clear();
                        for (ParseObject msg : objects){
                            arrayList.add(msg.getString("waSender") + ": " + msg.getString("waMessage"));
                        }
                        listChat.setAdapter(arrayAdapter);
                    }
                }

            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMessage.getText().toString() != "") {
                    ParseObject parseObject = new ParseObject("Chat");
                    parseObject.put("waSender", ParseUser.getCurrentUser().getUsername());
                    parseObject.put("waTargetRecipient", getIntent().getExtras().get("waTargetRecipient").toString());
                    parseObject.put("waMessage", edtMessage.getText().toString());

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ArrayList<String> strCol = new ArrayList<>();
                            strCol.add(ParseUser.getCurrentUser().getUsername());
                            strCol.add(getIntent().getExtras().get("waTargetRecipient").toString());

                            parseQuery.whereContainedIn("waSender", strCol);
                            parseQuery.whereContainedIn("waTargetRecipient", strCol);
                            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null){
                                        if (objects.size() > 0){
                                            boolean wasChange = false;
                                            for (ParseObject msg : objects){
                                                if (!arrayList.contains(msg.getString("waSender") + ": " + msg.getString("waMessage"))) {
                                                    arrayList.add(msg.getString("waSender") + ": " + msg.getString("waMessage"));
                                                    wasChange = true;

                                                }
                                            }
                                            if (wasChange){
                                                listChat.deferNotifyDataSetChanged();
                                            }

                                        }
                                    }

                                }
                            });

                        }
                    });
                } else {
                    FancyToast.makeText(Chat.this, "Your message is empty!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }

            }
        });

    }







}