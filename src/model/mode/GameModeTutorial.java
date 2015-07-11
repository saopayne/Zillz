package model.mode;


import mechanics.informations.GameInformation;
import model.PlayerProfile;
import android.os.Parcel;
import android.os.Parcelable;

public class GameModeTutorial extends GameMode {

	public GameModeTutorial() {
		super();
	}

	protected GameModeTutorial(Parcel in) {
		super(in);
	}

	@Override
	public boolean isAvailable(PlayerProfile p) {
		//always available
		return true;
	}

	@Override
	public int getRank(GameInformation gameInformation) {
		//always get the rank admiral for tutorial
		return GameModeFactory.GAME_RANK_ADMIRAL;
	}

	public static final Parcelable.Creator<GameModeTutorial> CREATOR = new Parcelable.Creator<GameModeTutorial>() {
		public GameModeTutorial createFromParcel(Parcel in) {
			return new GameModeTutorial(in);
		}

		public GameModeTutorial[] newArray(int size) {
			return new GameModeTutorial[size];
		}
	};

}
