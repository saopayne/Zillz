package beta;
import googleish.BaseGameActivity;
import mechanics.informations.GameInformation;
import mechanics.informations.GameInformationStandard;
import mechanics.informations.GameInformationTime;
import model.PlayerProfile;
import model.mode.GameMode;
import model.mode.GameModeFactory;
import model.weapon.Weapon;
import ui.customviews.GameModeView;
import ui.fragments.AboutFragment;
import ui.fragments.BonusFragment;
import ui.fragments.GameHomeFragment;
import ui.fragments.GameModeChooserFragment;
import ui.fragments.GameModeFragment;
import ui.fragments.GameScoreFragment;
import ui.fragments.LeaderboardChooserFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tracchis.saopayne.zills.R;


public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener, GameScoreFragment.Listener,
		GameModeChooserFragment.Listener, LeaderboardChooserFragment.Listener, BonusFragment.Listener, GameModeFragment.Listener {
	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;
	private static final int REQUEST_GAME_ACTIVITY_FRESH_START = 0x00000002;
	private static final int REQUEST_GAME_ACTIVITY_REPLAY = 0x00000003;
	//Achievement
	private static final int ACHIEVEMENT_NOVICE_LOOTER_LIMIT = 20;
	private static final int ACHIEVEMENT_TRAINED_LOOTER_LIMIT = 65;
	private static final int ACHIEVEMENT_EXPERT_LOOTER_LIMIT = 90;

	private Toast mTextToast;
	AdView adView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_home);
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					new GameHomeFragment(), GameHomeFragment.FRAGMENT_TAG).commit();
		}
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

		if (REQUEST_GAME_ACTIVITY_REPLAY == request) {
			getSupportFragmentManager().popBackStackImmediate();
		}

		if (RESULT_OK == response) {
			if (REQUEST_GAME_ACTIVITY_REPLAY == request || REQUEST_GAME_ACTIVITY_FRESH_START == request) {
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.game_home_fragment_container,
								GameScoreFragment.newInstance(
										(GameInformation) data.getParcelableExtra(GameScoreFragment.EXTRA_GAME_INFORMATION)),
								GameScoreFragment.FRAGMENT_TAG)
						.addToBackStack(null).commitAllowingStateLoss();
			}
		}

		if (ARActivity.RESULT_SENSOR_NOT_SUPPORTED == response) {
			makeToast(getString(R.string.home_device_not_compatible) + " (rotation sensor)");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		hideToast();
	}

	@Override
	public void onSignInFailed() {
		final Fragment gameScoreFragment = getSupportFragmentManager()
				.findFragmentByTag(GameScoreFragment.FRAGMENT_TAG);
		if (gameScoreFragment != null) {
			((GameScoreFragment) gameScoreFragment).notifySignedStateChanged(false);
		}
	}

	@Override
	public void onSignInSucceeded() {
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentByTag(GameHomeFragment.FRAGMENT_TAG);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(true);
		}
		
		final Fragment gameScoreFragment = getSupportFragmentManager()
				.findFragmentByTag(GameScoreFragment.FRAGMENT_TAG);
		if (gameScoreFragment != null) {
			((GameScoreFragment) gameScoreFragment).notifySignedStateChanged(true);
		}
	}

	@Override
	public void onSignOutSucceeded() {
		makeToast(getString(R.string.home_sign_out_success));
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentByTag(GameHomeFragment.FRAGMENT_TAG);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(false);
		}
		final Fragment gameScoreFragment = getSupportFragmentManager()
				.findFragmentByTag(GameScoreFragment.FRAGMENT_TAG);
		if (gameScoreFragment != null) {
			((GameScoreFragment) gameScoreFragment).notifySignedStateChanged(false);
		}
	}

	@Override
	public void onStartGameRequested() {
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				new GameModeChooserFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onShowAchievementsRequested() {
		final GamesClient gamesClient = getGamesClient();
		if (gamesClient.isConnected()) {
			startActivityForResult(gamesClient.getAchievementsIntent(), REQUEST_ACHIEVEMENT);
		} else {
			makeToast(getResources().getString(R.string.home_not_sign_in_achievement));
		}
	}

	@Override
	public void onShowLeaderboardsRequested() {
		if (getGamesClient().isConnected()) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.game_home_fragment_container, new LeaderboardChooserFragment())
					.addToBackStack(null)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
		} else {
			makeToast(getResources().getString(R.string.home_not_sign_in_leaderboard));
		}
	}

	@Override
	public void onShowAboutRequested() {
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				new AboutFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onSignInButtonClicked() {
		if (isNetworkAvailable()) {
			beginUserInitiatedSignIn();
		} else {
			makeToast(getResources().getString(R.string.home_internet_unavailable));
		}
	}

	@Override
	public void onSignOutButtonClicked() {
		SignOutConfirmDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
	}

	@Override
	public void onBatPictureClicked() {
		final GamesClient gamesClient = getGamesClient();
		if (gamesClient.isConnected()) {
			gamesClient.unlockAchievementImmediate(null, getResources().getString(R.string.achievement_curiosity));
		}
	}

	@Override
	public void onHelpRequested() {
		final Intent intent = new Intent(this, TutoActivity.class);
		intent.putExtra(TutoActivity.EXTRA_HELP_REQUESTED, true);
		startActivity(intent);
	}

	@Override
	public void onShowProfileRequested() {
		startActivity(new Intent(this, ProfileActivity.class));
	}

	@Override
	public void toast(String message) {
		makeToast(message);
	}

	/**
	 * use to inform user
	 *
	 * @param message display on screen
	 */
	private void makeToast(String message) {
		if (mTextToast != null) {
			mTextToast.cancel();
		}
		mTextToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		mTextToast.show();
	}

	private void hideToast() {
		if (mTextToast != null) {
			mTextToast.cancel();
			mTextToast = null;
		}
	}

	@Override
	public void onReplayRequested(GameInformation gameInformation) {
		startNewGame(gameInformation.getGameMode(), REQUEST_GAME_ACTIVITY_REPLAY);
	}

	@Override
	public void onHomeRequested() {
		Intent i = new Intent(this, HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

	@Override
	public void onUpdateAchievements(final GameInformationStandard gameInformation, final PlayerProfile playerProfile) {
		final GamesClient gamesClient = getGamesClient();
		if (gamesClient.isConnected()) {
			final long exp = playerProfile.getLevelInformation().getTotalExpEarned();
			final GameMode gameMode = gameInformation.getGameMode();
			final Weapon weapon = gameInformation.getWeapon();
			final int numberOfLoots = gameInformation.getNumberOfLoots();

			//TODO find a better way to retrieve score
			long score = gameInformation.getCurrentScore();
			final int gameType = gameInformation.getGameMode().getType();
			if (gameType == GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING
					|| gameType == GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW) {
				score = ((GameInformationTime) gameInformation).getPlayingTime();
			}

			//Submit score for the played game mode
			final int leaderBoardStringId = gameMode.getLeaderboardStringId();
			if (score > 0 && leaderBoardStringId != -1) {
				gamesClient.submitScore(getResources().getString(gameMode.getLeaderboardStringId()), score);
			}

			//Submit exp for the overall ranking
			gamesClient.submitScore(getResources().getString(R.string.leaderboard_overall_ranking), exp);

			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_soldier), 1);
			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_corporal), 1);
			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_sergeant), 1);
			if (score == 0) {
				gamesClient.unlockAchievement(getResources().getString(R.string.achievement_pacifist));
			} else if (!weapon.hasRunOutOfAmmo()) {
				gamesClient.unlockAchievement(getResources().getString(R.string.achievement_thrifty));
			}
			if (numberOfLoots >= ACHIEVEMENT_NOVICE_LOOTER_LIMIT) {
				gamesClient.unlockAchievement(getString(R.string.achievement_novice_looter));
			}

			if (numberOfLoots >= ACHIEVEMENT_TRAINED_LOOTER_LIMIT) {
				gamesClient.unlockAchievement(getString(R.string.achievement_trained_looter));
			}

			if (numberOfLoots >= ACHIEVEMENT_EXPERT_LOOTER_LIMIT) {
				gamesClient.unlockAchievement(getString(R.string.achievement_expert_looter));
			}

			//check ranks achievement
			if (playerProfile.getRankByGameMode(gameMode) == GameModeFactory.GAME_RANK_ADMIRAL) {
				switch (gameMode.getType()) {
					case GameModeFactory.GAME_TYPE_SURVIVAL:
						gamesClient.unlockAchievement(getString(R.string.achievement_the_final_battle_admiral_rank));
						break;
					case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
						gamesClient.unlockAchievement(getString(R.string.achievement_death_to_the_king_admiral_rank));
						break;
					case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
						gamesClient.unlockAchievement(getString(R.string.achievement_everything_is_an_illusion_admiral_rank));
						break;
					case GameModeFactory.GAME_TYPE_MEMORIZE:
						gamesClient.unlockAchievement(getString(R.string.achievement_brainteaser_admiral_rank));
						break;
					case GameModeFactory.GAME_TYPE_REMAINING_TIME:
						if (gameMode.getLevel() == 1) {
							gamesClient.unlockAchievement(getString(R.string.achievement_scouts_first_admiral_rank));
						} else if (gameMode.getLevel() == 3) {
							gamesClient.unlockAchievement(getString(R.string.achievement_prove_your_stamina__admiral_rank));
						}
						break;
				}
			}
		}
	}

	@Override
	public void onShareScoreRequested(long score) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.score_share_subject));
		intent.putExtra(Intent.EXTRA_TEXT,
				String.format(getResources().getString(R.string.score_share_content),
						score));
		startActivity(Intent.createChooser(intent,
				getResources().getString(R.string.score_share_dialog)));
	}

	@Override
	public void onLevelChosen(GameModeView g) {
		final GameMode gameMode = g.getModel();
		if (gameMode.getType() == GameModeFactory.GAME_TYPE_TUTORIAL) {
			//no details view for tutorial
			onGameStartRequest(gameMode);
		} else {
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					GameModeFragment.newInstance(gameMode), GameModeFragment.TAG).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		}
	}

	public void startNewGame(GameMode gameMode, int requestCode) {
		final Intent i = new Intent(this, GameActivity.class);
		i.putExtra(GameActivity.EXTRA_GAME_MODE, gameMode);
		startActivityForResult(i, requestCode);
	}

	@Override
	public void onLeaderboardChosen(int leaderboardStringId) {
		final GamesClient gamesClient = getGamesClient();
		if (gamesClient.isConnected()) {
			startActivityForResult(gamesClient.getLeaderboardIntent(
					getResources().getString(leaderboardStringId)), REQUEST_LEADERBOARD);
		} else {
			makeToast(getResources().getString(R.string.home_not_sign_in_leaderboard));
		}
	}

	/**
	 * check if an internet connection is available
	 *
	 * @return
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onGameStartRequest(GameMode gameMode) {
		startNewGame(gameMode, REQUEST_GAME_ACTIVITY_FRESH_START);
	}

	@Override
	public void onPlayRequest(GameMode gameMode) {
		if (gameMode.areBonusAvailable()) {
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					BonusFragment.newInstance(gameMode)).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		} else {
			onGameStartRequest(gameMode);
		}
	}


	public static class SignOutConfirmDialogFragment extends DialogFragment {
		
		public SignOutConfirmDialogFragment() {}

		public static SignOutConfirmDialogFragment newInstance() 
		{
			return new SignOutConfirmDialogFragment();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.app_name)
					.setMessage(R.string.home_sign_out_confirm_dialog_message)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int which) {
							((HomeActivity) getActivity()).signOut();
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int which) {
							dialogInterface.dismiss();
						}
					})
					.create();
		}
	}
}

