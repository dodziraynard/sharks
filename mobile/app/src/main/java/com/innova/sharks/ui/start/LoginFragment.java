package com.innova.sharks.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.innova.sharks.databinding.FragmentLoginBinding;
import com.innova.sharks.ui.MainActivity;

import static com.innova.sharks.utils.Constants.FRAGMENT_POSITION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StartViewModel mViewModel;
    private FragmentLoginBinding mBinding;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mViewModel = new ViewModelProvider(this).get(StartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        mBinding.skip.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), MainActivity.class));
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (!errorMessage.isEmpty()) {
                showSnackBar(errorMessage);
                enableForm();
            }
        });

        mViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                startActivity(new Intent(getContext(), MainActivity.class));
                if (getActivity() != null)
                    getActivity().finish();
            }
        });

        mBinding.createNew.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), StartActivity.class);
            intent.putExtra(FRAGMENT_POSITION, 3);
            startActivity(intent);
            if (getActivity() != null)
                getActivity().finish();
        });

        mBinding.login.setOnClickListener(view -> {
            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();
            submitForm(username, password);
        });
        return mBinding.getRoot();
    }

    private void submitForm(String username, String password) {
        disableForm();
        mViewModel.login(username, password);
    }

    private void showSnackBar(String message) {
        View parentLayout = getActivity().findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction("OKAY", v -> {
                })
                .setTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void disableForm() {
        mBinding.login.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.password.setEnabled(false);
        mBinding.username.setEnabled(false);
    }

    private void enableForm() {
        mBinding.login.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.password.setEnabled(true);
        mBinding.username.setEnabled(true);
    }
}