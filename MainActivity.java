package com.astakos.not.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.astakos.not.R;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.view.KeyEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class MainActivity extends AppCompatActivity
{
    
	private ImageView logo;
	private Button giris;
	private EditText ePosta;
	private EditText sifre;
	private TextView kayit;
	private TextView sifremiUnuttum;


	private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;
	


    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		giris = (Button) findViewById(R.id.activity_mainButtonGiris);
		ePosta = (EditText) findViewById(R.id.activity_mainEditTextEposta);
		sifre = (EditText) findViewById(R.id.activity_mainEditTextSifre);
		kayit = (TextView) findViewById(R.id.activity_mainTextViewKayitOl);
		sifremiUnuttum = (TextView) findViewById(R.id.activity_mainTextViewSifremiUnuttum);

		getSupportActionBar().setTitle("Oturum Aç");


		logo = (ImageView) findViewById(R.id.imageViewGiris);

		RequestOptions requestOptions = new RequestOptions();
		requestOptions.centerCrop();
		requestOptions.circleCrop();

		Glide.with(this)
            .setDefaultRequestOptions(requestOptions)
			.load(R.raw.kalp) 
			.into(logo);



		mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
		
		try
		{
			if (firebaseUser.getDisplayName() != null)
			{
				Intent i = new Intent(this, ChatActivity.class);
				startActivity(i);
				finish();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}




		sifremiUnuttum.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					dialogEdit();


				}
			});


		kayit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					Intent i = new Intent(getApplicationContext(), KayitActivity.class);
					startActivity(i);

				}
			});

        giris.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{

					userName = ePosta.getText().toString();
					userPassword = sifre.getText().toString();
					if (userName.isEmpty() || userPassword.isEmpty())
					{

						Toast.makeText(getApplicationContext(), "Lütfen gerekli alanları doldurunuz!", Toast.LENGTH_SHORT).show();

					}
					else
					{ 
					
					

						login();
						
					}
				}
			});

    }



	private void login()
	{

        mAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(MainActivity.this,
			new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task)
				{
					if (task.isSuccessful())
					{

						if (checkifEmailVerified())
						{
                        
							Intent i = new Intent(MainActivity.this, ChatActivity.class);
							startActivity(i);
							finish();

						}
						else
						{
							mAuth.signOut();
							Toast.makeText(getApplicationContext(), "Önce Email adresinizi doğrulayın" , Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});}


	private boolean checkifEmailVerified()
	{
		return mAuth.getCurrentUser().isEmailVerified();
	}


	public void sendPasswordReset(String ePosta)
	{
        // [START send_password_reset]

        mAuth.sendPasswordResetEmail(ePosta)
			.addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task)
				{
					if (task.isSuccessful())
					{
						Toast.makeText(getApplicationContext(), "Şifre Mail adresinize gönderildi" , Toast.LENGTH_LONG).show();
					}
				}
			});
        // [END send_password_reset]


    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if (keyCode == event.KEYCODE_BACK)
		{
			moveTaskToBack(true);
			finish();
		}
		else
		{

		}
		return super.onKeyDown(keyCode, event);

	}

	private void sendDialogDataToActivity(String data)
    {
		try
		{
			if (data != null)
			{
				sendPasswordReset(data);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Lütfen Email adresi giriniz" , Toast.LENGTH_LONG).show();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Lütfen Email adresi giriniz" , Toast.LENGTH_LONG).show();

		}
    }


	public void dialogEdit()
	{

		AlertDialog.Builder builder
			= new AlertDialog.Builder(this);
		builder.setTitle("Şifre Sıfırla");

		// set the custom layout
		final View customLayout
			= getLayoutInflater()
			.inflate(
			R.layout.custom_layout,
			null);
		builder.setView(customLayout);

		// add a button
		builder
			.setPositiveButton(
			"TAMAM",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
					DialogInterface dialog,
					int which)
				{

					// send data from the
					// AlertDialog to the Activity
					EditText editText
						= customLayout
						.findViewById(
						R.id.editTextAlertDialog);
					sendDialogDataToActivity(
						editText
						.getText()
						.toString());
				}
			});

		// create and show
		// the alert dialog
		AlertDialog dialog
			= builder.create();
		dialog.show();
	}



}
