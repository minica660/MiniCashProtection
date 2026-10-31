package MiniCash;

import MiniCash.listener.AntiGlitchPlaceBlock;
import MiniCash.listener.ElytraEvent;
import MiniCash.listener.ItemFrameListener;
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

        try {

            // エリトラ使用を制限
            StateFlag mpBlockPlace = new StateFlag("use-elytra", false);
            registry.register(mpBlockPlace);
            MP_BLOCK_PLACE_FLAG = mpBlockPlace;

        } catch (FlagConflictException e) {

            if (registry.get("use-elytra") instanceof StateFlag existing) {

                MP_BLOCK_PLACE_FLAG = existing;

            }
        }



        // PacketEvents関連
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();


    }


    @Override
    public void onEnable() {
        // Plugin startup logic
//        region = new ProtectedCuboidRegion()
        getServer().getPluginManager().registerEvents(new ItemFrameListener(USE_ITEMFLAME), this);
        getServer().getPluginManager().registerEvents(new ElytraEvent(), this);

        // PacketEvents関連

        PacketEvents.getAPI().init();


        PacketEvents.getAPI().getEventManager().registerListener(new AntiGlitchPlaceBlock(this ,MP_BLOCK_PLACE_FLAG));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PacketEvents.getAPI().terminate();

    }

}

