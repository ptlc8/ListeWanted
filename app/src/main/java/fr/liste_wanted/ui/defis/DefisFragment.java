package fr.liste_wanted.ui.defis;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import fr.liste_wanted.Defis;
import fr.liste_wanted.R;
import fr.liste_wanted.data.Defi;
import fr.liste_wanted.databinding.FragmentDefisBinding;

public class DefisFragment extends Fragment {

    private Defis defis;
    private FragmentDefisBinding binding;
    private DefisListAdapter defisListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        defis = new Defis();

        binding = FragmentDefisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView defisListView = root.findViewById(R.id.list_defis);
        defisListAdapter = new DefisListAdapter(getContext());
        defisListView.setAdapter(defisListAdapter);

        SwipeRefreshLayout swipe2refresh = root.findViewById(R.id.swipe2refresh);
        swipe2refresh.setOnRefreshListener(() -> {
            refresh(()->swipe2refresh.setRefreshing(false));
        });

        return root;
    }

    public void refresh(Runnable onRefresh) {
        defis.refresh(defis -> {
            getActivity().runOnUiThread(()-> {
                defisListAdapter.setDefis(defis);
            });
            onRefresh.run();
        }, ioe -> {
            Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_LONG).show();
            onRefresh.run();
        }, se -> {
            Toast.makeText(getContext(), "Erreur côté serveur, veuillez réésayer plus tard", Toast.LENGTH_LONG).show();
            onRefresh.run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public static class DefisListAdapter extends BaseAdapter {

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
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = inflater.inflate(R.layout.view_defi, viewGroup, false);
            Defi defi = defis.getDefis().get(i);
            ((TextView)view.findViewById(R.id.author)).setText(context.getString(R.string.defi_title, defi.getNumber(), defi.getAuthor()));
            ((TextView)view.findViewById(R.id.task)).setText(defi.getTask());
            view.findViewById(R.id.finished).setVisibility(View.VISIBLE);
            return view;
        }
    }
}