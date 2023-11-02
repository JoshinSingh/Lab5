package com.cst3104.practice;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ChatAdapter myAdapter;
    private ArrayList<Message> messageBar;

    @Override
    protected void onCreate(Bundle p) {
        super.onCreate(p);
        setContentView(R.layout.activity_main);
        Button sendBtn = findViewById(R.id.button1);
        Button receiveBtn = findViewById(R.id.button2);
        EditText editText = findViewById(R.id.typeHere);
        ListView listView = findViewById(R.id.list);
        messageBar = new ArrayList<>();
        myAdapter = new ChatAdapter();
        listView.setAdapter( myAdapter ) ;

        sendBtn.setOnClickListener( click ->{
            String msgTyped = editText.getText().toString();
            boolean sent = true;
            if ( !msgTyped.isEmpty()) {
                messageBar.add(new Message(msgTyped, sent));
                editText.setText("");
                myAdapter.notifyDataSetChanged();
            }
        });

        receiveBtn.setOnClickListener( click ->{
            String msgTyped = editText.getText().toString();
            boolean received = false;
            if ( !msgTyped.isEmpty()) {
                messageBar.add(new Message(msgTyped, received));
                editText.setText("");
                myAdapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertMsg = new AlertDialog.Builder(MainActivity.this);
                alertMsg.setTitle("Do you want to delete this message?");
                alertMsg.setMessage("The selected row is: " + position);
                alertMsg.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (position >= 0 && position < messageBar.size()) {
                            messageBar.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
                alertMsg.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertMsg.create().show();
                return true;
            }
        });

    }

    public class ChatAdapter extends BaseAdapter {
        @Override
        public int getCount() {return messageBar.size(); }

        @Override
        public Object getItem(int position) { return "This is row: " + position; }

        @Override
        public long getItemId(int position) { return position;   }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflaterObject = getLayoutInflater();
            Message thisRow = messageBar.get(position);
            View newView;
            if (thisRow.isCheck()) {
                newView = inflaterObject.inflate(R.layout.sendlayout, parent, false);
            } else {
                newView = inflaterObject.inflate(R.layout.receivelayout, parent, false);
            }
            TextView msgView = newView.findViewById(R.id.viewMsg);
            msgView.setText( thisRow.getText() );
            return newView;
        }
    }

    public class Message {
        public String text; public boolean isCheck;
        public Message(String text, boolean isCheck) { this.text = text; this.isCheck = isCheck; }
        public String getText() { return text; }
        public boolean isCheck() { return isCheck; }
    }

}