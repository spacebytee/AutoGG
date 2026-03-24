package com.bytespacegames.autogg.mixin;

import com.bytespacegames.autogg.AutoGG;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
	@Inject(
			at = @At("HEAD"),
			method = "addMessage"
	)
	public void mixin$addMessage(Component contents, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag, CallbackInfo ci) {
		String content = contents.getString().replaceAll("§.", "").stripTrailing();
		AutoGG.INSTANCE.handleChat(content);

		/*if (content.trim().equals("The game starts in 5 seconds!")) {
			AutoGG.INSTANCE.sendChatMessage("/ac glhf");
		}*/
	}
}