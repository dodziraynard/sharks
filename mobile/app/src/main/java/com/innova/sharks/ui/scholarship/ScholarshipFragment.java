package com.innova.sharks.ui.scholarship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.sharks.adapter.ScholarshipAdapter;
import com.innova.sharks.databinding.FragmentScholarshipBinding;
import com.innova.sharks.models.Scholarship;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

public class ScholarshipFragment extends Fragment implements TextWatcher {
    private static final String TAG = "ScholarshipFragment";
    private ScholarshipAdapter mAdapter;
    private ScholarshipViewModel mViewModel;
    private List<Scholarship> mScholarships;
    private FragmentScholarshipBinding mBinding;

    public ScholarshipFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScholarships = new ArrayList<>();
        mAdapter = new ScholarshipAdapter(getContext());
        mViewModel = new ViewModelProvider(this).get(ScholarshipViewModel.class);

        mAdapter.setOnItemClickListener(scholarship -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scholarship.getUrl()));
            startActivity(browserIntent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentScholarshipBinding.inflate(inflater, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerView.setAdapter(mAdapter);

        mBinding.swipeRefresh.setOnRefreshListener(this::refreshData);

        mViewModel.getScholarships("").observe(getViewLifecycleOwner(), scholarships -> {
            mBinding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (scholarships.size() > 0) {
                mBinding.noItemFound.setVisibility(View.GONE);
                mScholarships = scholarships;
                mAdapter.setData(mScholarships);
            } else {
                mBinding.noItemFound.setVisibility(View.VISIBLE);
            }
        });

        mBinding.search.addTextChangedListener(this);
        return mBinding.getRoot();
    }

    private void refreshData() {
        mViewModel.refreshData("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.setOnItemClickListener(null);
        mAdapter.setData(null);
        mScholarships = null;
        mAdapter = null;
        mViewModel = null;
        mBinding.search.removeTextChangedListener(this);
        mBinding = null;
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String newText = editable.toString().toLowerCase();
        List<Scholarship> filterScholarships = new ArrayList<>();
        for (Scholarship scholarship : mScholarships) {
            if (scholarship.getTitle().toLowerCase().contains(newText)
                    || scholarship.getDescription().toLowerCase().contains(newText)
                    || scholarship.getLocation().toLowerCase().contains(newText)) {
                filterScholarships.add(scholarship);
            }
        }
        mAdapter.setData(filterScholarships);
    }
}