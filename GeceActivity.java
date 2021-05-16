package com.astakos.not.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.astakos.not.R;
import android.widget.ImageButton;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;


public class GeceActivity extends AppCompatActivity
{
ImageButton buton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gece);
		
		getSupportActionBar().setTitle("Gece Modu");
		
		buton = findViewById(R.id.activity_geceImageView);
		SharedPreferences sharedPreferences  = getSharedPreferences("sharedPrefs", MODE_PRIVATE); 

        final SharedPreferences.Editor editor   = sharedPreferences.edit(); 

        final boolean isDarkModeOn  = sharedPreferences .getBoolean("isDarkModeOn", false); 



        // When user reopens the app 

        // after applying dark/light mode 

        if (isDarkModeOn)
		{ 

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); 

            buton.setImageResource(R.drawable.gunduz);

        } 

        else
		{ 

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); 
			buton.setImageResource(R.drawable.gece);
			
        } 


        buton.setOnClickListener( new View.OnClickListener() { 
				@Override

                public void onClick(View view) 

                { 
                    if (isDarkModeOn)
					{ 

                        // if dark mode is on it 

                        // will turn it off 

                        AppCompatDelegate .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); 

                        // it will set isDarkModeOn 
                        editor.putBoolean("isDarkModeOn", false); 

						editor.apply(); 
                        // change text of Button 

                        buton.setImageResource(R.drawable.gece);
						
						Intent i = new Intent(getApplicationContext(),MainActivity.class);
						startActivity(i);

                    } 

                    else
					{ 
                        // if dark mode is off 

                        // it will turn it on 

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); 
                        // it will set isDarkModeOn 

                        // boolean to true 
                        editor.putBoolean("isDarkModeOn", true); 
						editor.apply(); 

                        // change text of Button 

                        buton.setImageResource(R.drawable.gunduz);
						
				        Intent i = new Intent(getApplicationContext(),MainActivity.class);
				        startActivity(i);

                    } 

                } 

            }); 
		
	}
	
}
