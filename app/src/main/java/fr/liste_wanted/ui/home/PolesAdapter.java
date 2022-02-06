package fr.liste_wanted.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Member;
import fr.liste_wanted.data.Pole;

public class PolesAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Pole> poles;
    private int membersPerRow;

    public PolesAdapter(Context context, List<Pole> poles, int membersPerRow) {
        this.poles = poles;
        inflater = LayoutInflater.from(context);
        this.membersPerRow = membersPerRow;
    }

    public void setPoles(List<Pole> poles) {
        this.poles = poles;
        notifyDataSetChanged();
    }

    public int getMembersPerRow() {
        return membersPerRow;
    }
    public void setMembersPerRow(int membersPerRow) {
        this.membersPerRow = membersPerRow;
    }

    @Override
    public int getCount() {
        return poles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Pole pole = poles.get(i);
        View poleView = inflater.inflate(R.layout.view_pole, viewGroup, false);
        ((TextView)poleView.findViewById(R.id.text_name)).setText(pole.getName());
        LinearLayout membersGrid = poleView.findViewById(R.id.linear_pole);
        for (int l = 0; l < pole.size()*1f/ membersPerRow; l++) {
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row, membersGrid, false);
            row.setWeightSum(membersPerRow);
            for (int j = l* membersPerRow; j < (l+1)*membersPerRow && j < pole.size(); j++) {
                View memberView = inflater.inflate(R.layout.view_member, row, false);
                Member member = pole.get(j);
                ((TextView) memberView.findViewById(R.id.name)).setText(member.getFirstname());
                ((TextView) memberView.findViewById(R.id.nickname)).setText(member.getNickname());
                ((TextView) memberView.findViewById(R.id.role)).setText(member.getRole());
                if (member.getRole() == null)
                    ((TextView) memberView.findViewById(R.id.role)).setHeight(0);
                ((TextView) memberView.findViewById(R.id.description)).setText(member.getDescription());
                ((ImageView) memberView.findViewById(R.id.picture)).setImageResource(member.getPictureResourceId(inflater.getContext()));
                row.addView(memberView);
            }
            membersGrid.addView(row);
        }
        return poleView;
    }
}
