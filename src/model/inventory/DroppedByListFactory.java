package model.inventory;

import com.tracchis.saopayne.zills.R;

public class DroppedByListFactory {
	public static DroppedByList create(int inventoryItemType) {
		DroppedByList droppedByList = new DroppedByList();
		switch (inventoryItemType) {
			case InventoryItemInformation.TYPE_BABY_DROOL:
				droppedByList.addMonster(R.string.bestiary_baby_bat_title, DroppedByList.DROP_RATE_BABY_DROOL);
				break;

			case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
				droppedByList.addMonster(R.string.bestiary_bat_with_helmet_title, DroppedByList.DROP_RATE_BROKEN_HELMET_HORN);
				break;

			case InventoryItemInformation.TYPE_COIN:
				droppedByList.addMonster(R.string.bestiary_easy_bat_title, DroppedByList.DROP_RATE_COIN);
				droppedByList.addMonster(R.string.bestiary_hidden_bat_title, DroppedByList.DROP_RATE_COIN);
				droppedByList.addMonster(R.string.bestiary_baby_bat_title, DroppedByList.DROP_RATE_COIN * 2);
				droppedByList.addMonster(R.string.bestiary_bat_with_helmet_title, DroppedByList.DROP_RATE_COIN * 4);
				droppedByList.addMonster(R.string.bestiary_king_bat_title, DroppedByList.DROP_RATE_COIN * 10);
				break;

			case InventoryItemInformation.TYPE_KING_CROWN:
				droppedByList.addMonster(R.string.bestiary_king_bat_title, DroppedByList.DROP_RATE_KING_CROWN);
				break;

			case InventoryItemInformation.TYPE_BAT_TEAR:
				droppedByList.addMonster(R.string.bestiary_blond_bat_title, DroppedByList.DROP_RATE_GHOST_TEAR);
				break;
		}
		return droppedByList;
	}
}
