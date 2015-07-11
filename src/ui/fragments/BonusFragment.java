package ui.fragments;

import model.PlayerProfile;
import model.bonus.Bonus;
import model.bonus.BonusEntry;
import model.bonus.BonusEntryFactory;
import model.inventory.InventoryItemInformation;
import model.mode.GameMode;
import ui.adapter.BonusEntryAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.tracchis.saopayne.zills.R;

public class BonusFragment extends Fragment {
	public static final String EXTRA_GAME_MODE = "BonusFragment.Extra.GameMode";
	private Listener mListener;
	private GridView mBonusGridView;
	private BonusEntryAdapter mBonusEntryAdapter;
	private PlayerProfile mPlayerProfile;
	private GameMode mGameMode;

	public static BonusFragment newInstance(GameMode gameMode) {
		final BonusFragment f = new BonusFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(EXTRA_GAME_MODE, gameMode);
		f.setArguments(arguments);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments.containsKey(EXTRA_GAME_MODE)) {
			mGameMode = (GameMode) getArguments().get(EXTRA_GAME_MODE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//TODO beurk, to rework
		mBonusEntryAdapter = new BonusEntryAdapter(getActivity(), new BonusEntry[]{
				BonusEntryFactory.create(InventoryItemInformation.TYPE_STEEL_BULLET, mPlayerProfile.getSteelBulletQuantity()),
				BonusEntryFactory.create(InventoryItemInformation.TYPE_GOLD_BULLET, mPlayerProfile.getGoldBulletQuantity()),
				BonusEntryFactory.create(InventoryItemInformation.TYPE_ONE_SHOT_BULLET, mPlayerProfile.getOneShotBulletQuantity()),
				BonusEntryFactory.create(InventoryItemInformation.TYPE_SPEED_POTION, mPlayerProfile.getSpeedPotionQuantity()),
		});
		mBonusGridView.setAdapter(mBonusEntryAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bonus, container, false);
		mBonusGridView = ((GridView) v.findViewById(R.id.bonus_grid_view));

		(v.findViewById(R.id.bonus_start)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Bonus equippedBonus = mBonusEntryAdapter.getEquippedBonus();
				mGameMode.setBonus(equippedBonus);
				mListener.onGameStartRequest(mGameMode);
			}
		});

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BonusFragment.Listener) {
			mListener = (BonusFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet BonusFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface Listener {
		public void onGameStartRequest(GameMode gameMode);
	}

}
