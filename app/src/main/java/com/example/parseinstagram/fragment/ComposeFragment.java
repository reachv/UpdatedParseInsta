package com.example.parseinstagram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parseinstagram.LoginActivity;
import com.example.parseinstagram.MainActivity;
import com.example.parseinstagram.Post;
import com.example.parseinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;


public class ComposeFragment extends Fragment {
    EditText description;
    Button Take_Picture;
    ImageView ivImage;
    Button Submit;

    private File photoFile;
    private String photoFileName = "photo.jpg";


    public ComposeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose, container, false);

        description = view.findViewById(R.id.etDescription);
        ivImage = view.findViewById(R.id.ivImage);
        Take_Picture = view.findViewById(R.id.btnPicture);
        Submit = view.findViewById(R.id.btnSubmit);
        Take_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("here" ,"here");
                launchCamera();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descriptions = description.getText().toString();
                if(descriptions.isEmpty()){
                    Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivImage.getDrawable() == null){
                    Toast.makeText(getContext(), "No Image Taken", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(descriptions, currentUser, photoFile);
            }
        });

        return view;
    }
    public void OnViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if(intent.resolveActivity(getContext().getPackageManager()) != null){
            someActivityResultLauncher.launch(intent);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ComposeFragment");
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdir()){
            Log.d("ComposeFragment", "Failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);

    }

    private void savePost(String descriptions, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(descriptions);
        post.setUser(currentUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("ComposeFragment", "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i("ComposeFragment", "Post was saved successfully");
                description.setText("");
                ivImage.setImageResource(0);
            }
        });
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        ivImage.setImageBitmap(takenImage);
                    }else{
                        Toast.makeText(getContext(), "Picture not taken", Toast.LENGTH_SHORT).show();
                    }
                }
            });



}