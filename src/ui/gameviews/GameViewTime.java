package ui.gameviews;


import mechanics.engine.GameEngineTime;
import android.content.Context;
import android.graphics.Canvas;

public class GameViewTime extends GameViewStandard {
	protected GameEngineTime mGameEngine;

	public GameViewTime(Context c, GameEngineTime gameEngine) {
		super(c, gameEngine);
		mGameEngine = gameEngine;
	}

	@Override
	public void onDrawing(Canvas c) {
		super.onDrawing(c);
		drawTimer(c);
	}

	/**
	 * draw time, in red if time < 10 sec else in green
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	protected void drawTimer(Canvas canvas) {
		final long millis = mGameEngine.getCurrentTime();
		final int seconds = (int) (millis / 1000);
		final String remainingTime = String.format(mTimeString, seconds);
		resetPainter();
		useGreenPainter();
		mPaint.getTextBounds(remainingTime, 0, remainingTime.length(), mBounds);
		canvas.drawText(remainingTime
				, mPadding + mBounds.width() / 2
				, mPadding + mPaint.getTextSize()
				, mPaint);

	}

}
