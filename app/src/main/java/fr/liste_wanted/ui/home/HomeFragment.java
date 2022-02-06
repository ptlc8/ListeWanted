package fr.liste_wanted.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
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

    private static final String
            FACEBOOK = "https://www.facebook.com/n/?BDERunWild", // TODO : à modifier
            INSTAGRAM = "https://www.instagram.com/ptlc8/", // TODO : à modifier
            YOUTUBE = "https://www.youtube.com/channel/UCYMikqdthiDtJcJlsAOOE-Q", // TODO : à modifier
            TELEGRAM = "https://t.me/joinchat/MNxDBfASdc5jNzE8", // TODO : à modifier
            WEBSITE = "https://liste-wanted.fr";

    private FragmentHomeBinding binding;
    List<Pole> poles;
    ListView polesView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        polesView = root.findViewById(R.id.list_poles);

        poles = getPolesFromJSON(requireContext());
        polesView.setAdapter(new PolesAdapter(getContext(), poles, getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT?3:6));

        binding.facebook.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK))));
        binding.instagram.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM))));
        binding.youtube.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE))));
        binding.telegram.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM))));
        binding.website.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE))));

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