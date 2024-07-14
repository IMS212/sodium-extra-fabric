package me.flashyreese.mods.sodiumextra.mixin.toasts;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastComponent.class)
public class MixinToastManager {
    @Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
    public void goodByeToasts(CallbackInfo ci) {
        if (!SodiumExtraClientMod.options().extraSettings.toasts) {
            ci.cancel();
        }
    }
}
