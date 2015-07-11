package ui.dialogfragments;

import model.inventory.InventoryItemEntry;
import ui.customviews.InventoryItemEntryDetailView;
import ui.gameviews.InventoryCraftListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import com.tracchis.saopayne.zills.R;

public class InventoryItemEntryDetailDialogFragment extends DialogFragment implements InventoryCraftListener {

	public static final String TAG = "InventoryItemEntryDetailDialogFragment.TAG";
	private static final String EXTRA_INVENTORY_ITEM_ENTRY = "InventoryItemEntryDetailDialogFragment.Extra.InventoryItemEntry";
	private InventoryItemEntry mInventoryItemEntry;
	private InventoryCraftListener mListener;
	private InventoryItemEntryDetailView mInventoryItemEntryViewDetailView;

	public static InventoryItemEntryDetailDialogFragment newInstance(InventoryItemEntry inventoryItemEntry) {
		InventoryItemEntryDetailDialogFragment fragment = new InventoryItemEntryDetailDialogFragment();
		Bundle arguments = new Bundle();
		arguments.putParcelable(EXTRA_INVENTORY_ITEM_ENTRY, inventoryItemEntry);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof InventoryCraftListener) {
			mListener = (InventoryCraftListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement InventoryCraftListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mInventoryItemEntry = getArguments().getParcelable(EXTRA_INVENTORY_ITEM_ENTRY);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		mInventoryItemEntryViewDetailView = new InventoryItemEntryDetailView(getActivity());
		mInventoryItemEntryViewDetailView.setModel(mInventoryItemEntry);
		mInventoryItemEntryViewDetailView.setCraftRequestListener(this);
		builder.setView(mInventoryItemEntryViewDetailView);

		builder.setCancelable(true);
		builder.setPositiveButton(R.string.craft_dialog_fragment_ok_response, null);

		AlertDialog alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
				positiveButton.setBackgroundResource(R.drawable.button_dialog);
			}
		});
		return alertDialog;
	}

	public void udpateInventoryItemEntry(InventoryItemEntry inventoryItemEntry) {
		mInventoryItemEntry = inventoryItemEntry;
		mInventoryItemEntryViewDetailView.setModel(mInventoryItemEntry);
		mInventoryItemEntryViewDetailView.invalidate();
	}

	@Override
	public void onCraftRequested(InventoryItemEntry inventoryItemEntry) {
		mListener.onCraftRequested(inventoryItemEntry);
	}
}
