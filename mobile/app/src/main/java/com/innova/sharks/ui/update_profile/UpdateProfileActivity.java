package com.innova.sharks.ui.update_profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ActivityUpdateProfileBinding;
import com.innova.sharks.ui.MainActivity;
import com.innova.sharks.ui.start.StartActivity;
import com.innova.sharks.utils.Constants;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.innova.sharks.utils.Constants.FRAGMENT_POSITION;
import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;
import static com.innova.sharks.utils.Functions.getUserId;
import static com.innova.sharks.utils.Functions.getUserToken;

public class UpdateProfileActivity extends AppCompatActivity {
    private static final String TAG = "UpdateProfileActivity";
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int PICK_BACKGROUND_IMAGE = 1;
    private final int PICK_PROFILE_IMAGE = 2;
    private final int REQUEST_PERMISSIONS_CODE = 3;
    private final HashMap<String, String> mProfileInfo = new HashMap<>();
    private ActivityUpdateProfileBinding mBinding;
    private UpdateProfileViewModel mViewModel;
    private boolean isTeacher = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);

        String token = getUserToken(this);
        long userId = getUserId(this);

        if (token.isEmpty()) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra(FRAGMENT_POSITION, 2);
            startActivity(intent);
            finish();
            return;
        }

        mBinding.editBackgroundImage.setOnClickListener(view -> {
            if (permissionsDenied()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
                    requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
                }
            }
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_BACKGROUND_IMAGE);
        });

        mBinding.editProfileImage.setOnClickListener(view -> {
            if (permissionsDenied()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissionsDenied()) {
                    requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS_CODE);
                }
            }
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_PROFILE_IMAGE);
        });

        mBinding.logout.setOnClickListener(view -> {
            mViewModel.logout();
            onBackPressed();
            finish();
        });

        mViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                mBinding.progressBar.setVisibility(View.VISIBLE);
            } else {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });

        mViewModel.getTeacher().observe(this, teacher -> {
            isTeacher = true;

            mBinding.fullname.setText(teacher.getName());
            mBinding.level.setText(teacher.getHeader());
            mBinding.contact.setText(teacher.getContact());
            mBinding.education.setText(teacher.getEducation());
            mBinding.email.setText(teacher.getEmail());
            mBinding.location.setText(teacher.getLocation());
            mBinding.introduction.setText(teacher.getIntroduction());

            updateImages(teacher.getBackgroundImage(), mBinding.backgroundImage);
            updateImages(teacher.getImage(), mBinding.profileImage);
        });

        mViewModel.getStudent().observe(this, student -> {
            mBinding.greeting.setText(getString(R.string.greeting, student.getUsername()));
            mBinding.fullname.setText(student.getName());
            mBinding.level.setHint("Level/Class e.g 1 for Basic 1 and 10 for SHS 10");
            mBinding.level.setText(String.valueOf(student.getLevel()));
            mBinding.contact.setText(student.getContact());
            mBinding.education.setText(student.getEducation());
            mBinding.email.setVisibility(View.GONE);
            mBinding.location.setText(student.getLocation());
            mBinding.introduction.setText(student.getIntroduction());
            updateImages(student.getBackgroundImage(), mBinding.backgroundImage);
            updateImages(student.getImage(), mBinding.profileImage);
        });

        mViewModel.getUser(userId);

        mBinding.save.setOnClickListener(view -> {
            updateProfileInfo();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    @SuppressLint("ApplySharedPref")
    public void updateProfileInfo() {
        mProfileInfo.put("full_name", mBinding.fullname.getText().toString());
        mProfileInfo.put("contact", mBinding.contact.getText().toString());
        mProfileInfo.put("education", mBinding.education.getText().toString());
        mProfileInfo.put("location", mBinding.location.getText().toString());
        mProfileInfo.put("introduction", mBinding.introduction.getText().toString());

        if (isTeacher) {
            mProfileInfo.put("email", mBinding.email.getText().toString());
            mProfileInfo.put("header", mBinding.level.getText().toString());
        } else {
            mProfileInfo.put("level", mBinding.level.getText().toString());
        }
        int level = Integer.parseInt(mBinding.level.getText().toString());
        Toast.makeText(this, "" + level, Toast.LENGTH_SHORT).show();
        getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE).edit().putInt(Constants.STUDENT_LEVEL, level).commit();
        mViewModel.updateProfileInfo(mProfileInfo);
    }

    private void updateImages(String url, ImageView view) {
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_favorite_border_24)
                .into(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_BACKGROUND_IMAGE && resultCode == RESULT_OK && null != data) {
            handleOnBackgroundImageSelected(data);
        } else if (requestCode == PICK_PROFILE_IMAGE && resultCode == RESULT_OK && null != data) {
            handleOnProfileImageSelected(data);
        }
    }

    private void handleOnBackgroundImageSelected(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        File file = new File(picturePath);
        Uri imageUri = Uri.fromFile(file);
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_favorite_border_24)
                .into(mBinding.backgroundImage);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part backgroundImage = MultipartBody.Part.createFormData("background_image", file.getName(), requestFile);
        mViewModel.updateProfileImages(backgroundImage);
    }

    private void handleOnProfileImageSelected(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        File file = new File(picturePath);
        Uri imageUri = Uri.fromFile(file);
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_favorite_border_24)
                .into(mBinding.profileImage);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part profileImage = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);
        mViewModel.updateProfileImages(profileImage);
    }

    private boolean permissionsDenied() {
        for (String permission : PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionsDenied()) {
            Toast.makeText(this, "Perm Denied", Toast.LENGTH_LONG).show();
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
        }
    }
}