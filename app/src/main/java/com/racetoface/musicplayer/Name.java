package com.racetoface.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

public class Name extends AppCompatActivity {
    Button START;
    EditText NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        NAME=(EditText)findViewById(R.id.Name);
        START=(Button)findViewById(R.id.START);

        START.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=NAME.getText().toString();

                if (name.equals(""))
                {
                    Toast.makeText(Name.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                }

                else {
                    SharedPreferences sharedPreferences=getSharedPreferences("NAMESTORE",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("NAME",name);
                    editor.commit();


                    Intent intent=new Intent(Name.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }
}
