package beta;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.tracchis.saopayne.zills.R;

public class SensorDelayDialogFragment extends DialogFragment{
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("Delay Sensor");
		
		final ArrayList<String> sensorDelayString = new ArrayList<String>();
		sensorDelayString.add("Delay Fastest");
		sensorDelayString.add("Delay Game");
		sensorDelayString.add("Delay UI");
		sensorDelayString.add("Delay Normal");
		
		final ArrayList<Integer> sensorDelayInteger = new ArrayList<Integer>();
		sensorDelayInteger.add(SensorManager.SENSOR_DELAY_FASTEST);
		sensorDelayInteger.add(SensorManager.SENSOR_DELAY_GAME);
		sensorDelayInteger.add(SensorManager.SENSOR_DELAY_UI);
		sensorDelayInteger.add(SensorManager.SENSOR_DELAY_NORMAL);
		
		final SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences(BetaUtils.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
		
		final SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
		
		int currentSensorDelayIndex = sensorDelayInteger.indexOf(sharedPreferences
				.getInt(BetaUtils.KEY_SENSOR_DELAY, SensorManager.SENSOR_DELAY_GAME));
		
		builder.setSingleChoiceItems(sensorDelayString.toArray(new String []{}),currentSensorDelayIndex
				,new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sharedPreferencesEditor.putInt(BetaUtils.KEY_SENSOR_DELAY, sensorDelayInteger.get(which));
					}
				});
		builder.setPositiveButton(R.string.craft_dialog_fragment_ok_response, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sharedPreferencesEditor.commit();
			}
		});
		return builder.create();
	}

}
