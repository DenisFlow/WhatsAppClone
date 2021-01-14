package com.example.whatsappclone;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsers extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView txtLoadingData;
    String sCurrentUser;
    Button buttonRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(WhatsAppUsers.this);

        buttonRefresh = findViewById(R.id.buttonRefresh);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(WhatsAppUsers.this, android.R.layout.simple_expandable_list_item_1, arrayList);
        sCurrentUser = ParseUser.getCurrentUser().getUsername();



        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", arrayList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0) {
                                for (ParseUser user: objects
                                     ) {
                                    arrayList.add(user.getUsername());
                                }
                                arrayAdapter.notifyDataSetChanged();

                            }
                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fnRefreshDataList();
    }

    public void fnRefreshDataList(){
        ParseQuery<ParseUser> parseQuery = new ParseUser().getQuery();

        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                if (e == null){
                    if (users.size() > 0){
                        arrayList.clear();
                        for (ParseUser user : users){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_item) {
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(WhatsAppUsers.this, LogIn.class);
                    startActivity(intent);
                    FancyToast.makeText(WhatsAppUsers.this, sCurrentUser + " log off", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    finish();
                }
            });
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WhatsAppUsers.this, Chat.class);
        intent.putExtra("waTargetRecipient", arrayList.get(position));
        startActivity(intent);
    }
}