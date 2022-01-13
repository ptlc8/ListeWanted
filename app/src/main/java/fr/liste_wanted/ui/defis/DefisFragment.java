package fr.liste_wanted.ui.defis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Defis;
import fr.liste_wanted.databinding.FragmentDefisBinding;

public class DefisFragment extends Fragment {

    private Defis defis;
    private FragmentDefisBinding binding;
    private DefisListAdapter defisListAdapter;
    private Button proposeButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        defis = new Defis();

        binding = FragmentDefisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView defisListView = root.findViewById(R.id.list_defis);
        defisListAdapter = new DefisListAdapter(getContext());
        defisListView.setAdapter(defisListAdapter);
        proposeButton = root.findViewById(R.id.propose_defi);

        refresh(()->{});

        SwipeRefreshLayout swipe2refresh = root.findViewById(R.id.swipe2refresh);
        swipe2refresh.setOnRefreshListener(() -> refresh(() -> swipe2refresh.setRefreshing(false)));

        root.findViewById(R.id.propose_defi).setOnClickListener(event -> {
            startActivity(new Intent(getContext(), SendDefiActivity.class));
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh(()->{});
    }

    public void refresh(Runnable onRefresh) {
        defis.refresh(defis -> {
            getActivity().runOnUiThread(() -> {
                defisListAdapter.setDefis(defis);
                proposeButton.setEnabled(defis.canSubmit());
            });
            onRefresh.run();
        }, ioe -> {
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show());
            onRefresh.run();
        }, se -> {
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_LONG).show());
            onRefresh.run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}