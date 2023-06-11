package io.github.modsbyleo.wirelessredstone;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelKey;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelReceiver;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelTransmitter;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelsComponent;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashBigSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashBigSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import net.minecraft.nbt.NbtCompound;

public class RedstoneChannelsComponentImpl implements RedstoneChannelsComponent {
	private final @NotNull ObjectOpenHashBigSet<RedstoneChannelKey> globalChannels;
	private final @NotNull Multimap<UUID, RedstoneChannelKey> localChannelsMap;
	private final @NotNull Multimap<RedstoneChannelKey, RedstoneChannelTransmitter> transmittersMap;
	private final @NotNull Multimap<RedstoneChannelKey, RedstoneChannelReceiver> receiversMap;

	public RedstoneChannelsComponentImpl() {
		globalChannels = new ObjectOpenHashBigSet<>();
		localChannelsMap = Multimaps.newSetMultimap(new Object2ReferenceOpenHashMap<>(), ObjectOpenHashBigSet::new);
		transmittersMap = Multimaps.newSetMultimap(new Object2ReferenceOpenHashMap<>(), ReferenceOpenHashBigSet::new);
		receiversMap = Multimaps.newSetMultimap(new Object2ReferenceOpenHashMap<>(), ReferenceOpenHashBigSet::new);
	}

	@Override
	public boolean registerChannel(@NotNull RedstoneChannelKey key) {
		if (key.type() == RedstoneChannelKey.Type.GLOBAL) {
			return globalChannels.add(key);
		} else {
			return localChannelsMap.put(key.ownerUUID(), key);
		}
	}

	@Override
	public @UnmodifiableView Collection<RedstoneChannelKey> getGlobalChannels() {
		return Collections.unmodifiableCollection(globalChannels);
	}

	@Override
	public @UnmodifiableView Collection<RedstoneChannelKey> getLocalChannels(@NotNull UUID ownerUUID) {
		return Collections.unmodifiableCollection(localChannelsMap.get(ownerUUID));
	}

	@Override
	public boolean isChannelPowered(@NotNull RedstoneChannelKey key) {
		return transmittersMap.containsKey(key);
	}

	@Override
	public boolean registerTransmitter(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelTransmitter transmitter) {
		var transmitters = transmittersMap.get(key);
		boolean retVal = transmitters.add(transmitter);
		if (retVal && transmitters.size() == 1) {
			// first transmitter added => state change "unpowered" to "powered"
			updateReceivers(key, true);
		}
		return retVal;
	}

	@Override
	public boolean unregisterTransmitter(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelTransmitter transmitter) {
		var transmitters = transmittersMap.get(key);
		boolean retVal = transmitters.remove(transmitter);
		if (retVal && transmitters.isEmpty()) {
			// last transmitter removed => state change "powered" to "unpowered"
			updateReceivers(key, false);
		}
		return retVal;
	}

	@Override
	public boolean registerReceiver(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelReceiver receiver) {
		return receiversMap.put(key, receiver);
	}

	@Override
	public boolean unregisterReceiver(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelReceiver receiver) {
		return receiversMap.remove(key, receiver);
	}

	private void updateReceivers(@NotNull RedstoneChannelKey key, boolean newState) {
		receiversMap.get(key).removeIf(receiver -> !receiver.onChannelStateChanged(key, newState));
	}

	@Override
	public void readFromNbt(NbtCompound tag) {

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
}
