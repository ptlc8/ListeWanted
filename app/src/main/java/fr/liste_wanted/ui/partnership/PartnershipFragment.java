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
import fr.liste_wanted.data.Pole;
import fr.liste_wanted.databinding.FragmentEventsBinding;
import fr.liste_wanted.databinding.FragmentPartnershipBinding;

public class PartnershipFragment extends Fragment {

    FragmentPartnershipBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPartnershipBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((ListView)root.findViewById(R.id.list_partnerships)).setAdapter(new PartnershipsAdapter(getContext(), getPartnershipsFromJSON(requireContext())));

        return root;
    }

    protected static List<Partnership> getPartnershipsFromJSON(Context context) {
        List<Partnership> partnerships = new ArrayList<>();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.partenerships)));
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
