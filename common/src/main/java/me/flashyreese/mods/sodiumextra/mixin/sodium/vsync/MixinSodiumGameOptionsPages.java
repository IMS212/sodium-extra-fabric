package me.flashyreese.mods.sodiumextra.mixin.sodium.vsync;

import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptionPages;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.Option;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class MixinSodiumGameOptionsPages {
    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @Redirect(method = "general", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 5), remap = false)
    private static OptionGroup.Builder redirectVsyncToggle(OptionGroup.Builder instance, Option<?> option) {
        if (!option.getTooltip().getString().equals(Component.translatable("sodium.options.v_sync.tooltip").getString())) {
            return instance.add(option);
        }
        return instance.add(OptionImpl.createBuilder(SodiumExtraGameOptions.VerticalSyncOption.class, SodiumExtraGameOptionPages.sodiumExtraOpts)
                .setName(Component.translatable("options.vsync"))
                .setTooltip(Component.literal(Component.translatable("sodium.options.v_sync.tooltip").getString() + "\n- " + Component.translatable("sodium-extra.option.use_adaptive_sync.name").getString() + ": " + Component.translatable("sodium-extra.option.use_adaptive_sync.tooltip").getString()))
                .setControl((opt) -> new CyclingControl<>(opt, SodiumExtraGameOptions.VerticalSyncOption.class,
                        SodiumExtraGameOptions.VerticalSyncOption.getAvailableOptions()))
                .setBinding((opts, value) -> {
                    switch (value) {
                        case OFF -> {
                            opts.extraSettings.useAdaptiveSync = false;
                            vanillaOpts.getData().enableVsync().set(false);
                        }
                        case ON -> {
                            opts.extraSettings.useAdaptiveSync = false;
                            vanillaOpts.getData().enableVsync().set(true);
                        }
                        case ADAPTIVE -> {
                            opts.extraSettings.useAdaptiveSync = true;
                            vanillaOpts.getData().enableVsync().set(true);
                        }
                    }
                    vanillaOpts.save();
                }, opts -> {
                    if (vanillaOpts.getData().enableVsync().get() && !opts.extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.ON;
                    } else if (!vanillaOpts.getData().enableVsync().get() && !opts.extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.OFF;
                    } else {
                        return SodiumExtraGameOptions.VerticalSyncOption.ADAPTIVE;
                    }
                })
                .setImpact(OptionImpact.VARIES)
                .build());
    }
}
