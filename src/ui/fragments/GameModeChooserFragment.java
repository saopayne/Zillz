package ui.fragments;

import java.util.ArrayList;

import model.PlayerProfile;
import model.mode.GameMode;
import model.mode.GameModeFactory;
import ui.adapter.GameModeViewAdapter;
import ui.customviews.GameModeView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tracchis.saopayne.zills.R;


public class GameModeChooserFragment extends Fragment implements GameModeViewAdapter.Listener {


	private Listener mListener;
	private PlayerProfile mPlayerProfile;
	private GameModeViewAdapter mGameModeViewAdapter;
	private AdView adView;


	public interface Listener {
		public void onLevelChosen(GameModeView g);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameScoreFragment.Listener) {
			mListener = (GameModeChooserFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameModeChooserFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_game_mode_chooser, container, false);
		
		
		
		mGameModeViewAdapter = new GameModeViewAdapter(getActivity(), new ArrayList<GameMode>(), mPlayerProfile, this);
		((GridView) v.findViewById(R.id.gamemode_grid_view)).setAdapter(mGameModeViewAdapter);

		loadGameMode();
		return v;
	}

	private void loadGameMode() {
		mGameModeViewAdapter.clear();

		//how to play : learn basics of the game play
		mGameModeViewAdapter.add(GameModeFactory.createTutorialGame());

		//First mission: Scouts First
		//Sprint mode
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(1));

		//Second mission: Everything is an illusion
		//Twenty in a row
		mGameModeViewAdapter.add(GameModeFactory.createTwentyInARow(1));

		//Third mission: Prove your stamina
		//Marathon mode
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(3));

		//Fourth mission: Brainteaser
		//Memorize
		mGameModeViewAdapter.add(GameModeFactory.createMemorize(1));

		//Fifth mission: Death to the king
		//Death to the king
		mGameModeViewAdapter.add(GameModeFactory.createKillTheKingGame(1));

		//Sixth mission: The Final Battle
		mGameModeViewAdapter.add(GameModeFactory.createSurvivalGame(1));

		mGameModeViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGameModeSelected(GameModeView view) {
		mListener.onLevelChosen(view);
	}
}
