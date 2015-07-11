package ui.adapter;

import java.util.ArrayList;

import model.PlayerProfile;
import model.mode.GameMode;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tracchis.saopayne.zills.R;


public class GameModeDetailsAdapter extends ArrayAdapter<GameMode> {

	private ArrayList<GameMode> mGameModes;
	private PlayerProfile mPlayerProfile;
	public Listener mListener;

	public GameModeDetailsAdapter(Context context, ArrayList<GameMode> gameModes, Listener l, PlayerProfile p) {
		super(context, R.layout.row_mode_details_entry, gameModes);
		mListener = l;
		mGameModes = gameModes;
		mPlayerProfile = p;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = getContext();
		final Resources res = context.getResources();
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_mode_details_entry, parent, false);
		}

		final GameMode currentGameMode = mGameModes.get(position);
		final TextView title = (TextView) rowView.findViewById(R.id.row_game_details_title);
		title.setText(currentGameMode.getTitle());
		final ImageView icon = (ImageView) rowView.findViewById(R.id.row_game_details_icon);
		icon.setBackgroundResource(currentGameMode.getImage());
		final TextView rank = (TextView) rowView.findViewById(R.id.row_game_details_rank);
		String[] grades = res.getStringArray(R.array.ranks_array_full);
		final int currentRank = mPlayerProfile.getRankByGameMode(currentGameMode);
		rank.setText(grades[currentRank]);

		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onGameModeSelected(currentGameMode);
			}
		});
		return rowView;
	}

	public interface Listener {
		public void onGameModeSelected(GameMode gameMode);
	}
}
