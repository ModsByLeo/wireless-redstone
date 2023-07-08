package io.github.modsbyleo.wirelessredstone;

import com.mojang.serialization.JsonOps;
import io.github.modsbyleo.wirelessredstone.api.RedstoneChannelKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class WirelessRedstoneInitializer implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Wireless Redstone: Leo's Take");

	@Override
	public void onInitialize(ModContainer mod) {
		var key = RedstoneChannelKey.ofGlobal("test");
		var result = RedstoneChannelKey.CODEC.encodeStart(JsonOps.INSTANCE, key);
		var a = result.getOrThrow(false, ignored -> {});
		LOGGER.info(a.toString());
		throw new RuntimeException("STOP");
	}
}
