package com.example.e7gzly.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.utilities.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    View view;
    Context context;

    TextView profile_Name, profile_Email;
    ImageView ic_Back_Profile;
    CircleImageView img_Profile;

    private static final int Gallaery_Pic = 1;

    private Uri uri;

    // Fire Base

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userId;
    StorageReference userProfileRef;
    private String TAG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Fire Base

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }
        userProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        profile_Name = view.findViewById(R.id.tv_name_profile);
        profile_Email = view.findViewById(R.id.tv_email_profile);
        img_Profile = view.findViewById(R.id.img_user_profile);

        ProfileInfoUser();
        img_Profile = view.findViewById(R.id.img_user_profile);
        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImgProfile();
            }
        });
        return view;
    }


    private void ProfileInfoUser() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);

                String name = dataSnapshot.child("fullName").getValue().toString();
                profile_Name.setText(name);
                String email = dataSnapshot.child("email").getValue().toString();
                profile_Email.setText(email);
                String img = dataSnapshot.child("imgUrl").getValue().toString();
                Picasso.get().load(img).into(img_Profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadImgProfile() {

        // Open Gallery
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallaery_Pic);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set Image From Gallery
        if (requestCode == Gallaery_Pic && resultCode == RESULT_OK) {

            uri = data.getData();

            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1) // Size Crop Image
                    .start(getContext() , SettingsFragment.this); // Mahmoud Crash Here //
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final StorageReference filepath = userProfileRef.child(userId + "jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                        Uri downloadUri = taskSnapshot.getStorage().getDownloadUrl().getResult();
//                        databaseReference.child("imgUrl").setValue(downloadUri);

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, uri.toString());
                                databaseReference.child("imgUrl").setValue(uri.toString());
                            }
                        });
                    }
                });
            }

        }

    }

}
