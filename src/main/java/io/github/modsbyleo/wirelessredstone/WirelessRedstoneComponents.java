package io.github.modsbyleo.wirelessredstone;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelsComponent;

public class WirelessRedstoneComponents implements LevelComponentInitializer {
	public static final ComponentKey<RedstoneChannelsComponent> CHANNELS =
		ComponentRegistry.getOrCreate(WirelessRedstoneInitializer.id("channels"), RedstoneChannelsComponent.class);

	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(CHANNELS, props -> new RedstoneChannelsComponentImpl());
	}
}
