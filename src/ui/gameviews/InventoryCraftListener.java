package ui.gameviews;

import model.inventory.InventoryItemEntry;


public interface InventoryCraftListener {
	public void onCraftRequested(InventoryItemEntry inventoryItemEntry);
}
