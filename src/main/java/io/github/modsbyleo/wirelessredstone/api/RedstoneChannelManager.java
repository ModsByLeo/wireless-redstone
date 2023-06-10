package io.github.modsbyleo.wirelessredstone.api;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashBigSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Manages {@link RedstoneChannel}s.
 */
public final class RedstoneChannelManager {
	private RedstoneChannelManager() {
		throw new UnsupportedOperationException("RedstoneChannelManager only contains static declarations.");
	}

	private static final @NotNull Set<RedstoneChannel> CHANNELS =
		new ReferenceOpenHashBigSet<>();
	private static final @NotNull Multimap<RedstoneChannel, RedstoneChannelTransmitter> TRANSMITTERS =
		Multimaps.newSetMultimap(new Object2ReferenceLinkedOpenHashMap<>(), ReferenceOpenHashBigSet::new);
	private static final @NotNull Multimap<RedstoneChannel, RedstoneChannelReceiver> RECEIVERS =
		Multimaps.newSetMultimap(new Object2ReferenceLinkedOpenHashMap<>(), ReferenceOpenHashBigSet::new);

	public static @NotNull @UnmodifiableView Set<RedstoneChannel> getChannels() {
		return Collections.unmodifiableSet(CHANNELS);
	}

	public static boolean registerChannel(@NotNull RedstoneChannel channel) {
		return CHANNELS.add(channel);
	}

	public static boolean unregisterChannel(@NotNull RedstoneChannel channel) {
		return CHANNELS.remove(channel);
	}

	public static boolean isChannelPowered(@NotNull RedstoneChannel channel) {
		return !TRANSMITTERS.get(channel).isEmpty();
	}

	public static boolean registerTransmitter(@NotNull RedstoneChannel channel, @NotNull RedstoneChannelTransmitter transmitter) {
		if (TRANSMITTERS.put(channel, transmitter)) {
			updateReceivers(channel);
			return true;
		} else {
			return false;
		}
	}

	public static boolean unregisterTransmitter(@NotNull RedstoneChannel channel, @NotNull RedstoneChannelTransmitter transmitter) {
		var channelTrans = TRANSMITTERS.get(channel);
		boolean retVal = channelTrans.remove(transmitter);
		if (channelTrans.isEmpty()) {
			updateReceivers(channel);
		}
		return retVal;
	}

	public static boolean registerReceiver(@NotNull RedstoneChannel channel, @NotNull RedstoneChannelReceiver receiver) {
		return RECEIVERS.put(channel, receiver);
	}

	private static void updateReceivers(@NotNull RedstoneChannel channel) {
		RECEIVERS.get(channel).removeIf(receiver -> !receiver.onChannelPowered(channel));
	}
}
