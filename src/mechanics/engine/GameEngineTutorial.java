package mechanics.engine;


import mechanics.behaviors.GameBehaviorTutorial;
import ui.gameviews.GameViewTutorial;
import android.content.Context;

public abstract class GameEngineTutorial extends GameEngineStandard implements GameBehaviorTutorial.IGameBehaviorTutorial {
	protected GameBehaviorTutorial mGameBehavior;

	public GameEngineTutorial(Context context, IGameEngine iGameEngine, GameBehaviorTutorial gameBehavior) {
		super(context, iGameEngine, gameBehavior);
		mGameBehavior = gameBehavior;
	}

	public int getCurrentStep() {
		return mGameBehavior.getCurrentStep();
	}

	@Override
	public void onNextStep() {
		((GameViewTutorial) mGameView).updateStepMessage();
	}
}
