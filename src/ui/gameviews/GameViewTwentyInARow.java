package ui.gameviews;


import mechanics.engine.GameEngineTime;
import mechanics.engine.GameEngineTwentyInARow;
import android.content.Context;
import android.graphics.Canvas;

public class GameViewTwentyInARow extends GameViewTime {

	public GameViewTwentyInARow(Context c, GameEngineTime gameEngine) {
		super(c, gameEngine);
	}

	@Override
	public void onDrawing(Canvas c) {
		super.onDrawing(c);
		drawCurrentStack(c);
	}

	protected void drawCurrentStack(Canvas canvas) {
		final int currentStack = ((GameEngineTwentyInARow)mGameEngine).getCurrentStack();
		final String currentStackStr = String.valueOf(currentStack);
		resetPainter();
		useGreenPainter();
		mPaint.getTextBounds(currentStackStr, 0, currentStackStr.length(), mBounds);
		mPaint.getTextBounds(currentStackStr, 0, currentStackStr.length(), mBounds);
		canvas.drawText(currentStackStr
				, mBounds.width() / 2 + mPadding
				, mScreenHeight - mPaint.getTextSize() * 2 - mPadding
				, mPaint);

	}

}
