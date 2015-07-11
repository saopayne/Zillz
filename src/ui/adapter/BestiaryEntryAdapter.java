package ui.adapter;

import model.bestiary.BestiaryEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tracchis.saopayne.zills.R;

public class BestiaryEntryAdapter extends ArrayAdapter<BestiaryEntry> {
	private BestiaryEntry[] mBestiaryEntries;

	public BestiaryEntryAdapter(Context context, BestiaryEntry[] bestiaryEntries) {
		super(context, R.layout.row_bestiary_entry, bestiaryEntries);
		mBestiaryEntries = bestiaryEntries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = getContext();
		final BestiaryEntry currentBestiaryEntry = mBestiaryEntries[position];
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = convertView;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_bestiary_entry, parent, false);
		}

		((TextView) rowView.findViewById(R.id.row_bestiary_entry_title)).setText(currentBestiaryEntry.getTitleResourceId());
		((ImageView) rowView.findViewById(R.id.row_bestiary_entry_image)).setImageResource(currentBestiaryEntry.getImageResourceId());
		((TextView) rowView.findViewById(R.id.bestiary_health)).setText(currentBestiaryEntry.getHealth());
		((TextView) rowView.findViewById(R.id.bestiary_point)).setText(currentBestiaryEntry.getPointValue());
		((TextView) rowView.findViewById(R.id.bestiary_exp)).setText(currentBestiaryEntry.getExpValue());
		

		return rowView;

	}
}
