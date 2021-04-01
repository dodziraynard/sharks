package com.innova.sharks.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.innova.sharks.R;
import com.innova.sharks.adapter.BookAdapter;
import com.innova.sharks.databinding.FragmentLibraryBinding;
import com.innova.sharks.models.Book;
import com.innova.sharks.utils.Constants;
import com.innova.sharks.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    List<Book> mBooks = new ArrayList<>();
    private FragmentLibraryBinding mBinding;
    private LibraryViewModel mViewModel;
    private BookAdapter mAdapter;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        mAdapter = new BookAdapter(getContext());

        mAdapter.setOnItemClickListener(book -> {
            Intent intent = new Intent(getContext(), PdfViewActivity.class);
            intent.putExtra(Constants.BOOK_TO_PDF_VIEW, book);
            startActivity(intent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false);

        mBinding.swipeRefresh.setOnRefreshListener(this::refreshData);
        mBinding.swipeRefresh.setRefreshing(true);

        mViewModel.getBooks("").observe(getViewLifecycleOwner(), books -> {
            mBinding.swipeRefresh.setRefreshing(NetworkState.getInstance().getStatus() == NetworkState.Status.LOADING);
            if (books.size() > 0) {
                mBinding.noItemFound.setVisibility(View.GONE);
            } else {
                mBinding.noItemFound.setVisibility(View.VISIBLE);
            }
            mBooks = books;
            mAdapter.setData(mBooks);
        });

        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.pdf_grid_columns)));
        mBinding.searchBtn.setOnClickListener(view -> {
            String newText = mBinding.search.getText().toString().toLowerCase();
            List<Book> filterBooks = new ArrayList<>();
            for (Book book : mBooks) {
                if (book.getTitle().toLowerCase().contains(newText)
                        || book.getAuthor().toLowerCase().contains(newText)) {
                    filterBooks.add(book);
                }
            }
            mAdapter.setData(filterBooks);
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
        mBooks = null;
        mBinding = null;
        mViewModel = null;
        mAdapter = null;
    }
}