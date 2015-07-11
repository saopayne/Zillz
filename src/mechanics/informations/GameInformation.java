package mechanics.informations;

import model.PlayerProfile;
import model.bonus.Bonus;
import model.bonus.BonusInventoryItemConsumer;
import model.mode.GameMode;
import android.os.Parcel;
import android.os.Parcelable;

abstract public class GameInformation implements Parcelable {
	protected float mCurrentX;
	protected float mCurrentY;
	protected GameMode mGameMode;

	protected GameInformation(GameMode gameMode) {
		mGameMode = gameMode;
	}

	protected GameInformation(Parcel in) {
		readFromParcel(in);
	}

	protected void readFromParcel(Parcel in) {
		mCurrentX = in.readFloat();
		mCurrentY = in.readFloat();
		mGameMode = in.readParcelable(GameMode.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeFloat(mCurrentX);
		out.writeFloat(mCurrentY);
		out.writeParcelable(mGameMode, i);
	}

	public void setCurrentPosition(float x, float y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	public float[] getCurrentPosition() {
		return new float[]{mCurrentX, mCurrentY};
	}

	public GameMode getGameMode() {
		return mGameMode;
	}

	public Bonus getBonus() {
		return mGameMode.getBonus();
	}

	public void useBonus(PlayerProfile playerProfile) {
		final Bonus currentBonus = mGameMode.getBonus();
		if (currentBonus instanceof BonusInventoryItemConsumer) {
			mGameMode.setBonus(((BonusInventoryItemConsumer) currentBonus).consume(playerProfile));
		}
	}

	/**
	 * retrieve grade for the current informations
	 *
	 * @return
	 */
	public int getRank() {
		return mGameMode.getRank(this);
	}

}