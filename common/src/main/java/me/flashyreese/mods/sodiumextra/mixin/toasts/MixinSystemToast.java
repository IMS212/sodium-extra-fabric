package me.flashyreese.mods.sodiumextra.mixin.toasts;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SystemToast.class)
public class MixinSystemToast {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void draw(GuiGraphics guiGraphics, ToastComponent manager, long startTime, CallbackInfoReturnable<Toast.Visibility> cir) {
        if (!SodiumExtraClientMod.options().extraSettings.systemToast) {
            cir.setReturnValue(Toast.Visibility.HIDE);
        }
    }
}
