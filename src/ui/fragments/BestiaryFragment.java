package ui.fragments;

import model.DisplayableItemFactory;
import model.bestiary.BestiaryEntry;
import model.bestiary.BestiaryEntryFactory;
import ui.adapter.BestiaryEntryAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.tracchis.saopayne.zills.R;

public class BestiaryFragment extends Fragment {
	private GridView mBestiaryGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bestiary, container, false);
		mBestiaryGridView = ((GridView) v.findViewById(R.id.bestiary_grid_view));
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBestiaryGridView.setAdapter(new BestiaryEntryAdapter(getActivity(), new BestiaryEntry[]{
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_EASY_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_BLOND_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_BABY_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_GHOST_WITH_HELMET),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_HIDDEN_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_KING_GHOST)

		}));
	}
}
