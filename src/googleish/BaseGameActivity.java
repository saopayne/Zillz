package googleish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;


/**
 * Example base class for games. This implementation takes care of setting up
 * the GamesClient object and managing its lifecycle. Subclasses only need to
 * override the @link{#onSignInSucceeded} and @link{#onSignInFailed} abstract
 * methods. To initiate the sign-in flow when the user clicks the sign-in
 * button, subclasses should call @link{#beginUserInitiatedSignIn}. By default,
 * this class only instantiates the GamesClient object. If the PlusClient or
 * AppStateClient objects are also wanted, call the BaseGameActivity(int)
 * constructor and specify the requested clients. For example, to request
 * PlusClient and GamesClient, use BaseGameActivity(CLIENT_GAMES | CLIENT_PLUS).
 * To request all available clients, use BaseGameActivity(CLIENT_ALL).
 * Alternatively, you can also specify the requested clients via
 *
 * @author Bruno Oliveira (Google)
 * @link{#setRequestedClients}, but you must do so before @link{#onCreate}
 * gets called, otherwise the call will have no effect.
 */
public abstract class BaseGameActivity extends FragmentActivity  implements GameHelper.GameHelperListener{

	
	// The game helper object. This class is mainly a wrapper around this object.
		protected GameHelper mHelper;
		
		protected GamesClient mGamesClient;

		// We expose these constants here because we don't want users of this class
		// to have to know about GameHelper at all.
		public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
		public static final int CLIENT_APPSTATE = GameHelper.CLIENT_APPSTATE;
		public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
		public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

		// Requested clients. By default, that's just the games client.
		protected int mRequestedClients = CLIENT_GAMES;

		/**
		 * Constructs a BaseGameActivity with default client (GamesClient).
		 */
		protected BaseGameActivity() {
			super();
			mHelper = new GameHelper(this);
		}

		/**
		 * Constructs a BaseGameActivity with the requested clients.
		 *
		 * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
		 *                         CLIENT_PLUS and CLIENT_APPSTATE).
		 */
		protected BaseGameActivity(int requestedClients) {
			super();
			setRequestedClients(requestedClients);
		}

		/**
		 * Sets the requested clients. The preferred way to set the requested clients is
		 * via the constructor, but this method is available if for some reason your code
		 * cannot do this in the constructor. This must be called before onCreate in order to
		 * have any effect. If called after onCreate, this method is a no-op.
		 *
		 * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
		 *                         and CLIENT_APPSTATE, or CLIENT_ALL to request all available clients.
		 */
		protected void setRequestedClients(int requestedClients) {
			mRequestedClients = requestedClients;
		}

		@Override
		protected void onCreate(Bundle b) {
			super.onCreate(b);
			mHelper = new GameHelper(this);
			mHelper.setup(this, mRequestedClients);
			mGamesClient = mHelper.getGamesClient();
		}

		@Override
		protected void onStart() {
			super.onStart();
			mHelper.onStart(this);
		}

		@Override
		protected void onStop() {
			super.onStop();
			mHelper.onStop();
		}

		@Override
		protected void onActivityResult(int request, int response, Intent data) {
			super.onActivityResult(request, response, data);
			mHelper.onActivityResult(request, response, data);
		}

		protected GamesClient getGamesClient() {
			return mHelper.getGamesClient();
		}

		protected AppStateClient getAppStateClient() {
			return mHelper.getAppStateClient();
		}

		protected PlusClient getPlusClient() {
			return mHelper.getPlusClient();
		}

		protected boolean isSignedIn() {
			return mHelper.isSignedIn();
		}

		protected void beginUserInitiatedSignIn() {
			mHelper.beginUserInitiatedSignIn();
		}

		protected void signOut() {
			mHelper.signOut();
		}

		protected void showAlert(String title, String message) {
			mHelper.showAlert(title, message);
		}

		protected void showAlert(String message) {
			mHelper.showAlert(message);
		}

		protected void enableDebugLog(boolean enabled, String tag) {
			mHelper.enableDebugLog(enabled, tag);
		}

		protected String getInvitationId() {
			return mHelper.getInvitationId();
		}

		protected void reconnectClients(int whichClients) {
			mHelper.reconnectClients(whichClients);
		}

		protected String getScopes() {
			return mHelper.getScopes();
		}

		protected boolean hasSignInError() {
			return mHelper.hasSignInError();
		}

		protected ConnectionResult getSignInError() {
			return mHelper.getSignInError();
		}

		protected void setSignInMessages(String signingInMessage, String signingOutMessage) {
			mHelper.setSigningInMessage(signingInMessage);
			mHelper.setSigningOutMessage(signingOutMessage);
		}


}
