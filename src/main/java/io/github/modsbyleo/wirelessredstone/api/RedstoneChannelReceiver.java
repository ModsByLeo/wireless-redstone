package io.github.modsbyleo.wirelessredstone.api;

import org.jetbrains.annotations.NotNull;

/**
 * Listens for {@link RedstoneChannelKey}s being powered, functioning as the "output" of channels.
 */
@FunctionalInterface
public interface RedstoneChannelReceiver {
	/**
	 * Called when a {@link RedstoneChannelKey} becomes powered.
	 * @param channel the channel that has become powered
	 * @return {@code false} to unregister this receiver, {@code true} otherwise
	 */
	boolean onChannelPowered(@NotNull RedstoneChannelKey channel);
}
