package fr.liste_wanted.ui.defis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Defi;
import fr.liste_wanted.data.Defis;
import fr.liste_wanted.databinding.FragmentDefisBinding;

public class DefisFragment extends Fragment {

    private Defis defis;
    private FragmentDefisBinding binding;
    private DefisListAdapter defisListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        defis = new Defis();

        binding = FragmentDefisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        defisListAdapter = new DefisListAdapter(getContext());
        binding.listDefis.setAdapter(defisListAdapter);

        binding.swipe2refresh.setRefreshing(true);
        binding.swipe2refresh.setOnRefreshListener(() -> refresh(() -> binding.swipe2refresh.setRefreshing(false)));

        binding.proposeDefi.setOnClickListener(event -> startActivity(new Intent(getContext(), SendDefiActivity.class)));

        binding.listDefis.setOnItemClickListener((adapterView, view, i, l) -> {
            Defi defi = defis.getDefis().get(i);
            if (defi.isFinished()) {
                if (defi.getEvidenceLink()!=null && !defi.getEvidenceLink().equals("null") && !defi.getEvidenceLink().equals("")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(defi.getEvidenceLink())));
                } else {
                    Toast.makeText(requireContext(), R.string.inavailable_evidence, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh(() -> binding.swipe2refresh.setRefreshing(false));
    }

    public void refresh(Runnable onRefresh) {
        defis.refresh(defis -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
                binding.connectionError.getRoot().setVisibility(View.GONE);
                binding.listDefis.setVisibility(View.VISIBLE);
                defisListAdapter.setDefis(defis);
                binding.proposeDefi.setEnabled(defis.canSubmit());
                int finishedDefisCount = 0;
                for (Defi defi : defis.getDefis())
                    if (defi.isFinished())
                        finishedDefisCount++;
                binding.textDefis.setText(getString(R.string.finished_defis, finishedDefisCount));
            });
            onRefresh.run();
        }, ioe -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
                if (defis.getDefis().size() == 0)
                    binding.connectionError.getRoot().setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
            });
            onRefresh.run();
        }, se -> {
            if (getActivity()==null) return;
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