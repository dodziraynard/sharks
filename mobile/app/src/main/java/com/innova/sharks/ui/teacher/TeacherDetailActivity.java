package com.innova.sharks.ui.teacher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ActivityTeacherDetailBinding;
import com.innova.sharks.models.Teacher;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.innova.sharks.utils.Constants.TEACHER_TO_TEACHER_DETAILS;

public class TeacherDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTeacherDetailBinding binding = ActivityTeacherDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Teacher teacher = getIntent().getParcelableExtra(TEACHER_TO_TEACHER_DETAILS);
        setTitle(teacher.getName());

        binding.introduction.setText(teacher.getIntroduction());
        binding.contact.setText(teacher.getContact());
        binding.email.setText(teacher.getEmail());
        binding.education.setText(teacher.getEducation());
        binding.name.setText(teacher.getName());
        binding.header.setText(teacher.getHeader());
        binding.location.setText(teacher.getLocation());
        binding.rating.setText(getString(R.string.rating, teacher.getRating()));
        binding.comments.setText(getString(R.string.comments, teacher.getReviews()));

        Glide.with(this)
                .load(teacher.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(binding.profileImage);

        Glide.with(this)
                .load(teacher.getBackgroundImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(binding.backgroundImage);

        binding.whatsapp.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Hello ");
            messageBuilder.append(teacher.getName());
            messageBuilder.append(",\n");
            messageBuilder.append("I just saw your profile on the Sharks app; I'm impressed and would like to get in touch with you.");
            String url;
            try {
                url = "https://api.whatsapp.com/send?phone=+233" + teacher.getContact() + "&text=" + URLEncoder.encode(messageBuilder.toString(), "UTF-8");
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        binding.call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + teacher.getContact()));
            startActivity(intent);

        });
    }
}