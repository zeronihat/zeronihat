package com.astakos.not.activities;

import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.astakos.not.R;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import android.widget.ImageView;
import android.net.Uri;
import android.app.ProgressDialog;
import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.LinearLayout;
import android.provider.MediaStore;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.Color;
import android.widget.Button;
import android.content.Context;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import com.google.firebase.messaging.FirebaseMessaging;



public class ChatActivity extends AppCompatActivity
{

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
	private DatabaseReference databaseReferenceConnected;
	private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private ArrayList<Message> chatLists = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ListView listView;
    private ImageButton floatingActionButton;
	private ImageButton resim;
    private EditText inputChat;

	// avatar
	private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
	// resim
	private StorageReference storageRefcn;
	private FirebaseStorage storage;

	private  Uri filePath ;
    private final int PICK_IMAGE_REQUEST = 71;



    @Override
    protected void onCreate(Bundle savedInstanceState)
	{

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
		
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
		


        listView = (ListView)findViewById(R.id.chatListView);
        inputChat = (EditText)findViewById(R.id.inputChat);
        floatingActionButton = (ImageButton)findViewById(R.id.fab);
		resim = (ImageButton) findViewById(R.id.resimYukleButon);

		this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_action_bar);
		//getSupportActionBar().setElevation(0)

        db = FirebaseDatabase.getInstance();
		fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

		firebaseStorage = FirebaseStorage.getInstance();

		storage = FirebaseStorage.getInstance();
		storageRefcn = storage.getReference();
		
		

		try
		{
			enablePersistence();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        customAdapter = new CustomAdapter(getApplicationContext(), chatLists, fUser);
        listView.setAdapter(customAdapter);


		// referansa ulaşıp ilgili sohbetleri getirebilmemiz için gerekli 
		storageReference = firebaseStorage.getReference("users/");

		dbRef = db.getReference("Astakos/" + "/mesaj");


		databaseReferenceConnected = FirebaseDatabase.getInstance().getReference(".info/connected");
        databaseReferenceConnected.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot snapshot)
				{
					boolean connectedControl = snapshot.getValue(Boolean.class);

					View view = getSupportActionBar().getCustomView();
					ImageView name = view.findViewById(R.id.custom_action_barTextView);

					if (connectedControl)
					{
						name.setImageResource(R.drawable.personel_green);
					}
					else
					{
						name.setImageResource(R.drawable.personel_red);
					}
				}

				@Override
				public void onCancelled(@NonNull DatabaseError error)
				{

				}
			});

        dbRef.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					chatLists.clear();
					for (DataSnapshot ds : dataSnapshot.getChildren())
					{
						Message message = ds.getValue(Message.class);
						chatLists.add(message);
						//Log.d("VALUE",ds.getValue(Message.class).getMesajText());
					}
					customAdapter.notifyDataSetChanged();

				}

				@Override
				public void onCancelled(DatabaseError databaseError)
				{

				}
			});

		resim.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					chooseImage();
				}
			});

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{

					if (inputChat.getText().length() >= 1)
					{   


						long msTime = System.currentTimeMillis();
						Date curDateTime = new Date(msTime);
						SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm");
						String dateTime = formatter.format(curDateTime);
						Message message = new Message(inputChat.getText().toString(), fUser.getDisplayName(), fUser.getEmail(), fUser.getPhotoUrl().toString(), null, dateTime, fUser.getUid());
						dbRef.push().setValue(message);
						inputChat.setText("");

					}
					else
					{

					}
				}


			});
    }


	private void enablePersistence()
	{
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }


	public void deleteUser()
	{
        // [START delete_user]

        fUser.delete()
			.addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task)
				{
					if (task.isSuccessful())
					{
						Toast.makeText(getApplicationContext(), "Kullanıcı hesabınız silindi", Toast.LENGTH_LONG).show();
						fAuth.signOut();
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(i);
						finish();
					}
				}
			});
        // [END delete_user]
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
			upload();
			String file = filePath.toString().trim();

			long msTime = System.currentTimeMillis();
			Date curDateTime = new Date(msTime);
			SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm");
			String dateTime = formatter.format(curDateTime);

			Message message = new Message(null, fUser.getDisplayName(), fUser.getEmail(), fUser.getPhotoUrl().toString(), file, dateTime, fUser.getUid());
			dbRef.push().setValue(message);

        }
    }

	private void upload()
	{

		if (filePath != null)
		{

			// Code for showing progressDialog while uploading
			final	ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
			progressDialog.setTitle("Uploading...");
			progressDialog.show();

			// Defining the child of storageReference
			StorageReference ref = storageRefcn.child("images/" + fAuth.getCurrentUser().getUid().toString());

			// adding listeners on upload
			// or failure of image
			ref.putFile(filePath)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

					@Override
					public void onSuccess(
						UploadTask.TaskSnapshot taskSnapshot)
					{

						// Image uploaded successfully
						// Dismiss dialog
						progressDialog.dismiss();
						Toast.makeText(ChatActivity.this, "Resim Yüklendi", Toast.LENGTH_SHORT).show();
					}
				})

				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e)
					{

						// Error, Image not uploaded
						progressDialog.dismiss();
						Toast.makeText(ChatActivity.this, "Hata " + e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				})
				.addOnProgressListener(
				new OnProgressListener<UploadTask.TaskSnapshot>() {

					// Progress Listener for loading
					// percentage on the dialog box
					@Override
					public void onProgress(
						UploadTask.TaskSnapshot taskSnapshot)
					{
						double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
						progressDialog.setMessage("Yükleniyor " + (int)progress + "%");
					}
				});

		}

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_ayarlar , menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{

			case R.id.item1:
				// profil
				Intent i = new Intent(getApplicationContext(), AvatarActivity.class);
				startActivity(i);

				return true;

			case R.id.item2:
				//gece modu
				Intent intent = new Intent(getApplicationContext(), GeceActivity.class);
				startActivity(intent);
				return true;

			case R.id.item3:
				//çıkış
				fAuth.signOut();
				Intent in = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(in);
				finish();

				return true;

			case R.id.item4:
				//hesap sil
				deleteUser();

				return true;
		}

        return super.onOptionsItemSelected(item);
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


}
