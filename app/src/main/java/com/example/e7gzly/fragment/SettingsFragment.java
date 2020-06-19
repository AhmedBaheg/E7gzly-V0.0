package com.example.e7gzly.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.EditDialog;
import com.example.e7gzly.model.User;
import com.example.e7gzly.utilities.Constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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


public class SettingsFragment extends Fragment {

    private View view;
    private TextView profile_Name, profile_Email;
    private CircleImageView img_Profile;
    private ImageButton edit_name;

    private Uri uri;
    private String name, email, exist_img_url;
    // Fire Base

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference userProfileRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Fire Base
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        profile_Name = view.findViewById(R.id.tv_name_profile);
        profile_Email = view.findViewById(R.id.tv_email_profile);
        img_Profile = view.findViewById(R.id.img_user_profile);
        edit_name = view.findViewById(R.id.btn_edit_name_profile);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ProfileInfoUser();
        returnData();
        img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .setAutoZoomEnabled(true)
                        .start(getContext(), SettingsFragment.this); // Mahmoud Crash Here //
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditDialog editDialog = new EditDialog(getContext());
                editDialog.ed_dialog_name.setText(name);

                editDialog.btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editDialog.validationName()){
                            return;
                        }
                        name = editDialog.ed_dialog_name.getText().toString();
                        saveInfoDB(exist_img_url , name , email);
                        editDialog.dismiss();
                    }
                });
                editDialog.show();
                returnData();
            }
        });
    }

    /**
     * {@link Picasso#get().placeholder}  && {@link Picasso#get().error}
     * ده بيجيب الصوره من ال drawable في حالت ان حصل اي مشكله في ال URL اللي راجع
     */
    private void returnData() {
        databaseReference.child(Constants.USERS).child(Constants.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    exist_img_url = user.getImgUrl();
                    name = user.getFullName();
                    email = user.getEmail();

                    Picasso.get()
                            .load(exist_img_url)
                            .placeholder(R.drawable.default_pic_user)
                            .error(R.drawable.default_pic_user)
                            .into(img_Profile);
                    profile_Name.setText(name);
                    profile_Email.setText(email);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * يعني انا عاملك class User علشان تجيب الداتا من الفير بيز بيه
     * وانت جاي تجيب الداتا كده
     * والله حرام عليك
     * عملتلك الصح في ميثود دي {@link #returnData()}
     */
// private void ProfileInfoUser() {
//
//        databaseReference.child(Constants.USERS).child("userId").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
//
//                String name = dataSnapshot.child("fullName").getValue().toString();
//                profile_Name.setText(name);
//                String email = dataSnapshot.child("email").getValue().toString();
//                profile_Email.setText(email);
//                String img = dataSnapshot.child("imgUrl").getValue().toString();
//                Picasso.get().load(img).into(img_Profile);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    /**
     * {@link CropImage} مش هتستعمل دي لو هتستخدم
     */

//    public void uploadImgProfile() {
//
//        // Open Gallery
//        Intent galleryIntent = new Intent();
//        galleryIntent.setAction(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, Gallery_Pic);
//
//    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    uri = result.getUri();

                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.default_pic_user)
                            .error(R.drawable.default_pic_user)
                            .into(img_Profile);
                    updateInfo(name, email, uri);
                    returnData();
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }

        }
    }

    private void updateInfo(final String name, final String email, Uri add_pic) {

        UploadTask uploadTask;

        final StorageReference rf = userProfileRef.child("/Profile Images" + add_pic.getLastPathSegment());

        uploadTask = rf.putFile(add_pic);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return rf.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                String user_Pic = downloadUri.toString();

                saveInfoDB(user_Pic, name, email);
                returnData();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Can't Upload Photo", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void saveInfoDB(String user_pic, String name, String email) {
        User user;
        if (TextUtils.isEmpty(user_pic)) {
            user = new User(name, email);
        } else {
            user = new User(name, email, user_pic);
        }
        databaseReference.child(Constants.USERS).child(Constants.getUID()).setValue(user);

    }

    @Override
    public void onStart() {
        super.onStart();
        returnData();
    }

    @Override
    public void onResume() {
        super.onResume();
        returnData();
    }


}
