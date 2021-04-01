package com.innova.sharks.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.innova.sharks.databinding.FragmentIntroBinding;
import com.innova.sharks.ui.MainActivity;


public class IntroFragment extends Fragment {

    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentIntroBinding binding = FragmentIntroBinding.inflate(inflater, container, false);

//        // Create an animation instance
//        Animation an = new RotateAnimation(0.0f, 360.0f, 0, 0);
//
//        // Set the animation's parameters
//        an.setDuration(10000);               // duration in ms
//        an.setRepeatCount(0);                // -1 = infinite repeated
//        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
//        an.setFillAfter(true);               // keep rotation after animation
//
//        // Apply animation to image view
//        binding.tv.setAnimation(an);

        binding.skip.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
        return binding.getRoot();
    }
}