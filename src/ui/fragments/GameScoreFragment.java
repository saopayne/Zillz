package ui.fragments;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mechanics.informations.GameInformation;
import mechanics.informations.GameInformationStandard;
import mechanics.informations.GameInformationTime;
import mechanics.routine.Routine;
import model.LevelInformation;
import model.PlayerProfile;
import model.inventory.InventoryItemEntry;
import model.inventory.InventoryItemEntryFactory;
import model.mode.GameMode;
import model.mode.GameModeFactory;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tracchis.saopayne.zills.R;

public class GameScoreFragment extends Fragment implements View.OnClickListener {
	public static final String FRAGMENT_TAG = "GameScoreFragment_TAG";
	public static final String EXTRA_GAME_INFORMATION = "GameScoreFragment.Extra.GameInformation";
	private static final String BUNDLE_IS_DISPLAY_DONE = GameScoreFragment.class.getName() + ".Bundle.isDisplayDone";
	private static final String BUNDLE_HAS_LEVELED_UP = GameScoreFragment.class.getName() + ".Bundle.hasLeveledUp";
	private static final String BUNDLE_HAS_INCREASED_RANK = GameScoreFragment.class.getName() + ".Bundle.hasIncreaseRank";
	private static final String BUNDLE_CURRENT_FINAL_SCORE =
			GameScoreFragment.class.getName() + ".Bundle.currentFinalScore";
	private static final String BUNDLE_CURRENT_ACHIEVEMENT_CHECKED =
			GameScoreFragment.class.getName() + ".Bundle.achievementChecked";
	private static final String BUNDLE_CURRENT_PLAYER_PROFILE_SAVED =
			GameScoreFragment.class.getName() + ".Bundle.playerProfileSaved";
	private static final String BUNDLE_CURRENT_EXP_EARNED =
			GameScoreFragment.class.getName() + ".Bundle.expEarned";
	private static final String BUNDLE_CURRENT_EXP_BAR =
			GameScoreFragment.class.getName() + ".Bundle.expBar";

	private static final long CLICK_DELAY = 1400;
	private static final long TICK_INTERVAL = 100;
	private static final int NUMBER_OF_TICK = 30;
	
	private AdView adView;

	private Listener mListener = null;
	private GameInformationStandard mGameInformation;
	private Routine mRoutine;
	private float mCurrentExpEarned;
	private float mExpEarnedByTick;
	private float mFinalScoreByTick;
	private float mCurrentFinalScore;
	private float mCurrentExpBar;
	private float mExpBarByTick;
	private float mCurrentTickNumber;
	private boolean mIsDisplayDone = false;
	private boolean mAchievementChecked = false;
	private PlayerProfile mPlayerProfile;
	private boolean mPlayerProfileSaved = false;
	//Details from game played
	private long mRetrievedBulletFired;
	private long mRetrievedTargetKilled;
	private long mRetrievedExpEarned;
	private long mRetrievedCombo;
	private long mRetrievedScore;
	private long mRetrievedExpBar;
	//UI
	private TextView mFinalScoreTopTextView;
	private TextView mFinalScoreBottomTextView;
	private TextView mExpEarnedTextView;
	private Button mSkipButton;
	private View mSignInView;
	private ProgressBar mExpbar;
	private long mAttachTime;
	//Congratz card
	private boolean mHasLeveledUp;
	private boolean mHasIncreaseRank;
	
	
	public static GameScoreFragment newInstance(GameInformation gameInformation) {
		final GameScoreFragment fragment = new GameScoreFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(GameScoreFragment.EXTRA_GAME_INFORMATION,
				gameInformation);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAttachTime = System.currentTimeMillis();
		if (activity instanceof GameScoreFragment.Listener) {
			mListener = (GameScoreFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameScoreFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		if (mRoutine != null) {
			mRoutine.stopRoutine();
			mRoutine = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Resources res = getResources();
		View v = inflater.inflate(R.layout.fragment_score, container, false);
		

		
		final int[] clickable = new int[]{
				R.id.score_button_replay,
				R.id.score_button_home,
				R.id.score_button_skip,
				R.id.score_button_share
		};
		for (int i : clickable) {
			v.findViewById(i).setOnClickListener(this);
		}

		if (savedInstanceState != null) {
			mIsDisplayDone = savedInstanceState.getBoolean(BUNDLE_IS_DISPLAY_DONE, false);
			mAchievementChecked = savedInstanceState.getBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, false);
			mPlayerProfileSaved = savedInstanceState.getBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, false);
			mHasIncreaseRank = savedInstanceState.getBoolean(BUNDLE_HAS_INCREASED_RANK, false);
			mHasLeveledUp = savedInstanceState.getBoolean(BUNDLE_HAS_LEVELED_UP, false);
		}

		if (getArguments().containsKey(EXTRA_GAME_INFORMATION)) {
			mGameInformation = getArguments().getParcelable(EXTRA_GAME_INFORMATION);
			retrieveGameDetails(mGameInformation);

			//set info to details card
			((TextView) v.findViewById(R.id.numberOfTargetsKilled)).setText(String.valueOf(mRetrievedTargetKilled));
			((TextView) v.findViewById(R.id.numberOfBulletsFired)).setText(String.valueOf(mRetrievedBulletFired));
			((TextView) v.findViewById(R.id.maxCombo)).setText(String.valueOf(mRetrievedCombo));
			((TextView) v.findViewById(R.id.expEarned)).setText(String.valueOf(mRetrievedExpEarned));
		}

		//update playerProfile with value of this game
		updatePlayerProfile();

		//populate the view
		final LevelInformation levelInformation = mPlayerProfile.getLevelInformation();
		((TextView) v.findViewById(R.id.result_level)).setText(String.format(getString(R.string.profile_level), levelInformation.getLevel()));
		((TextView) v.findViewById(R.id.result_current_exp)).setText(String.format(getString(R.string.profile_exp), levelInformation.getExpProgress(), levelInformation.getExpNeededToLevelUp()));
		mRetrievedExpBar = levelInformation.getProgressInPercent();

		//congratz card ?
		final View congratzCard = v.findViewById(R.id.result_card_congratz);
		final TextView congratsText = (TextView) v.findViewById(R.id.result_congratz_message);
		if (mHasLeveledUp) {
			congratzCard.setVisibility(View.VISIBLE);
			congratsText.setText(getString(R.string.score_congratz_level_up) + "\n");
		}
		if (mHasIncreaseRank) {
			congratzCard.setVisibility(View.VISIBLE);
			congratsText.setText(congratsText.getText() + getString(R.string.score_congratz_rank_up));
		}

		mFinalScoreTopTextView = (TextView) v.findViewById(R.id.result_score_top);
		mFinalScoreBottomTextView = (TextView) v.findViewById(R.id.finalScore);
		mSkipButton = (Button) v.findViewById(R.id.score_button_skip);
		mSignInView = v.findViewById(R.id.sign_in_message);
		mExpEarnedTextView = (TextView) v.findViewById(R.id.result_earned_exp);
		mExpbar = (ProgressBar) v.findViewById(R.id.result_level_progess_bar);

		HashMap<Integer, Integer> loots = mGameInformation.getLoot();
		if (loots.size() != 0) {
			String stringLoot = "";
			for (Map.Entry<Integer, Integer> entry : loots.entrySet()) {
				InventoryItemEntry inventoryItemEntry = InventoryItemEntryFactory.create(entry.getKey(), entry.getValue());
				final long quantityDropped = inventoryItemEntry.getQuantityAvailable();
				final int titleResourceId = inventoryItemEntry.getTitleResourceId();
				stringLoot += String.valueOf(quantityDropped) + "x " + res.getQuantityString(titleResourceId, (int) quantityDropped) + "\n";
			}
			stringLoot = stringLoot.substring(0, stringLoot.length() - 1);
			((TextView) v.findViewById(R.id.score_loot_list)).setText(stringLoot);
		}

		//show the right rank
		String[] ranks = res.getStringArray(R.array.ranks_array_full);
		String[] grades = res.getStringArray(R.array.ranks_array_letter);
		final int gameRank = mGameInformation.getRank();
		switch (gameRank) {
			case GameModeFactory.GAME_RANK_DESERTER:
			case GameModeFactory.GAME_RANK_SOLDIER:
			case GameModeFactory.GAME_RANK_CORPORAL:
			case GameModeFactory.GAME_RANK_SERGEANT:
			case GameModeFactory.GAME_RANK_ADMIRAL:
				((TextView) v.findViewById(R.id.result_rang)).setText(ranks[gameRank]);
				((TextView) v.findViewById(R.id.result_grade)).setText(grades[gameRank]);
				break;
			default:
				((TextView) v.findViewById(R.id.result_rang)).setText(ranks[0]);
				((TextView) v.findViewById(R.id.result_grade)).setText(grades[0]);
				break;
		}


		return v;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		mRoutine = new Routine(Routine.TYPE_TICKER, TICK_INTERVAL) {
			@Override
			protected void run() {
				if (mCurrentTickNumber >= NUMBER_OF_TICK) {
					finalizeScoreDisplayed();
				} else {
					incrementCurrentScoreDisplayed();
				}
			}
		};

		if (mIsDisplayDone) {
			finalizeScoreDisplayed();
		} else if (hasSomethingToDisplay()) {
			initScoreDisplay(savedInstanceState);
			mRoutine.startRoutine();
		} else {
			mSkipButton.setVisibility(View.GONE);
			mIsDisplayDone = true;
		}
	}

	@Override
	public void onClick(View view) {
		if (mIsDisplayDone || (System.currentTimeMillis() - mAttachTime > CLICK_DELAY)) {
			
				if (view.getId() == R.id.score_button_home){
					mListener.onHomeRequested();
					}
				if (view.getId() ==  R.id.score_button_skip){
					finalizeScoreDisplayed();
				}
			    if (view.getId() ==  R.id.score_button_replay){
					mListener.onReplayRequested(mGameInformation);
			    }
				if (view.getId() == R.id.score_button_share){
					mListener.onShareScoreRequested(mRetrievedScore);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(BUNDLE_IS_DISPLAY_DONE, mIsDisplayDone);
		outState.putFloat(BUNDLE_CURRENT_FINAL_SCORE, mCurrentFinalScore);
		outState.putBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, mAchievementChecked);
		outState.putBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, mPlayerProfileSaved);
		outState.putFloat(BUNDLE_CURRENT_EXP_EARNED, mCurrentExpEarned);
		outState.putBoolean(BUNDLE_HAS_LEVELED_UP, mHasLeveledUp);
		outState.putBoolean(BUNDLE_HAS_INCREASED_RANK, mHasIncreaseRank);
	}

	public void notifySignedStateChanged(boolean signedIn) {
		if (signedIn) {
			mSignInView.setVisibility(View.GONE);
			if (!mAchievementChecked) {
				mListener.onUpdateAchievements(mGameInformation, mPlayerProfile);
				mAchievementChecked = true;
			}
		} else {
			mSignInView.setVisibility(View.VISIBLE);
		}
	}

	private void retrieveGameDetails(GameInformationStandard gameInformation) {
		mRetrievedBulletFired = gameInformation.getBulletsFired();
		mRetrievedTargetKilled = gameInformation.getFragNumber();
		mRetrievedCombo = gameInformation.getMaxCombo();
		mRetrievedExpEarned = gameInformation.getExpEarned();
		//TODO find a better way to display score
		final int gameType = gameInformation.getGameMode().getType();
		if (gameType == GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING
				|| gameType == GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW) {
			mRetrievedScore = ((GameInformationTime) gameInformation).getPlayingTime();
		} else {
			mRetrievedScore = gameInformation.getCurrentScore();
		}
	}

	private void initCurrentScoreDisplayed(Bundle savedBundle) {
		if (savedBundle != null) {
			mCurrentFinalScore = savedBundle.getFloat(BUNDLE_CURRENT_FINAL_SCORE);
			mCurrentExpEarned = savedBundle.getFloat(BUNDLE_CURRENT_EXP_EARNED);
			mCurrentExpEarned = savedBundle.getFloat(BUNDLE_CURRENT_EXP_BAR);
		} else {
			mCurrentFinalScore = 0;
			mCurrentExpEarned = 0;
			mCurrentExpBar = 0;
		}
	}

	private void initScoreByTick() {
		if (mGameInformation != null) {
			mFinalScoreByTick = (float) (mRetrievedScore - mCurrentFinalScore) / NUMBER_OF_TICK;
			mExpEarnedByTick = (float) (mRetrievedExpEarned - mCurrentExpEarned) / NUMBER_OF_TICK;
			mExpBarByTick = (float) (mRetrievedExpBar - mCurrentExpBar) / NUMBER_OF_TICK;
		}
	}

	private void initScoreDisplay(Bundle savedBundle) {
		mCurrentTickNumber = 0;
		initCurrentScoreDisplayed(savedBundle);
		initScoreByTick();
	}

	private void incrementCurrentScoreDisplayed() {
		mCurrentTickNumber++;
		mCurrentFinalScore += mFinalScoreByTick;
		mCurrentExpEarned += mExpEarnedByTick;
		mCurrentExpBar += mExpBarByTick;

		displayDetails(
				(long) mCurrentFinalScore,
				(long) mCurrentExpEarned,
				(long) mCurrentExpBar);
	}

	private void finalizeScoreDisplayed() {
		mRoutine.stopRoutine();

		displayDetails(mRetrievedScore, mRetrievedExpEarned, mRetrievedExpBar);

		if (mIsDisplayDone) {
			mSkipButton.setVisibility(View.GONE);
		} else {
			fadeOut(mSkipButton);
			mIsDisplayDone = true;
		}

	}

	private void displayDetails(long score, long expEarned, long progressBar) {
		mExpEarnedTextView.setText(String.format(getString(R.string.score_added_exp), expEarned));
		mExpbar.setProgress((int) progressBar);
		//TODO create an abstract method displaysGamesDetails and implement a specific behavior for each mode
		switch (mGameInformation.getGameMode().getType()) {

			case GameModeFactory.GAME_TYPE_SURVIVAL:
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
			case GameModeFactory.GAME_TYPE_TUTORIAL:
			case GameModeFactory.GAME_TYPE_MEMORIZE:
				mFinalScoreTopTextView.setText(String.valueOf(score));
				mFinalScoreBottomTextView.setText(String.valueOf(score));
				break;
			case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				final Calendar cl = Calendar.getInstance();
				cl.setTimeInMillis(score);
				mFinalScoreTopTextView.setText(cl.get(Calendar.SECOND) + "' " + cl.get(Calendar.MILLISECOND) + "''");
				mFinalScoreBottomTextView.setText(cl.get(Calendar.SECOND) + "' " + cl.get(Calendar.MILLISECOND) + "''");
				break;
		}
	}

	private boolean hasSomethingToDisplay() {
		return mGameInformation.getCurrentScore() != 0 ||
				mGameInformation.getExpEarned() != 0;

	}

	private void fadeOut(final View view) {
		final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(500);
		fadeOutAnimation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				view.setVisibility(View.GONE);
				fadeOutAnimation.removeListener(this);
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				fadeOutAnimation.removeListener(this);
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		fadeOutAnimation.start();
	}

	private void updatePlayerProfile() {
		final GameMode gameMode = mGameInformation.getGameMode();
		final int oldLevel = mPlayerProfile.getLevelInformation().getLevel();
		final int oldRank = mPlayerProfile.getRankByGameMode(gameMode);
		if (mGameInformation != null && !mPlayerProfileSaved) {
			mPlayerProfile.increaseBulletsFired(mRetrievedBulletFired);
			mPlayerProfile.increaseGamesPlayed(1);
			mPlayerProfile.increaseTargetsKilled(mRetrievedTargetKilled);
			mPlayerProfile.increaseBulletsMissed(mGameInformation.getBulletsMissed());
			mPlayerProfile.increaseExperienceEarned(mRetrievedExpEarned);
			mPlayerProfile.setRankByGameMode(gameMode, mGameInformation.getRank());
			updateInventoryEntryQuantity();
			mGameInformation.useBonus(mPlayerProfile);
			mPlayerProfileSaved = mPlayerProfile.saveChanges();
		}
		//check if player has leveled up
		if (oldLevel < mPlayerProfile.getLevelInformation().getLevel()) {
			mHasLeveledUp = true;
		}
		//check if player has increased his rank
		if (oldRank < mPlayerProfile.getRankByGameMode(gameMode)) {
			mHasIncreaseRank = true;
		}
	}

	private void updateInventoryEntryQuantity() {
		for (Map.Entry<Integer, Integer> entry : mGameInformation.getLoot().entrySet()) {
			mPlayerProfile.increaseInventoryItemQuantity(entry.getKey(), entry.getValue());
		}
	}

	//interface
	public interface Listener {
		public void onReplayRequested(GameInformation gameInformation);

		public void onHomeRequested();

		public void onUpdateAchievements(final GameInformationStandard gameInformation, final PlayerProfile playerProfile);

		public void onShareScoreRequested(long score);
	}
}
