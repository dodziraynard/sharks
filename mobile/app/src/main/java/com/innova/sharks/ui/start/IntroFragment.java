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

        binding.skip.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
        return binding.getRoot();
    }
}