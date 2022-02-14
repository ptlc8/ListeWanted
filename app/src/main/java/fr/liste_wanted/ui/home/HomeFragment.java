package fr.liste_wanted.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Member;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.facebook.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK))));
        binding.instagram.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM))));
        binding.youtube.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE))));
        binding.telegram.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM))));
        binding.website.setOnClickListener(view -> requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE))));

        LinearLayout polesView = binding.listPoles;
        List<Pole> poles = Pole.getPolesFromJSON(requireContext());
        int membersPerRow = getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT?3:6;
        for (Pole pole : poles)
            polesView.addView(createPoleView(inflater, polesView, pole, membersPerRow));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static View createPoleView(LayoutInflater inflater, ViewGroup root, Pole pole, int membersPerRow) {
        View poleView = inflater.inflate(R.layout.view_pole, root, false);
        ((TextView)poleView.findViewById(R.id.text_name)).setText(pole.getName());
        LinearLayout membersGrid = poleView.findViewById(R.id.linear_pole);
        for (int l = 0; l < pole.size()*1f/ membersPerRow; l++) {
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row, membersGrid, false);
            row.setWeightSum(membersPerRow);
            for (int j = l* membersPerRow; j < (l+1)*membersPerRow && j < pole.size(); j++) {
                View memberView = inflater.inflate(R.layout.view_member, row, false);
                Member member = pole.get(j);
                ((TextView) memberView.findViewById(R.id.nickname)).setText(member.getNickname());
                ((TextView) memberView.findViewById(R.id.role)).setText(member.getRole());
                if (member.getRole() == null)
                    ((TextView) memberView.findViewById(R.id.role)).setHeight(0);
                ((ImageView) memberView.findViewById(R.id.picture)).setImageBitmap(member.getPictureBitmap(inflater.getContext()));
                row.addView(memberView);
            }
            membersGrid.addView(row);
        }
        return poleView;
    }

}