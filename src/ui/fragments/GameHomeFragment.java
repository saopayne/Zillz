package ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tracchis.saopayne.zills.R;

public class GameHomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
	public static final String FRAGMENT_TAG = "GameHomeFragment_TAG";
	private static final String STATE_SIGNED_IN = "State_signed";
	private boolean mSignedIn;
	private Listener mListener = null;
	//animation
	AdView adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SIGNED_IN)) {
			mSignedIn = savedInstanceState.getBoolean(STATE_SIGNED_IN);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		
		 adView = (AdView)v.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder()
			.build();
			adView.loadAd(adRequest);
			
			
		final int[] clickable = new int[]{
				R.id.home_play,
				R.id.home_leaderboard,
				R.id.home_achievement,
				R.id.home_about,
				R.id.home_sign_in,
				R.id.home_sign_out,
				R.id.home_help_tuto,
				R.id.home_profile
		};
		for (int i : clickable) {
			final View view = v.findViewById(i);
			view.setOnClickListener(this);
			if (i == R.id.home_about || i == R.id.home_help_tuto) {
				view.findViewById(i).setOnLongClickListener(this);
			}
		}


		notifySignedStateChanged(mSignedIn, true, v);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameHomeFragment.Listener) {
			mListener = (GameHomeFragment.Listener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameHomeFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	
	@Override
	public void onClick(View view) {
		if (mListener == null)
			return;
		
			if (view.getId() == R.id.home_play){
				mListener.onStartGameRequested();
			}
			if (view.getId() == R.id.home_leaderboard){
				mListener.onShowLeaderboardsRequested();
			}
			if (view.getId() == R.id.home_achievement){
				mListener.onShowAchievementsRequested();
			}
			if (view.getId() == R.id.home_about){
				mListener.onShowAboutRequested();
			}
			if (view.getId() == R.id.home_sign_out){
				mListener.onSignOutButtonClicked();
			}
			if (view.getId() == R.id.home_sign_in){
				mListener.onSignInButtonClicked();
			}
			
			if (view.getId() == R.id.home_help_tuto){
				mListener.onHelpRequested();
			}
			if (view.getId() == R.id.home_profile){
				mListener.onShowProfileRequested();
			}
		}
	

	@Override
	public boolean onLongClick(View view) {
		if (mListener == null)
			return false;
		
			if (view.getId() == R.id.home_about){
				mListener.toast(getResources().getString(R.string.home_i_button_on_long_press));
			}
			if (view.getId() == R.id.home_help_tuto){
				mListener.toast(getResources().getString(R.string.home_help_button_on_long_press));
			}
			
		
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(STATE_SIGNED_IN, mSignedIn);
	}

	public void notifySignedStateChanged(boolean signedIn) {
		notifySignedStateChanged(signedIn, false);
	}

	private void notifySignedStateChanged(boolean signedIn, boolean forceRefreshState) {
		notifySignedStateChanged(signedIn, forceRefreshState, null);
	}

	private void notifySignedStateChanged(boolean signedIn, boolean forceRefreshState, View rootView) {
		if (forceRefreshState || signedIn != mSignedIn) {
			final View signIn;
			final View signOut;
			final View signInInfo;
			if (rootView == null) {
				final Activity activity = getActivity();
				signIn = activity.findViewById(R.id.home_sign_in);
				signOut = activity.findViewById(R.id.home_sign_out);
				signInInfo = activity.findViewById(R.id.google_signin_info);
			} else {
				signIn = rootView.findViewById(R.id.home_sign_in);
				signOut = rootView.findViewById(R.id.home_sign_out);
				signInInfo = rootView.findViewById(R.id.google_signin_info);
			}
			if (signedIn) {
				signIn.setVisibility(View.GONE);
				signInInfo.setVisibility(View.GONE);
				signOut.setVisibility(View.VISIBLE);
			} else {
				signOut.setVisibility(View.GONE);
				signIn.findViewById(R.id.home_sign_in).setVisibility(View.VISIBLE);
				signInInfo.setVisibility(View.VISIBLE);
			}
			mSignedIn = signedIn;
		}
	}


	//interface
	public interface Listener {
		public void onStartGameRequested();

		public void onShowAchievementsRequested();

		public void onShowLeaderboardsRequested();

		public void onShowAboutRequested();

		public void onSignInButtonClicked();

		public void onSignOutButtonClicked();

		public void onBatPictureClicked();

		public void onHelpRequested();

		public void onShowProfileRequested();

		public void toast(String message);
	}
}
