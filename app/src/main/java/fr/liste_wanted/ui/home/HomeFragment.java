package fr.liste_wanted.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.function.Consumer;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Member;
import fr.liste_wanted.data.Pole;
import fr.liste_wanted.databinding.FragmentHomeBinding;
import fr.liste_wanted.databinding.PopupMemberBinding;

public class HomeFragment extends Fragment {

    private static final String
            FACEBOOK = "https://www.facebook.com/n/?ListeBDEWanted",
            INSTAGRAM = "https://www.instagram.com/listebdewanted/",
            YOUTUBE = "https://www.youtube.com/channel/UCQth_1OoT6GGbZAxwDrBoZg",
            TELEGRAM = "https://t.me/listebdewanted",
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

        List<Pole> poles = Pole.getPolesFromJSON(requireContext());
        int membersPerRow = getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT?3:6;
        for (Pole pole : poles)
            binding.listPoles.addView(createPoleView(inflater, binding.listPoles, pole, membersPerRow, this::onMemberClick));

        return root;
    }

    private void onMemberClick(Member member) {
        View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_member, binding.listPoles, false);
        PopupMemberBinding binding = PopupMemberBinding.bind(popupView);
        binding.name.setText(member.getFirstname());
        if (member.getRole()==null || "".equals(member.getRole())) binding.role.setVisibility(View.GONE);
        else binding.role.setText(member.getRole());
        binding.nickname.setText(member.getNickname());
        binding.description.setText(member.getDescription());
        PopupWindow popup = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popup.setAnimationStyle(R.style.Animation_AppCompat_Dialog);
        popup.showAtLocation(this.binding.listPoles, Gravity.CENTER, 0, 0);
        popupView.setOnClickListener(view -> popup.dismiss());
    }

    private static View createPoleView(LayoutInflater inflater, ViewGroup root, Pole pole, int membersPerRow, Consumer<Member> onMemberClick) {
        View poleView = inflater.inflate(R.layout.view_pole, root, false);
        ((TextView)poleView.findViewById(R.id.text_name)).setText(pole.getName());
        LinearLayout membersGrid = poleView.findViewById(R.id.linear_pole);
        for (int l = 0; l < pole.size()*1f/ membersPerRow; l++) {
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row, membersGrid, false);
            row.setWeightSum(membersPerRow);
            for (int j = l* membersPerRow; j < (l+1)*membersPerRow && j < pole.size(); j++) {
                View memberView = inflater.inflate(R.layout.view_member, row, false);
                final Member member = pole.get(j);
                ((TextView) memberView.findViewById(R.id.nickname)).setText(member.getNickname());
                ((TextView) memberView.findViewById(R.id.role)).setText(member.getRole());
                if (member.getRole() == null)
                    ((TextView) memberView.findViewById(R.id.role)).setHeight(0);
                ((ImageView) memberView.findViewById(R.id.picture)).setImageBitmap(member.getPictureBitmap(inflater.getContext()));
                memberView.setOnClickListener(view -> onMemberClick.accept(member));
                row.addView(memberView);
            }
            membersGrid.addView(row);
        }
        return poleView;
    }

}