package fr.liste_wanted.ui.allos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import fr.liste_wanted.databinding.FragmentAllosBinding;

public class AllosFragment extends Fragment {

    private AllosViewModel dashboardViewModel;
    private FragmentAllosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(AllosViewModel.class);

        binding = FragmentAllosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAllos;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            textView.setText(s);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}