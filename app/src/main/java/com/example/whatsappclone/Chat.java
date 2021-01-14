package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
//        getIntent().getExtras();
//        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
//        String[] users = {ParseUser.getCurrentUser().getUsername(), getIntent().getExtras().get("waTargetRecipient").toString()};
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

            }
        });

    }







}