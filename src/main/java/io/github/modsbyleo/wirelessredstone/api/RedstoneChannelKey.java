package io.github.modsbyleo.wirelessredstone.api;

import java.util.Objects;
import java.util.UUID;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.UuidUtil;

/**
 * Identifies a wireless Redstone channel, which supports both {@linkplain RedstoneChannelTransmitter transmitters} and
 * {@linkplain RedstoneChannelReceiver receivers}.
 *
 * @see RedstoneChannelTransmitter
 * @see RedstoneChannelReceiver
 */
public final class RedstoneChannelKey {
	private final @NotNull Type type;
	private final @NotNull UUID ownerUUID;
	private final @NotNull String name;

	private RedstoneChannelKey(@NotNull Type type, @NotNull UUID ownerUUID, @NotNull String name) {
		this.type = type;
		this.ownerUUID = ownerUUID;
		this.name = name;
	}

	/**
	 * Represents the type of Redstone channel.
	 */
	public enum Type implements StringIdentifiable {
		/**
		 * Represents a <em>global</em> channel, usable by all players.
		 */
		GLOBAL("global"),
		/**
		 * Represents a <em>local</em> channel, usable only by the owner.
		 */
		LOCAL("local");

		private final @NotNull String name;

		Type(@NotNull String name) {
			this.name = name;
		}

		@Override
		public @NotNull String asString() {
			return name;
		}

		public static final Codec<RedstoneChannelKey.Type> CODEC = StringIdentifiable.createCodec(RedstoneChannelKey.Type::values);
	}

	private static final Interner<RedstoneChannelKey> INTERNER = Interners.newWeakInterner();

	/**
	 * Creates a new global Redstone channel.
	 *
	 * @param name the name of the channel
	 * @return the newly created channel
	 */
	public static @NotNull RedstoneChannelKey ofGlobal(@NotNull String name) {
		return INTERNER.intern(new RedstoneChannelKey(Type.GLOBAL, Util.NIL_UUID, name));
	}

	/**
	 * Creates a new local Redstone channel.
	 *
	 * @param ownerUUID the UUID of the player that owns the channel
	 * @param name      the name of the channel
	 * @return the newly created channel
	 */
	public static @NotNull RedstoneChannelKey ofLocal(@NotNull UUID ownerUUID, @NotNull String name) {
		return INTERNER.intern(new RedstoneChannelKey(Type.LOCAL, ownerUUID, name));
	}

	private static final Codec<RedstoneChannelKey> GLOBAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("name").forGetter(RedstoneChannelKey::name)
	).apply(instance, RedstoneChannelKey::ofGlobal));

	private static final Codec<RedstoneChannelKey> LOCAL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
		UuidUtil.STRING_CODEC.fieldOf("owner").forGetter(RedstoneChannelKey::ownerUUID),
		Codec.STRING.fieldOf("name").forGetter(RedstoneChannelKey::name)
	).apply(instance, RedstoneChannelKey::ofLocal));

	public static final Codec<RedstoneChannelKey> CODEC = Type.CODEC.dispatch(RedstoneChannelKey::type, type -> {
		if (type == Type.GLOBAL) {
			return GLOBAL_CODEC;
		} else {
			return LOCAL_CODEC;
		}
	});

	/**
	 * {@return the type of the channel}
	 */
	public @NotNull Type type() {
		return type;
	}

	/**
	 * {@return the UUID of the player that owns the channel}
	 * @throws IllegalStateException if this is a {@link Type#GLOBAL GLOBAL} channel.
	 */
	public @NotNull UUID ownerUUID() {
		if (type == Type.GLOBAL) {
			throw new IllegalStateException("This channel has no owner");
		}

		return ownerUUID;
	}

	/**
	 * {@return the name of the channel}
	 */
	public @NotNull String name() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		var that = (RedstoneChannelKey) obj;
		return Objects.equals(this.type, that.type) &&
			Objects.equals(this.ownerUUID, that.ownerUUID) &&
			Objects.equals(this.name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, ownerUUID, name);
	}

	@Override
	public String toString() {
		if (type == Type.GLOBAL) {
			return "RedstoneChannelKey.Global[name=" + name + "]";
		} else {
			return "RedstoneChannelKey.Local[ownerUUID=" + ownerUUID + ", name=" + name + "]";
		}
	}

}
