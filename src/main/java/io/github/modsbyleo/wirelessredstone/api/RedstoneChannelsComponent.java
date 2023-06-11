package io.github.modsbyleo.wirelessredstone.api;

import java.util.Collection;
import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.modsbyleo.wirelessredstone.WirelessRedstoneComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public interface RedstoneChannelsComponent extends Component {
	static @NotNull RedstoneChannelsComponent get(@NotNull MinecraftServer server) {
		return server.getOverworld().getProperties().getComponent(WirelessRedstoneComponents.CHANNELS);
	}

	static @NotNull RedstoneChannelsComponent get(@NotNull ServerWorld world) {
		return world.getProperties().getComponent(WirelessRedstoneComponents.CHANNELS);
	}

	boolean registerChannel(@NotNull RedstoneChannelKey key);
	@UnmodifiableView Collection<RedstoneChannelKey> getGlobalChannels();
	@UnmodifiableView Collection<RedstoneChannelKey> getLocalChannels(@NotNull UUID ownerUUID);

	boolean isChannelPowered(@NotNull RedstoneChannelKey key);
	boolean registerTransmitter(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelTransmitter transmitter);
	boolean unregisterTransmitter(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelTransmitter transmitter);
	boolean registerReceiver(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelReceiver receiver);
	boolean unregisterReceiver(@NotNull RedstoneChannelKey key, @NotNull RedstoneChannelReceiver receiver);
}
