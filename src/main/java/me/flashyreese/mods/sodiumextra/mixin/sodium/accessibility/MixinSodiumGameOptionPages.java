package me.flashyreese.mods.sodiumextra.mixin.sodium.accessibility;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class MixinSodiumGameOptionPages {

    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 2, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void quality(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(Component.translatable("options.screenEffectScale"))
                        .setTooltip(Component.translatable("options.screenEffectScale.tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.screenEffectScale().set((double) value / 100.0F), (opts) -> Math.toIntExact(Math.round(opts.screenEffectScale().get() * 100.0F)))
                        .setImpact(OptionImpact.LOW)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                        .setName(Component.translatable("options.fovEffectScale"))
                        .setTooltip(Component.translatable("options.fovEffectScale.tooltip"))
                        .setControl(option -> new SliderControl(option, 0, 100, 1, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.fovEffectScale().set(Math.sqrt(value / 100.0F)), (opts) -> (int) Math.round(Math.pow(opts.fovEffectScale().get(), 2.0D) * 100.0F))
                        .setImpact(OptionImpact.LOW)
                        .build()
                )
                .build());
    }
}
