package com.innova.sharks.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.innova.sharks.databinding.FragmentRegisterBinding;
import com.innova.sharks.ui.MainActivity;
import com.innova.sharks.ui.update_profile.UpdateProfileActivity;

import static com.innova.sharks.utils.Constants.FRAGMENT_POSITION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentRegisterBinding mBinding;
    private StartViewModel mViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        mBinding.login.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), StartActivity.class);
            intent.putExtra(FRAGMENT_POSITION, 2);
            startActivity(intent);
            if (getActivity() != null)
                getActivity().finish();
        });

        mBinding.skip.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), MainActivity.class));
        });

        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (!errorMessage.isEmpty()) {
                showSnackBar(errorMessage);
                enableForm();
            }
        });

        mBinding.register.setOnClickListener(view -> {
            String username = mBinding.username.getText().toString();
            String password = mBinding.password.getText().toString();
            String password2 = mBinding.password2.getText().toString();

            String errorMessage = "";
            if (password2.isEmpty())
                errorMessage = "Please enter second password.";
            if (password.isEmpty())
                errorMessage = "Please enter password.";
            if (username.isEmpty())
                errorMessage = "Please enter username.";
            if (username.length() < 3)
                errorMessage = "Please enter at least 2 letters for username.";
            if (password.length() < 5)
                errorMessage = "Password should be at least 5 characters.";
            if (!password.equals(password2))
                errorMessage = "Passwords do not match.";
            if (!errorMessage.isEmpty()) {
                showSnackBar(errorMessage);
                return;
            }
            submitForm(username, password);
        });

        return mBinding.getRoot();
    }

    private void submitForm(String username, String password) {
        disableForm();
        mViewModel.registerStudent(username, password).observe(getViewLifecycleOwner(), student -> {
            Toast.makeText(getContext(), "Yay, complete your student profile.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), UpdateProfileActivity.class));

            if (getActivity() != null)
                getActivity().finish();
        });
    }

    private void showSnackBar(String message) {
        if (getActivity() == null) return;
        View parentLayout = getActivity().findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction("OKAY", v -> {
                })
                .setTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void disableForm() {
        mBinding.register.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.password.setEnabled(false);
        mBinding.password2.setEnabled(false);
        mBinding.username.setEnabled(false);
    }

    private void enableForm() {
        mBinding.register.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.password.setEnabled(true);
        mBinding.password2.setEnabled(true);
        mBinding.username.setEnabled(true);
    }
}