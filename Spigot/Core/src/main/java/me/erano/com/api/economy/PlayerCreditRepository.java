package me.erano.com.api.economy;

import java.util.UUID;

public interface PlayerCreditRepository {

    int getCredit(UUID player);

    boolean sendCredit(UUID sender, UUID receiver);

    boolean incrementCredit(UUID player, int amount);

    boolean decreaseCredit(UUID player, int amount);
}
