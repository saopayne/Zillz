package mechanics.engine;


import mechanics.behaviors.GameBehaviorDeathToTheKing;
import mechanics.behaviors.GameBehaviorFactory;
import mechanics.behaviors.GameBehaviorMemorize;
import mechanics.behaviors.GameBehaviorSurvival;
import mechanics.behaviors.GameBehaviorTime;
import mechanics.behaviors.GameBehaviorTutorial;
import mechanics.behaviors.GameBehaviorTwentyInARow;
import mechanics.informations.GameInformation;
import mechanics.informations.GameInformationDeathToTheKing;
import mechanics.informations.GameInformationMemorize;
import mechanics.informations.GameInformationSurvival;
import mechanics.informations.GameInformationTime;
import mechanics.informations.GameInformationTutorial;
import mechanics.informations.GameInformationTwentyInARow;
import mechanics.routine.Routine;
import mechanics.routine.RoutineTicker;
import model.mode.GameMode;
import model.mode.GameModeDeathToTheKing;
import model.mode.GameModeFactory;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;
import ui.gameviews.GameViewDeathToTheKing;
import ui.gameviews.GameViewMemorize;
import ui.gameviews.GameViewTime;
import ui.gameviews.GameViewTimeDecreasing;
import ui.gameviews.GameViewTutorial;
import ui.gameviews.GameViewTwentyInARow;
import android.content.Context;

public class GameEngineFactory {
	private static final long DEFAULT_SPAWNING_TIME = 1000;
	private static final long TWENTY_IN_A_ROW_SPAWNING_TIME = 800;
	private static final long DEFAULT_TICKING_TIME = 1000;
	private static final long DEFAULT_STARTING_TIME = 30000;

	public static GameEngine create(final Context context, GameEngine.IGameEngine iGameEngine, GameMode gameMode) {
		GameEngine gameEngine = null;
		switch (gameMode.getType()) {
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
				gameEngine = createSprintOrMarathon(context, gameMode, iGameEngine);
				break;

			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				gameEngine = createDeathToTheKing(context, (GameModeDeathToTheKing) gameMode, iGameEngine);
				break;

			case GameModeFactory.GAME_TYPE_SURVIVAL:
				gameEngine = createSurvival(context, gameMode, iGameEngine);
				break;

			case GameModeFactory.GAME_TYPE_TUTORIAL:
				gameEngine = createTutorial(context, gameMode, iGameEngine);
				break;

			case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
				gameEngine = createTwentyInARow(context, gameMode, iGameEngine);
				break;

			case GameModeFactory.GAME_TYPE_MEMORIZE:
				gameEngine = createMemorize(context, gameMode, iGameEngine);
				break;

		}
		return gameEngine;
	}

	public static GameEngine restore(final Context context, GameEngine.IGameEngine iGameEngine, GameInformation gameInformation) {
		GameEngine gameEngine = null;
		final int gameModeType = gameInformation.getGameMode().getType();
		switch (gameModeType) {
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
				gameEngine = createSprintOrMarathon(context, iGameEngine, (GameInformationTime) gameInformation);
				break;

			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				gameEngine = createDeathToTheKing(context, iGameEngine, (GameInformationTime) gameInformation);
				break;

			case GameModeFactory.GAME_TYPE_SURVIVAL:
				gameEngine = createSurvival(context, iGameEngine, (GameInformationTime) gameInformation);
				break;

			case GameModeFactory.GAME_TYPE_TUTORIAL:
				gameEngine = createTutorial(context, iGameEngine, (GameInformationTutorial) gameInformation);
				break;

			case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
				gameEngine = createTwentyInARow(context, iGameEngine, (GameInformationTwentyInARow) gameInformation);
				break;

			case GameModeFactory.GAME_TYPE_MEMORIZE:
				gameEngine = createMemorize(context, iGameEngine, (GameInformationMemorize) gameInformation);
				break;

		}
		return gameEngine;
	}

	private static GameEngine createMemorize(final Context context, final GameMode gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationMemorize gameInformation = new GameInformationMemorize(gameMode, weapon);

		return createMemorize(context, iGameEngine, gameInformation);
	}


	private static GameEngine createMemorize(final Context context,
											 final GameEngine.IGameEngine iGameEngine,
											 GameInformationMemorize gameInformation) {
		//Game Behavior
		final GameBehaviorMemorize gameBehavior = GameBehaviorFactory.createMemorize();
		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineMemorize gameEngine = new GameEngineMemorize(context, iGameEngine, gameBehavior);
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameEngine.addRoutine(new RoutineTicker(2000));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewMemorize gameView = new GameViewMemorize(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}


	private static GameEngine createTwentyInARow(final Context context, final GameMode gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationTwentyInARow gameInformation = new GameInformationTwentyInARow(gameMode,
				weapon, 1);

		return createTwentyInARow(context, iGameEngine, gameInformation);
	}


	private static GameEngine createTwentyInARow(final Context context,
												 final GameEngine.IGameEngine iGameEngine,
												 GameInformationTime gameInformation) {
		//Game Behavior
		final GameBehaviorTwentyInARow gameBehavior = GameBehaviorFactory.createTwentyInARow();
		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineTwentyInARow gameEngine = new GameEngineTwentyInARow(context, iGameEngine, gameBehavior);
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameEngine.addRoutine(new Routine(Routine.TYPE_SPAWNER, TWENTY_IN_A_ROW_SPAWNING_TIME));
		gameEngine.addRoutine(new RoutineTicker(DEFAULT_TICKING_TIME));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewTwentyInARow gameView = new GameViewTwentyInARow(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}

	private static GameEngine createTutorial(final Context context, final GameMode gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationTutorial gameInformation = new GameInformationTutorial(gameMode, weapon);

		return createTutorial(context, iGameEngine, gameInformation);
	}


	private static GameEngine createTutorial(final Context context,
											 final GameEngine.IGameEngine iGameEngine,
											 final GameInformationTutorial gameInformation) {
		//Game Behavior
		final GameBehaviorTutorial gameBehavior = GameBehaviorFactory.createTutorial();
		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineTutorial gameEngine = new GameEngineTutorial(context, iGameEngine, gameBehavior) {
			@Override
			public void onRun(int routineType, Object obj) {
				switch (routineType) {
					case Routine.TYPE_RELOADER:
						mGameBehavior.reload();
						break;
				}
			}
		};
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewTutorial gameView = new GameViewTutorial(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}

	private static GameEngine createDeathToTheKing(final Context context, final GameModeDeathToTheKing gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationDeathToTheKing gameInformation = new GameInformationDeathToTheKing(gameMode,
				weapon, 0);


		return createDeathToTheKing(context, iGameEngine, gameInformation);
	}

	private static GameEngine createDeathToTheKing(final Context context,
												   final GameEngine.IGameEngine iGameEngine,
												   GameInformationTime gameInformation) {
		//Game Behavior
		final GameBehaviorDeathToTheKing gameBehavior = GameBehaviorFactory.createDeathToTheKing();
		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineDeathToTheKing gameEngine = new GameEngineDeathToTheKing(context, iGameEngine, gameBehavior);
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameEngine.addRoutine(new RoutineTicker(DEFAULT_TICKING_TIME));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewDeathToTheKing gameView = new GameViewDeathToTheKing(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}

	private static GameEngine createSurvival(final Context context, final GameMode gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationSurvival gameInformation = new GameInformationSurvival(gameMode,
				weapon, DEFAULT_STARTING_TIME);

		return createSurvival(context, iGameEngine, gameInformation);
	}


	private static GameEngine createSurvival(final Context context,
											 final GameEngine.IGameEngine iGameEngine,
											 GameInformationTime gameInformation) {
		//Game Behavior
		final GameBehaviorSurvival gameBehavior = GameBehaviorFactory.createSurvival();
		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineTime gameEngine = new GameEngineTime(context, iGameEngine, gameBehavior) {
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
		};
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameEngine.addRoutine(new Routine(Routine.TYPE_SPAWNER, DEFAULT_SPAWNING_TIME));
		gameEngine.addRoutine(new RoutineTicker(DEFAULT_TICKING_TIME));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewTime gameView = new GameViewTimeDecreasing(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}

	private static GameEngine createSprintOrMarathon(final Context context, final GameMode gameMode
			, final GameEngine.IGameEngine iGameEngine) {
		//Weapon
		final Weapon weapon = WeaponFactory.createBasicWeapon();

		//Game Information
		final GameInformationTime gameInformation = new GameInformationTime(gameMode,
				weapon, DEFAULT_STARTING_TIME * gameMode.getLevel());

		return createSprintOrMarathon(context, iGameEngine, gameInformation);
	}

	private static GameEngine createSprintOrMarathon(final Context context,
													 final GameEngine.IGameEngine iGameEngine,
													 GameInformationTime gameInformation) {
		//Game Behavior
		GameBehaviorTime gameBehavior = GameBehaviorFactory.createSprint();
		if (gameInformation.getGameMode().getLevel() > 1) {
			gameBehavior = GameBehaviorFactory.createMarathon();
		}

		gameBehavior.setGameInformation(gameInformation);

		//Game Engine & Game Behavior
		final GameEngineTime gameEngine = new GameEngineTime(context, iGameEngine, gameBehavior) {
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
		};
		gameEngine.addRoutine(new Routine(Routine.TYPE_RELOADER, gameInformation.getWeapon().getReloadingTime()));
		gameEngine.addRoutine(new Routine(Routine.TYPE_SPAWNER, DEFAULT_SPAWNING_TIME));
		gameEngine.addRoutine(new RoutineTicker(DEFAULT_TICKING_TIME));
		gameBehavior.setInterface(gameEngine);

		//Game View
		final GameViewTime gameView = new GameViewTimeDecreasing(context, gameEngine);
		gameEngine.setGameView(gameView);

		return gameEngine;
	}
}
