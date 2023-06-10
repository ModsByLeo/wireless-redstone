package io.github.modsbyleo.wirelessredstone.api;

import org.jetbrains.annotations.NotNull;

/**
 * Listens for {@link RedstoneChannel}s being powered, functioning as the "output" of channels.
 */
@FunctionalInterface
public interface RedstoneChannelReceiver {
	/**
	 * Called when a {@link RedstoneChannel} becomes powered.
	 * @param channel the channel that has become powered
	 * @return {@code false} to unregister this receiver, {@code true} otherwise
	 */
	boolean onChannelPowered(@NotNull RedstoneChannel channel);
}
