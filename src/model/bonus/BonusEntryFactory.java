package model.bonus;

import model.inventory.InventoryItemInformation;

import com.tracchis.saopayne.zills.R;

public class BonusEntryFactory {
	public static BonusEntry create(int inventoryItemType, long quantity) {
		BonusEntry bonusEntry = new BonusEntry(inventoryItemType, quantity);
		Bonus bonus = new Bonus.DummyBonus();
		int effectResourceId = 0;
		switch (inventoryItemType) {
			case InventoryItemInformation.TYPE_STEEL_BULLET:
				bonus = new BonusDamage(inventoryItemType, 1);
				effectResourceId = R.string.bonus_damage_effect;
				break;
			case InventoryItemInformation.TYPE_GOLD_BULLET:
				bonus = new BonusDamage(inventoryItemType, 3);
				effectResourceId = R.string.bonus_damage_effect;
				break;
			case InventoryItemInformation.TYPE_ONE_SHOT_BULLET:
				bonus = new BonusDamage(inventoryItemType, 10);
				effectResourceId = R.string.bonus_damage_effect;
				break;
			case InventoryItemInformation.TYPE_SPEED_POTION:
				bonus = new BonusSpeed(inventoryItemType, 300);
				effectResourceId = R.string.bonus_speed_effect;
				break;
		}

		bonusEntry.setBonus(bonus);
		bonusEntry.setEffectResourceId(effectResourceId);
		return bonusEntry;
	}


}
