package me.flashyreese.mods.sodiumextra.mixin.toasts;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TutorialToast.class)
public class MixinTutorialToast {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void draw(GuiGraphics guiGraphics, ToastComponent manager, long startTime, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (!SodiumExtraClientMod.options().extraSettings.tutorialToast) {
            cir.setReturnValue(Toast.Visibility.HIDE);
        }
    }
}
