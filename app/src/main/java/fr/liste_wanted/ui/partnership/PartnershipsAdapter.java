package fr.liste_wanted.ui.partnership;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.liste_wanted.R;
import fr.liste_wanted.data.Partnership;

public class PartnershipsAdapter extends BaseAdapter {

    private Context context;
    private List<Partnership> partnerships;
    private LayoutInflater inflater;

    public PartnershipsAdapter(Context context, List<Partnership> partnerships) {
        this.context = context;
        this.partnerships = partnerships;
        this.inflater = LayoutInflater.from(context);
    }

    public void setPartnerships(List<Partnership> partnerships) {
        this.partnerships = partnerships;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return partnerships.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View partnershipView, ViewGroup viewGroup) {
        if (partnershipView==null)
            partnershipView = inflater.inflate(R.layout.view_partnership, viewGroup, false);
        Partnership partnership = partnerships.get(i);
        ((TextView)partnershipView.findViewById(R.id.name)).setText(partnership.getName());
        ImageView imageView = partnershipView.findViewById(R.id.image_partnership);
        if (partnership.hasImageUrl()) Glide.with(partnershipView).load(partnership.getImageUrl()).into(imageView);
        else imageView.setImageResource(partnership.getDrawableResourceId(context));
        if (partnership.getColor() != null)
            imageView.setBackgroundColor(Color.parseColor(partnership.getColor()));
        return partnershipView;
    }
}
