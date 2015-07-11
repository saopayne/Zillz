package ui.fragments;

import java.util.ArrayList;

import model.PlayerProfile;
import model.mode.GameMode;
import model.mode.GameModeFactory;
import ui.adapter.GameModeDetailsAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.tracchis.saopayne.zills.R;

public class GameModeDetailsFragment extends Fragment implements GameModeDetailsAdapter.Listener {
	public static final String TAG = "GameModeDetailsFragment_TAG";

	private Listener mListener;
	private PlayerProfile mPlayerProfile;
	private GameModeDetailsAdapter mGameModeDetailsAdapter;

	public interface Listener {
		void onGameModeDetailsRequest(GameMode gameMode);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameModeDetailsFragment.Listener) {
			mListener = (GameModeDetailsFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameModeDetailsFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_mode_details, container, false);
		mGameModeDetailsAdapter = new GameModeDetailsAdapter(getActivity(), new ArrayList<GameMode>(), this, mPlayerProfile);
		((GridView) v.findViewById(R.id.mode_details_grid_view)).setAdapter(mGameModeDetailsAdapter);
		loadGameMode();
		return v;
	}

	private void loadGameMode() {
		mGameModeDetailsAdapter.clear();

		//First mission: Scouts First
		//Sprint mode
		mGameModeDetailsAdapter.add(GameModeFactory.createRemainingTimeGame(1));

		//Second mission: Everything is an illusion
		//Twenty in a row
		mGameModeDetailsAdapter.add(GameModeFactory.createTwentyInARow(1));

		//Third mission: Prove your stamina
		//Marathon mode
		mGameModeDetailsAdapter.add(GameModeFactory.createRemainingTimeGame(3));

		//Fourth mission: Brainteaser
		//Memorize
		mGameModeDetailsAdapter.add(GameModeFactory.createMemorize(1));

		//Fifth mission: Death to the king
		//Death to the king
		mGameModeDetailsAdapter.add(GameModeFactory.createKillTheKingGame(1));

		//Sixth mission: The Final Battle
		mGameModeDetailsAdapter.add(GameModeFactory.createSurvivalGame(1));

		mGameModeDetailsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGameModeSelected(GameMode gameMode) {
		mListener.onGameModeDetailsRequest(gameMode);
	}
}
