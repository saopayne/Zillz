package mechanics.behaviors;

import mechanics.informations.GameInformation;
import mechanics.informations.GameInformationDeathToTheKing;
import model.DisplayableItemFactory;
import model.TargetableItem;

public class GameBehaviorDeathToTheKing extends GameBehaviorTimeIncreasing {
	private GameInformationDeathToTheKing mGameInformation;
	private IGameBehaviorDeathToTheKing mIGameBehavior;

	public boolean hasKingAlreadyBeenSummoned() {
		return mGameInformation.isKingSummoned();
	}

	@Override
	public void setInterface(IGameBehavior iGameBehavior) {
		super.setInterface(iGameBehavior);
		mIGameBehavior = (IGameBehaviorDeathToTheKing) iGameBehavior;
	}

	@Override
	public void setGameInformation(GameInformation gameInformation) {
		super.setGameInformation(gameInformation);
		mGameInformation = (GameInformationDeathToTheKing) gameInformation;
	}

	@Override
	public void spawn(int xRange, int yRange) {
	}

	@Override
	public void onClick() {
		super.onClick();
		if (mGameInformation.isKingSummoned()) {
			fire();
		} else {
			mGameInformation.summonKing();
			mIGameBehavior.onKingSummon();
		}
	}


	@Override
	protected void killTarget(TargetableItem currentTarget) {
		super.killTarget(currentTarget);
		if (currentTarget.getType() == DisplayableItemFactory.TYPE_KING_GHOST) {
			mIGameBehavior.stop();
		}
	}

	public interface IGameBehaviorDeathToTheKing extends IGameBehavior {
		public void onKingSummon();
	}
}
