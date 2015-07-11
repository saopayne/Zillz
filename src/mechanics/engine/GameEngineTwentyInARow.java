package mechanics.engine;

import mechanics.behaviors.GameBehaviorTime;
import mechanics.behaviors.GameBehaviorTwentyInARow;
import mechanics.routine.Routine;
import android.content.Context;

public class GameEngineTwentyInARow extends GameEngineTime {

	public GameEngineTwentyInARow(Context context, IGameEngine iGameEngine, GameBehaviorTime gameBehavior) {
		super(context, iGameEngine, gameBehavior);
	}

	@Override
	public void onRun(int routineType, Object obj) {
		switch (routineType) {
			case Routine.TYPE_RELOADER:
				mGameBehavior.reload();
				break;
			case Routine.TYPE_SPAWNER:
				final float[] cameraAngle = mGameView.getCameraAngleInDegree();
				mGameBehavior.spawn((int) cameraAngle[0], (int) cameraAngle[1]);
				break;
			case Routine.TYPE_TICKER:
				mGameBehavior.tick((Long) obj);
				break;

		}
	}

	public int getCurrentStack() {
		return ((GameBehaviorTwentyInARow) mGameBehavior).getCurrentStack();
	}
}
