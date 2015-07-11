package mechanics.informations;


import model.DisplayableItemFactory;
import model.TargetableItem;
import model.mode.GameModeDeathToTheKing;
import model.weapon.Weapon;
import android.os.Parcel;
import android.os.Parcelable;

public class GameInformationDeathToTheKing extends GameInformationTime {
	private boolean mIsKingSummoned;

	public GameInformationDeathToTheKing(GameModeDeathToTheKing gameMode, Weapon weapon, long currentTime) {
		super(gameMode, weapon, currentTime);
		mIsKingSummoned = false;
	}

	public GameInformationDeathToTheKing(Parcel in) {
		super(in);
	}

	public boolean isKingSummoned() {
		return mIsKingSummoned;
	}

	public void summonKing() {
		if (mIsKingSummoned) return;
		for (int i = 0; i < 100; i++) {
			if (i != 50) {
				addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
						TargetableItem.randomGhostTypeWithoutKing()));
			} else {
				addTargetableItem(DisplayableItemFactory.createKingGhostForDeathToTheKing());
			}

		}
		mCurrentTime = 0;
		mStartingTimeInMillis = System.currentTimeMillis();
		mIsKingSummoned = true;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		super.writeToParcel(out, i);
		out.writeByte((byte) (mIsKingSummoned ? 1 : 0));
	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mIsKingSummoned = in.readByte() == 1;
	}

	public static final Parcelable.Creator<GameInformationDeathToTheKing> CREATOR = new Parcelable.Creator<GameInformationDeathToTheKing>() {
		public GameInformationDeathToTheKing createFromParcel(Parcel in) {
			return new GameInformationDeathToTheKing(in);
		}

		public GameInformationDeathToTheKing[] newArray(int size) {
			return new GameInformationDeathToTheKing[size];
		}
	};

}
