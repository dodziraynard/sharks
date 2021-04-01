package com.innova.sharks.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.innova.sharks.R;
import com.innova.sharks.models.Lesson;

public class DetailFragment extends Fragment {
    private static final String ARG_LESSON_PARAM = "param1";
    private Lesson mLesson;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(Lesson lesson) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LESSON_PARAM, lesson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLesson = getArguments().getParcelable(ARG_LESSON_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}