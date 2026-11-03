package MiniCash;

import MiniCash.listener.AntiGlitchPlaceBlock;
import MiniCash.listener.ElytraEvent;
import MiniCash.listener.ItemFrameListener;
import MiniCash.listener.WindChargeUnSitListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniCashProtection extends JavaPlugin {

    private ProtectedCuboidRegion region;
    public static StateFlag USE_ITEMFLAME;
    public static StateFlag MP_BLOCK_PLACE_FLAG;
    public static StateFlag ELYTRA_USE_FLAG;


    // 各プラグインが有効かどうかのフラグ
    private boolean usePacketEvents = false;
    private boolean useGSit = false;


    @Override
    public void onLoad() {

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();


        try {

            StateFlag useItemFlame = new StateFlag("use-itemflame", false);
            registry.register(useItemFlame);
            USE_ITEMFLAME = useItemFlame;

        } catch (FlagConflictException e) {

            if (registry.get("use-itemflame") instanceof StateFlag existing) {

                USE_ITEMFLAME = existing;

            }
        }

        if (getServer().getPluginManager().getPlugin("packetevents") != null) {
            usePacketEvents = true;

            try {

                // 不正な壁登りをパケットレベルで防止！？
                StateFlag mpBlockPlace = new StateFlag("mp-block-place", false);
                registry.register(mpBlockPlace);
                MP_BLOCK_PLACE_FLAG = mpBlockPlace;

            } catch (FlagConflictException e) {

                if (registry.get("mp-block-place") instanceof StateFlag existing) {

                    MP_BLOCK_PLACE_FLAG = existing;

                }
            }

            loadPacketEvents();


        }

        try {

            // エリトラ使用を制限
            StateFlag elytra = new StateFlag("use-elytra", false);
            registry.register(elytra);
            ELYTRA_USE_FLAG = elytra;

        } catch (FlagConflictException e) {

            if (registry.get("use-elytra") instanceof StateFlag existing) {

                ELYTRA_USE_FLAG = existing;

            }
        }


    }


    @Override
    public void onEnable() {
        // Plugin startup logic
//        region = new ProtectedCuboidRegion()
        getServer().getPluginManager().registerEvents(new ItemFrameListener(USE_ITEMFLAME), this);
        getServer().getPluginManager().registerEvents(new ElytraEvent(), this);

        if (getServer().getPluginManager().getPlugin("GSit") != null) {
            useGSit = true;
            registerGSitEvents();

        }
        // PacketEvents関連


        if (usePacketEvents) {
            enablePacketEvents();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (usePacketEvents) {
            terminatePacketEvents();
        }

    }


    private void loadPacketEvents() {
        com.github.retrooper.packetevents.PacketEvents.setAPI(
                io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder.build(this)
        );
        com.github.retrooper.packetevents.PacketEvents.getAPI().load();
    }

    private void enablePacketEvents() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().init();
        com.github.retrooper.packetevents.PacketEvents.getAPI().getEventManager().registerListener(
                new MiniCash.listener.AntiGlitchPlaceBlock(this, MP_BLOCK_PLACE_FLAG)
        );
    }

    private void terminatePacketEvents() {
        com.github.retrooper.packetevents.PacketEvents.getAPI().terminate();
    }

    private void registerGSitEvents() {
        getServer().getPluginManager().registerEvents(
                new MiniCash.listener.WindChargeUnSitListener(this), this
        );
    }

}

