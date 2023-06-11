package io.github.modsbyleo.wirelessredstone.api;

import java.util.UUID;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Util;

/**
 * Identifies a wireless Redstone channel, which supports both {@linkplain RedstoneChannelTransmitter transmitters} and {@linkplain RedstoneChannelReceiver receivers}.
 * @param type      the type of the channel
 * @param ownerUUID the UUID of the player that owns the channel (only applicable to {@link Type#LOCAL LOCAL} channels)
 * @param name      the name of the channel
 * @see RedstoneChannelTransmitter
 * @see RedstoneChannelReceiver
 */
public record RedstoneChannelKey(@NotNull Type type, @NotNull UUID ownerUUID, @NotNull String name) {
	/**
	 * Represents the type of Redstone channel.
	 */
	public enum Type {
		/**
		 * Represents a <em>global</em> channel, usable by all players.
		 */
		GLOBAL,
		/**
		 * Represents a <em>local</em> channel, usable only by the owner.
		 */
		LOCAL
	}

	/**
	 * Creates a new global Redstone channel.
	 * @param name the name of the channel
	 * @return the newly created channel
	 */
	@Contract(value = "_ -> new", pure = true)
	public static @NotNull RedstoneChannelKey ofGlobal(@NotNull String name) {
		return new RedstoneChannelKey(Type.GLOBAL, Util.NIL_UUID, name);
	}

	/**
	 * Creates a new local Redstone channel.
	 * @param ownerUUID the UUID of the player that owns the channel
	 * @param name      the name of the channel
	 * @return the newly created channel
	 */
	@Contract(value = "_, _ -> new", pure = true)
	public static @NotNull RedstoneChannelKey ofLocal(@NotNull UUID ownerUUID, @NotNull String name) {
		return new RedstoneChannelKey(Type.LOCAL, ownerUUID, name);
	}
}
