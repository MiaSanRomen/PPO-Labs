package com.example.a3lab_holidays;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    EditText inputUsername, inputEmail, inputPassword;
    TextView lblName, lblVictories;
    Button imageBtn, saveBtn;
    ImageView avatar;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab-holidays-default-rtdb.firebaseio.com/");;
    DatabaseReference reference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lblName = findViewById(R.id.txt_name);
        lblVictories = findViewById(R.id.txt_victories);
        inputUsername = findViewById(R.id.username_profile);
        inputEmail = findViewById(R.id.email_profile);
        inputPassword = findViewById(R.id.password_profile);
        imageBtn = findViewById(R.id.btn_image_change);
        saveBtn = findViewById(R.id.btn_save_all);
        avatar = findViewById(R.id.img_user);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        fillData();



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = rootNode.getReference("users");
                if(isEmailChanged() || isPasswordChanged() || isUsernameChanged() || isImageChanged())
                {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(ProfileActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile();
                    }
                    fillData();
                    Toast.makeText(ProfileActivity.this, "User updated!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ProfileActivity.this, "Same data, nothing to save", Toast.LENGTH_LONG).show();
                }
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileFolder();
            }
        });
    }

    private void openFileFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(avatar);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        //String uploadId = mDatabaseRef.push().getKey();
                        //mDatabaseRef.child(uploadId).setValue(upload);
                        ImageHelper image = new ImageHelper(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        UserHelper.imageUrl = image.getImageUrl();
                        reference.child(UserHelper.username).child("imageUrl").setValue(image.getImageUrl());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void fillData(){
        String user_username = UserHelper.username;
        String user_email = UserHelper.email;
        String user_password = UserHelper.password;
        String user_victories = "Victories: " + UserHelper.victories;

        lblName.setText(user_username);
        lblVictories.setText(user_victories);
        inputUsername.setText(user_username);
        inputEmail.setText(user_email);
        inputPassword.setText(user_password);

        Picasso.get().load(Uri.parse(UserHelper.imageUrl)).into(avatar);
    }

    private boolean isImageChanged(){
        String string = mImageUri.toString();
        if(!UserHelper.imageUrl.equals(string))
        {
            return true;
        }else{
            return false;
        }
    }

    private boolean isPasswordChanged(){
        String string = inputPassword.getText().toString();
        if(!UserHelper.password.equals(inputPassword.getText().toString()))
        {
            reference.child(UserHelper.username).child("password").setValue(inputPassword.getText().toString());
            UserHelper.password=inputPassword.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isUsernameChanged(){
        String string = inputUsername.getText().toString();
        if(!UserHelper.username.equals(inputUsername.getText().toString())){
            reference.child(UserHelper.username).setValue(null);
            UserHelper.username=inputUsername.getText().toString();
            reference = rootNode.getReference("users");
            UserHelper helperClass = new UserHelper(UserHelper.username, UserHelper.password, UserHelper.email);
            reference.child(UserHelper.username).setValue(helperClass);
            return true;
        }else{
            return false;
        }
    }

    private boolean isEmailChanged(){
        String string = inputEmail.getText().toString();
        if(!UserHelper.email.equals(inputEmail.getText().toString())){
            reference.child(UserHelper.username).child("email").setValue(inputEmail.getText().toString());
            UserHelper.email=inputEmail.getText().toString();
            return true;
        }else{
            return false;
        }

    }
}
