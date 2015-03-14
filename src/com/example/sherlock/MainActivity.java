package com.example.sherlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageView image = (ImageView) findViewById(R.id.welcomeImageView);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setContentView(R.layout.add_mission);
            }
        });
        
        
       /* Button addMisson = (Button) findViewById(R.id.addMissionButton);
        addMisson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setContentView(R.layout.add_mission);
            }

        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
