package fr.liste_wanted.ui.defis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Defi;
import fr.liste_wanted.data.Defis;

public class DefisListAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private Defis defis;

    public DefisListAdapter(Context context) {
        this.context = context;
        this.defis = new Defis();
        inflater = LayoutInflater.from(context);
    }

    public void setDefis(Defis defis) {
        this.defis = defis;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return defis.getDefis().size();
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
    public View getView(int i, View defiView, ViewGroup viewGroup) {
        if (defiView==null)
            defiView = inflater.inflate(R.layout.view_defi, viewGroup, false);
        Defi defi = defis.getDefis().get(i);
        ((TextView) defiView.findViewById(R.id.author)).setText(context.getString(R.string.defi_title, defi.getNumber(), defi.getAuthor()));
        ((TextView) defiView.findViewById(R.id.task)).setText(defi.getTask());
        defiView.findViewById(R.id.finished).setVisibility(defi.isFinished() ? View.VISIBLE : View.INVISIBLE);
        return defiView;
    }
}
