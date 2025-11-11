package com.bytespacegames.autogg.mixin;

import com.bytespacegames.autogg.AutoGG;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
	@Inject(
			at = @At("HEAD"),
			method = "addMessage*"
	)
	public void mixin$addMessage(Component contents, CallbackInfo ci) {
		String content = contents.getString().replaceAll("§.", "").stripTrailing();
		AutoGG.INSTANCE.handleChat(content);
	}
}