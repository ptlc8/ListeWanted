package fr.liste_wanted.ui.partnership;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Partnership;
import fr.liste_wanted.databinding.FragmentPartnershipsBinding;

public class PartnershipsFragment extends Fragment {

    FragmentPartnershipsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPartnershipsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Partnership> partnerships = getPartnershipsFromJSON(requireContext());
        ListView partnershipsListView = root.findViewById(R.id.list_partnerships);
        partnershipsListView.setAdapter(new PartnershipsAdapter(getContext(), partnerships));
        partnershipsListView.setOnItemClickListener((adapterView, view, i, l) ->
                startActivity(PartnershipActivity.getShowEventIntent(requireContext(), partnerships.get(i)))
        );

        return root;
    }

    protected static List<Partnership> getPartnershipsFromJSON(Context context) {
        List<Partnership> partnerships = new ArrayList<>();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.partnerships)));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonPartnerships = new JSONArray(json);
            for (int i = 0; i < jsonPartnerships.length(); i++)
                partnerships.add(new Partnership(jsonPartnerships.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partnerships;
    }

}
