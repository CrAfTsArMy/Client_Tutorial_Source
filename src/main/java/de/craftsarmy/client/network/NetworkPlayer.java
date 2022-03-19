package de.craftsarmy.client.network;


import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.concurrent.ConcurrentLinkedQueue;

public record NetworkPlayer(PlayerInfo playerInfo, ConcurrentLinkedQueue<String> data, boolean isOnClient) {
}
