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
 * Manages {@link RedstoneChannelKey}s.
 */
public final class RedstoneChannelManager {
	private RedstoneChannelManager() {
		throw new UnsupportedOperationException("RedstoneChannelManager only contains static declarations.");
	}

	private static final @NotNull Set<RedstoneChannelKey> CHANNELS =
		new ReferenceOpenHashBigSet<>();
	private static final @NotNull Multimap<RedstoneChannelKey, RedstoneChannelTransmitter> TRANSMITTERS =
		Multimaps.newSetMultimap(new Object2ReferenceLinkedOpenHashMap<>(), ReferenceOpenHashBigSet::new);
	private static final @NotNull Multimap<RedstoneChannelKey, RedstoneChannelReceiver> RECEIVERS =
		Multimaps.newSetMultimap(new Object2ReferenceLinkedOpenHashMap<>(), ReferenceOpenHashBigSet::new);

	public static @NotNull @UnmodifiableView Set<RedstoneChannelKey> getChannels() {
		return Collections.unmodifiableSet(CHANNELS);
	}

	public static boolean registerChannel(@NotNull RedstoneChannelKey channel) {
		return CHANNELS.add(channel);
	}

	public static boolean unregisterChannel(@NotNull RedstoneChannelKey channel) {
		return CHANNELS.remove(channel);
	}

	public static boolean isChannelPowered(@NotNull RedstoneChannelKey channel) {
		return TRANSMITTERS.containsKey(channel);
	}

	public static boolean registerTransmitter(@NotNull RedstoneChannelKey channel, @NotNull RedstoneChannelTransmitter transmitter) {
		var channelTrans = TRANSMITTERS.get(channel);
		boolean retVal = channelTrans.add(transmitter);
		if (retVal && channelTrans.size() == 1) {
			// first transmitter added => state changed to powered
			updateReceivers(channel, true);
		}
		return retVal;
	}

	public static boolean unregisterTransmitter(@NotNull RedstoneChannelKey channel, @NotNull RedstoneChannelTransmitter transmitter) {
		var channelTrans = TRANSMITTERS.get(channel);
		boolean retVal = channelTrans.remove(transmitter);
		if (channelTrans.isEmpty()) {
			// last transmitter removed => state changed to unpowered
			updateReceivers(channel, false);
		}
		return retVal;
	}

	public static boolean registerReceiver(@NotNull RedstoneChannelKey channel, @NotNull RedstoneChannelReceiver receiver) {
		return RECEIVERS.put(channel, receiver);
	}

	private static void updateReceivers(@NotNull RedstoneChannelKey channel, boolean newState) {
		RECEIVERS.get(channel).removeIf(receiver -> !receiver.onChannelStateChanged(channel, newState));
	}
}
