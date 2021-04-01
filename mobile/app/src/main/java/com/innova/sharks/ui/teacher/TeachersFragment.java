package com.innova.sharks.ui.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.sharks.adapter.TeacherAdapter;
import com.innova.sharks.databinding.FragmentTeachersBinding;
import com.innova.sharks.models.Teacher;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import static com.innova.sharks.utils.Constants.TEACHER_TO_TEACHER_DETAILS;

public class TeachersFragment extends Fragment {

    List<Teacher> mTeachers = new ArrayList<>();
    private FragmentTeachersBinding mBinding;
    private TeachersViewModel mViewModel;
    private TeacherAdapter mAdapter;

    public TeachersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeachersViewModel.class);
        mAdapter = new TeacherAdapter(getContext());

        mAdapter.setOnItemClickListener(teacher -> {
            Intent intent = new Intent(getContext(), TeacherDetailActivity.class);
            intent.putExtra(TEACHER_TO_TEACHER_DETAILS, teacher);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentTeachersBinding.inflate(inflater, container, false);

        mBinding.swipeRefresh.setOnRefreshListener(this::refreshData);
        mBinding.swipeRefresh.setRefreshing(true);

        mViewModel.getTeachers("").observe(getViewLifecycleOwner(), teachers -> {
            mBinding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (teachers.size() > 0) {
                mBinding.noItemFound.setVisibility(View.GONE);
            } else {
                mBinding.noItemFound.setVisibility(View.VISIBLE);
            }
            mTeachers = teachers;
            mAdapter.setData(mTeachers);
        });

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.searchBtn.setOnClickListener(view -> {
            String newText = mBinding.search.getText().toString().toLowerCase();
            List<Teacher> filterTeachers = new ArrayList<>();
            for (Teacher teacher : mTeachers) {
                String teacherHeader = teacher.getHeader() == null ? "" : teacher.getHeader();
                if (teacher.getName().toLowerCase().contains(newText)
                        || teacherHeader.toLowerCase().contains(newText)) {
                    filterTeachers.add(teacher);
                }
            }
            mAdapter.setData(filterTeachers);
        });

        return mBinding.getRoot();
    }

    private void refreshData() {
        String newText = mBinding.search.getText().toString().toLowerCase();
        mViewModel.refreshData(newText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTeachers = null;
        mBinding = null;
        mViewModel = null;
        mAdapter = null;
    }
}