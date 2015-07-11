package ui.fragments;

import model.PlayerProfile;
import model.mode.GameMode;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tracchis.saopayne.zills.R;


public class GameModeFragment extends Fragment {

	public static final String TAG = "GameModeFragment_TAG";
	public static final String EXTRA_GAME_MODE = GameModeFragment.class.getName() + "extra_game_mode";

	private GameMode mGameMode;
	private PlayerProfile mPlayerProfile;
	private GameModeFragment.Listener mListener;


	public static GameModeFragment newInstance(GameMode gameMode) {
		final GameModeFragment fragment = new GameModeFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(GameModeFragment.EXTRA_GAME_MODE,
				gameMode);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
				PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));

		if (activity instanceof GameModeFragment.Listener) {
			//show play button
			mListener = (GameModeFragment.Listener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Resources res = getResources();

		View v = inflater.inflate(R.layout.fragment_details, container, false);

		if (getArguments().containsKey(EXTRA_GAME_MODE)) {
			mGameMode = getArguments().getParcelable(EXTRA_GAME_MODE);
		}

		String[] ranks = res.getStringArray(R.array.ranks_array_full);
		String[] grades = res.getStringArray(R.array.ranks_array_letter);

		final TextView rankTitle = (TextView) v.findViewById(R.id.details_rank);
		final TextView rankLetter = (TextView) v.findViewById(R.id.details_rank_letter);
		final ProgressBar progression = (ProgressBar) v.findViewById(R.id.details_progess_bar);
		final TextView progressText = (TextView) v.findViewById(R.id.details_progression);
		final TextView title = (TextView) v.findViewById(R.id.details_title);
		final int rank = mPlayerProfile.getRankByGameMode(mGameMode);
		final int progress = 100 - (int) ((((float) (ranks.length - 1) - rank) / (float) (ranks.length - 1)) * 100);
		final TextView admiral = (TextView) v.findViewById(R.id.admiral_description);
		final TextView sergeant = (TextView) v.findViewById(R.id.sergeant_description);
		final TextView corporal = (TextView) v.findViewById(R.id.corporal_description);
		final TextView soldier = (TextView) v.findViewById(R.id.soldier_description);
		final TextView deserter = (TextView) v.findViewById(R.id.deserter_description);
		final TextView longDescription = (TextView) v.findViewById(R.id.details_description);
		final int descriptionId = mGameMode.getLongDescription();

		rankTitle.setText(ranks[rank]);
		rankLetter.setText(grades[rank]);
		progression.setProgress(progress);
		progressText.setText(String.valueOf(progress) + " %");
		title.setText(mGameMode.getTitle());
		admiral.setText(mGameMode.getAdmiralRankRule(res));
		sergeant.setText(mGameMode.getSergeantRankRule(res));
		corporal.setText(mGameMode.getCorporalRankRule(res));
		soldier.setText(mGameMode.getSoldierRankRule(res));
		deserter.setText(mGameMode.getDeserterRankRule(res));
		if (descriptionId != -1) longDescription.setText(descriptionId);

		if (mListener != null) {
			//show button play
			final Button start = (Button) v.findViewById(R.id.details_play);
			start.setVisibility(View.VISIBLE);
			start.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onPlayRequest(mGameMode);
				}
			});
		}

		return v;
	}

	public interface Listener {
		public void onPlayRequest(GameMode gameMode);
	}

}
