package com.example.degrees;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.degrees.databinding.FragmentFirstBinding;

/**
 * This fragment uses the View Binding feature to interact with the XML layout file
 * through a binding class, {@code FragmentFirstBinding}.
 */
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    /**
     * Instantiates the user interface view for this fragment.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
/**
 * Called to have the fragment instantiate its user interface view.
 * This method inflates the layout for this fragment, initializing a {@code FragmentFirstBinding} instance with the inflated view.
 */
 public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}