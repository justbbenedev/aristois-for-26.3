package me.deftware.weaver;

import me.deftware.client.framework.util.path.LocationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;

/**
 * Lightweight bootstrap entrypoint that points {@code fabric.modsFolder} to the
 * version-specific EMC framework directory (matching legacy Weaver behavior and
 * isolating it from {@code .minecraft/mods}) and delegates launch to {@link Knot}.
 */
public class Main implements PreLaunchEntrypoint {
    public static void main(String[] args) {
        try {
            // Carpeta de mods propia del perfil: <gameDir>/mods-aristois si existe
            // (Sodium/Iris reemplazan el renderer y rompen X-ray y otros módulos).
            String gameDir = ".";
            for (int i = 0; i < args.length - 1; i++) {
                if ("--gameDir".equals(args[i])) gameDir = args[i + 1];
            }
            File own = new File(gameDir, "mods-aristois");
            if (System.getProperty("fabric.modsFolder") == null && own.isDirectory()) {
                System.setProperty("fabric.modsFolder", own.getAbsolutePath());
            }
            System.out.println("[EMC] fabric.modsFolder = " + System.getProperty("fabric.modsFolder"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Knot.launch(args, EnvType.CLIENT);
    }

    @Override
    public void onPreLaunch() {
        if (Boolean.getBoolean("emc.auditMixins")) {
            System.out.println("=== RUNNING FULL SPONGE MIXIN AUDIT ===");
            MixinEnvironment.getCurrentEnvironment().audit();
            System.out.println("=== SPONGE MIXIN AUDIT FINISHED ===");
            net.minecraft.SharedConstants.tryDetectVersion();
            net.minecraft.server.Bootstrap.bootStrap();
            net.minecraft.core.registries.BuiltInRegistries.DATA_COMPONENT_INITIALIZERS
                    .build(net.minecraft.data.registries.VanillaRegistries.createWorldLookup())
                    .forEach(net.minecraft.core.component.DataComponentInitializers.PendingComponents::apply);
            System.out.println("APPLE isFood=" + ((me.deftware.client.framework.item.Item) net.minecraft.world.item.Items.APPLE).isFood()
                    + ", name=" + ((me.deftware.client.framework.item.Item) net.minecraft.world.item.Items.APPLE).getName().getString());
            System.exit(0);
        }
    }
}
