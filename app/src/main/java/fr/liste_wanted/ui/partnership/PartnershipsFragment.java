package fr.liste_wanted.ui.partnership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Partnership;
import fr.liste_wanted.data.Partnerships;
import fr.liste_wanted.databinding.FragmentPartnershipsBinding;

public class PartnershipsFragment extends Fragment {

    private FragmentPartnershipsBinding binding;
    private Partnerships partnerships;
    private PartnershipsAdapter partnershipsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPartnershipsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        partnerships = new Partnerships();

        binding.swipe2refresh.setOnRefreshListener(() -> refresh(() -> binding.swipe2refresh.setRefreshing(false)));

        binding.listPartnerships.setAdapter(partnershipsAdapter = new PartnershipsAdapter(getContext(), partnerships));
        binding.listPartnerships.setOnItemClickListener((adapterView, view, i, l) ->
                startActivity(PartnershipActivity.getShowEventIntent(requireContext(), partnerships.get(i)))
        );

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh(()->{});
    }

    public void refresh(Runnable onRefresh) {
        partnerships.refresh(partnerships -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
                binding.connectionError.getRoot().setVisibility(View.GONE);
                partnershipsAdapter.setPartnerships(partnerships);
            });
            onRefresh.run();
        }, ioe -> {
            if (getActivity()==null) return;
            getActivity().runOnUiThread(() -> {
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
}
