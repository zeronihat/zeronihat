package com.astakos.not.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import android.widget.TextView;
import com.astakos.not.R;
import java.util.Random;
import android.graphics.Color;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;

import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;


/**
 * Created by alper on 16/07/2017.
 */

public class CustomAdapter extends ArrayAdapter<Message>
{

    private FirebaseUser firebaseUser;
	Context c;

    public CustomAdapter(Context context, ArrayList<Message> chatList, FirebaseUser firebaseUser)
	{
        super(context, 0, chatList);
        this.firebaseUser = firebaseUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
	{



        Message message = getItem(position);
        if (firebaseUser.getEmail().equalsIgnoreCase(message.getEposta()))
		{

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_item_layout, parent, false);

            ImageView avatar = (ImageView) convertView.findViewById(R.id.left_item_layoutImageViewAvatar);
			ImageView resim = (ImageView) convertView.findViewById(R.id.left_item_layoutImageViewResim);

			TextView txtEposta = (TextView) convertView.findViewById(R.id.txtEpostaLeft);
            TextView txtUser = (TextView) convertView.findViewById(R.id.txtUserLeft);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessageLeft);
            TextView txtTime = (TextView) convertView.findViewById(R.id.txtTimeLeft);
			
       
            txtEposta.setText(message.getEposta());
            txtUser.setText(message.getGonderici());
            txtMessage.setText(message.getMesajText());
            txtTime.setText(message.getZaman());


			// Not avatar 
			//Picasso.with(context).load(uri).fit().centerCrop().into(holder.userImage);
			RequestOptions requestOptions = new RequestOptions();
			requestOptions.centerCrop();
			requestOptions.circleCrop();
			requestOptions.circleCropTransform();


			Glide.with(convertView)
				.setDefaultRequestOptions(requestOptions)
				.load(message.getAvatar())
				.into(avatar);


			// not resim

			RequestOptions request = new RequestOptions();
			request.fitCenter();


			Glide.with(convertView)
				.setDefaultRequestOptions(request)
				.load(message.getResim())
				.into(resim);
			

			if(message.getMesajText() == null){
				txtMessage.setVisibility(View.GONE);
			}else{
				txtMessage.setVisibility(View.VISIBLE);
			}


        }
		else
		{


			convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_item_layout, parent, false);

            ImageView avatar = (ImageView) convertView.findViewById(R.id.right_item_layoutImageViewAvatar);
			ImageView resim = (ImageView) convertView.findViewById(R.id.right_item_layoutImageViewResim);
			TextView txtEposta = (TextView) convertView.findViewById(R.id.txtEpostaRight);
            TextView txtUser = (TextView) convertView.findViewById(R.id.txtUserRight);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessageRight);
            TextView txtTime = (TextView) convertView.findViewById(R.id.txtTimeRight);
			

            txtEposta.setText(message.getEposta());
            txtUser.setText(message.getGonderici());
            txtMessage.setText(message.getMesajText());
            txtTime.setText(message.getZaman());


			//Picasso.with(context).load(uri).fit().centerCrop().into(holder.userImage);
			RequestOptions requestOptions = new RequestOptions();
			requestOptions.centerCrop();
			requestOptions.circleCrop();
			requestOptions.circleCropTransform();



			Glide.with(convertView)
				.setDefaultRequestOptions(requestOptions)
				.load(message.getAvatar())
				.into(avatar);


			// not resim

			RequestOptions request = new RequestOptions();
			request.fitCenter();


			Glide.with(convertView)
				.setDefaultRequestOptions(request)
				.load(message.getResim())
				.into(resim);
				
				
			if(message.getMesajText() == null){
				txtMessage.setVisibility(View.GONE);
			}else{
				txtMessage.setVisibility(View.VISIBLE);
			}

        }

        return convertView;
    }
}
