package com.astakos.not.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.astakos.not.R;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import androidx.annotation.NonNull;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.app.Activity;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import androidx.core.app.ActivityCompat;


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

import java.io.IOException;
import java.util.UUID;

public class AvatarActivity extends AppCompatActivity
{

	private ImageView avatar;
	private Button kaydet;

	private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 8;

	private FirebaseStorage storage;
    private StorageReference storageReference;

	private FirebaseAuth mAuth;

	private static final int READ_EXTERNAL_STORAGE = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_activity);
		
		getSupportActionBar().setTitle("Profil");

		avatar = (ImageView) findViewById(R.id.avatar_activityImageView);

		kaydet = (Button) findViewById(R.id.avatar_activityButtonKaydet);

		mAuth = FirebaseAuth.getInstance();
		storage = FirebaseStorage.getInstance();
		storageReference = storage.getReference();
		
		int permissioncheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		if (permissioncheck == PackageManager.PERMISSION_GRANTED) {
			// Galeri dosyası

		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
		}


		if (Build.VERSION.SDK_INT >= 23) { //do your check here }
		
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.centerCrop();
		requestOptions.circleCrop();
		requestOptions.circleCropTransform();

		Glide.with(AvatarActivity.this)
			.setDefaultRequestOptions(requestOptions)
			.load(R.drawable.resim) 
			.into(avatar);
		
		

		avatar.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					chooseImage();

				}
			});


		kaydet.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{

					try
					{


						uploadImage();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

				}
			});}
}


    private void uploadImage()
	{

        if (filePath != null)
		{

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Yükleniyor....");
            progress.show();
			StorageReference storageRef = storage.getReference().child("users").child(mAuth.getCurrentUser().getUid());
            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
					{


						progress.dismiss();
						Toast.makeText(AvatarActivity.this, "Yüklendi", Toast.LENGTH_SHORT).show();

						String imageUrl = taskSnapshot.getDownloadUrl().toString();
                        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();


						RequestOptions requestOptions = new RequestOptions();
						requestOptions.centerCrop();
						requestOptions.circleCrop();
						requestOptions.circleCropTransform();

						Glide.with(AvatarActivity.this)
							.setDefaultRequestOptions(requestOptions)
							.load(imageUrl) 
							.into(avatar);


						UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
							.setPhotoUri(taskSnapshot.getDownloadUrl()).build();

						user.updateProfile(profileUpdates)
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task)
								{
									if (task.isSuccessful())
									{

										Intent i = new Intent(getApplicationContext(), MainActivity.class);
										startActivity(i);
										finish();



									}
								}
							}
						);




					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e)
					{
						progress.dismiss();
						Toast.makeText(AvatarActivity.this, "Hata " + e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
					{
						double progres_time = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
						progress.setMessage("Yükleme " + (int)progres_time + " %");
					}
				});
		}
	}
	
	public boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
		           == PackageManager.PERMISSION_GRANTED) {

				return true;
			} else {

				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
				return false;
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation

			return true;
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == READ_EXTERNAL_STORAGE){
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
				// Galeri dosyası

			}else {
				Toast.makeText(this,"izin gerekli..!",Toast.LENGTH_SHORT).show();
			}
		}
	}
	


    private void chooseImage()
	{
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
			data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
			
                avatar.setImageBitmap(bitmap);
            }
			catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }




}
