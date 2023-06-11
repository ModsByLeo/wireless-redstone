package io.github.modsbyleo.wirelessredstone.api;

import org.jetbrains.annotations.NotNull;

/**
 * Powers {@link RedstoneChannelKey}s, functioning as the "input" of channels.
 * @apiNote To simplify the API surface and to improve performance, these are considered to be always powered.
 *          You must unregister a transmitter if it becomes unpowered.
 */
public interface RedstoneChannelTransmitter {
	/**
	 * {@return a human-readable description of the transmitter}
	 */
	@NotNull String getDescription();
}
