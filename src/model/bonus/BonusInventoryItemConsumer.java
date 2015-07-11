package model.bonus;

import model.PlayerProfile;


public interface BonusInventoryItemConsumer {
	public Bonus consume(PlayerProfile playerProfile);
}
