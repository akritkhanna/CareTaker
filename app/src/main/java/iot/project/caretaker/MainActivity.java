package iot.project.caretaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import iot.project.caretaker.interfacing_bridge.Server;

public class MainActivity extends AppCompatActivity {

    ImageView ctlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctlogo = (ImageView) findViewById(R.id.ct_logo);

        ctlogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MainActivity.this,Server.class);
                startActivity(i);
                return true;
            }
        });

    }
    public void onButtonClick(View v){
        if (v.getId() == R.id.quicktab_btn){
            Intent i = new Intent(MainActivity.this,Quicktab.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.reminder_btn){
            Intent i = new Intent(MainActivity.this,Reminder.class);
            startActivity(i);
        }

        else if (v.getId() == R.id.aboutme_btn){
            Intent i = new Intent(MainActivity.this,About_user.class);
            startActivity(i);
        }
    }
}
