package io.github.modsbyleo.wirelessredstone.api;

import org.jetbrains.annotations.NotNull;

/**
 * Listens for wireless Redstone channels being powered, functioning as the "output" of channels.
 */
@FunctionalInterface
public interface RedstoneChannelReceiver {
	/**
	 * Called when a wireless Redstone channel has its state changed.
	 *
	 * @param key     the key of the channel that has had its state changed
	 * @param powered the channel's new state
	 * @return {@code false} to unregister this receiver, {@code true} otherwise
	 */
	boolean onChannelStateChanged(@NotNull RedstoneChannelKey key, boolean powered);
}
