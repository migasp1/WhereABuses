package com.example.whereabuses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_Room extends AppCompatActivity {
    private Button btn_send_mdg;
    private EditText input_msg;
    private TextView chat_conversation;
    private String user_name, room_name;
    private DatabaseReference root;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        btn_send_mdg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView1);
        Bundle bundle = getIntent().getExtras();
        user_name = bundle.getString("user_name");
        input_msg.setBackgroundColor(Color.parseColor("#FFA500"));
        room_name = bundle.getString("room_name");
        setTitle("Room - " + room_name);
        root = FirebaseDatabase.getInstance().getReference().child(room_name);
        btn_send_mdg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String,Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String,Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());
                message_root.updateChildren(map2);
                input_msg.setText("");
            }
        });
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private String chat_msg, chat_user_name;
    private void append_chat_conversation (DataSnapshot d){
            Iterator i = d.getChildren().iterator();
            while(i.hasNext()){
                    chat_msg = (String) ((DataSnapshot) i.next()).getValue();
                    chat_user_name = (String) ((DataSnapshot) i.next()).getValue();
                    chat_conversation.append(chat_user_name + " : "+chat_msg + "\n") ;

            }
    }
}