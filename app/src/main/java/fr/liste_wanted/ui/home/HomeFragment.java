package fr.liste_wanted.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Pole;
import fr.liste_wanted.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Pole> poles;
    ListView polesView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        polesView = root.findViewById(R.id.list_poles);

        poles = getPolesFromJSON(getContext());
        polesView.setAdapter(new PolesAdapter(getContext(), poles));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected static List<Pole> getPolesFromJSON(Context context) {
        List<Pole> poles = new ArrayList<>();
        String json = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.poles)));
            String line;
            while ((line = reader.readLine()) != null)
                json += line + "\n";
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonPoles = new JSONArray(json);
            for (int i = 0; i < jsonPoles.length(); i++)
                poles.add(new Pole(jsonPoles.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poles;
    }

}