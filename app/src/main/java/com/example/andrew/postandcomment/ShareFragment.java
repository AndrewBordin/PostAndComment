package com.example.andrew.postandcomment;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.andrew.postandcomment.model.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageview;
    private Button btnGetImage;
    private Button btnUploadImage;
    private EditText edtImageName;
    private ProgressBar progressBar;
    private Uri imageUri;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    private StorageTask uploadTask;

    public ShareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = user.getUid();

        btnGetImage = getActivity().findViewById(R.id.btnGetImage);
        btnUploadImage = getActivity().findViewById(R.id.btnUpload);
        edtImageName = getActivity().findViewById(R.id.edtImageName);
        progressBar = getActivity().findViewById(R.id.progressBar);
        imageview = getActivity().findViewById(R.id.imgShared);
        storageRef = FirebaseStorage.getInstance().getReference("uploads/" + userId);
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads/" + userId);

        btnGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();

            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getActivity().getApplicationContext(), "Upload is in progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadFile();
                }
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(imageUri != null){
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadTask = fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(getActivity().getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(edtImageName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                            String uploadId = databaseRef.push().getKey();
                            databaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progess = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progess);
                        }
                    });

        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(getActivity().getApplicationContext()).load(imageUri).into(imageview);
        }
    }
}
