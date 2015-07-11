package mechanics.engine;

import ui.gameviews.GameView;
import ui.gameviews.GameViewDeathToTheKing;
import mechanics.behaviors.GameBehaviorDeathToTheKing;
import mechanics.behaviors.GameBehaviorTime;
import mechanics.routine.Routine;
import android.content.Context;

public class GameEngineDeathToTheKing extends GameEngineTime implements GameBehaviorDeathToTheKing.IGameBehaviorDeathToTheKing {
	private GameBehaviorDeathToTheKing mGameBehavior;
	private GameViewDeathToTheKing mGameView;

	public GameEngineDeathToTheKing(Context context, IGameEngine iGameEngine, GameBehaviorTime gameBehavior) {
		super(context, iGameEngine, gameBehavior);
		mGameBehavior = (GameBehaviorDeathToTheKing) gameBehavior;
	}

	@Override
	protected void setGameView(GameView gameView) {
		super.setGameView(gameView);
		mGameView = (GameViewDeathToTheKing) gameView;
	}

	@Override
	public void onRun(int routineType, Object obj) {
		switch (routineType) {
			case Routine.TYPE_RELOADER:
				mGameBehavior.reload();
				break;
			case Routine.TYPE_TICKER:
				mGameBehavior.tick((Long) obj);
				break;

		}
	}

	public boolean hasTheKingAlreadyBeenSummoned() {
		return mGameBehavior.hasKingAlreadyBeenSummoned();
	}

	@Override
	public void onKingSummon() {
		mGameView.hideInstruction();
	}
}
