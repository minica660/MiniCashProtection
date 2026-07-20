package MiniCash;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiniCashProtection extends JavaPlugin {

    private ProtectedCuboidRegion region;
    public static StateFlag USE_ITEMFLAME;
    public static StateFlag MP_BLOCK_PLACE_FLAG;

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

            StateFlag mpBlockPlace = new StateFlag("mp-block-place", false);
            registry.register(mpBlockPlace);
            MP_BLOCK_PLACE_FLAG = mpBlockPlace;

        } catch (FlagConflictException e) {
            if (registry.get("mp-block-place") instanceof StateFlag existing) {

                MP_BLOCK_PLACE_FLAG = existing;

            }
        }


    }


    @Override
    public void onEnable() {
        // Plugin startup logic
//        region = new ProtectedCuboidRegion()
        getServer().getPluginManager().registerEvents(new Event(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}

