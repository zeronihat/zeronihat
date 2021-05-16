package com.astakos.not.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.astakos.not.R;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.app.Activity;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.net.Uri;
import android.graphics.Bitmap;
import java.io.IOException;
import android.provider.MediaStore;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class KayitActivity extends AppCompatActivity
{
	private Button kayit;
	private EditText ePosta;
	private EditText sifre;
	private EditText ad;
	private ImageView logo;

	private FirebaseAuth mAuth;
	private String userName;
	private String userPassword;
	private String takmaAd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mAuth = FirebaseAuth.getInstance();

		getSupportActionBar().setTitle("Kayıt Ol");

		kayit = (Button) findViewById(R.id.activity_registerButtonKayit);
		ePosta = (EditText) findViewById(R.id.activity_registerEditTextEposta);
		sifre = (EditText) findViewById(R.id.activity_registerEditTextSifre);
		ad = (EditText) findViewById(R.id.activity_registerEditTextAd);
		logo = (ImageView) findViewById(R.id.imageViewRegister);


		RequestOptions requestOptions = new RequestOptions();
		requestOptions.centerCrop();
		requestOptions.circleCrop();

		Glide.with(this)
            .setDefaultRequestOptions(requestOptions)
			.load(R.raw.kayitlogo) 
			.into(logo);
       
		kayit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{

					userName = ePosta.getText().toString();
					userPassword = sifre.getText().toString();
					takmaAd = ad.getText().toString();
					if (userName.isEmpty() || userPassword.isEmpty() || takmaAd.isEmpty())
					{

						Toast.makeText(getApplicationContext(), "Lütfen gerekli alanları doldurunuz!", Toast.LENGTH_SHORT).show();

					}
					else
					{

						registerFunc();

					}

				}
			});

	}

	private void registerFunc()
	{

		mAuth.createUserWithEmailAndPassword(userName, userPassword)
			.addOnCompleteListener(KayitActivity.this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(Task<AuthResult> task)
				{
					if (task.isSuccessful() && task.getResult() != null)
					{

						if (task.getResult().getUser() != null) task.getResult().getUser().sendEmailVerification();

						Toast.makeText(KayitActivity.this, "Üyelik Başarılı! Aktivasyon Linki Mailinize Gönderildi!", Toast.LENGTH_LONG).show();

						// Buraya yoruma almamın sebebi aktivasyon sonra giriş işlemi yapılmasını isteyebileceğiniz içindir
						//girisSonrasiIslemler();
						
						


						FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String isim = ad.getText().toString().trim();
						String avatar = "https://www.resimyukle.link/img/JstQ2.jpg";
						UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
						    
						    .setPhotoUri(Uri.parse(avatar))
							.setDisplayName(isim).build();
							
						    user.updateProfile(profileUpdates)
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task)
								{
									if (task.isSuccessful())
									{


									}
								}
							}

						);
						Intent i = new Intent(getApplicationContext(), AvatarActivity.class);
						startActivity(i);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}
}

	


		

	
	
