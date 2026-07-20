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
            // このフラグがtrueの場合は使用アイテムフレームの使用が可能ということになる
            StateFlag useItemFlame = new StateFlag("use-itemflame", false);
            registry.register(useItemFlame);
            USE_ITEMFLAME = useItemFlame;

            StateFlag mpBlockPlace = new StateFlag("mp-block-place", false);
            registry.register(mpBlockPlace);
            MP_BLOCK_PLACE_FLAG = mpBlockPlace;
        } catch (FlagConflictException e) {
            getLogger().severe("フラグの登録に失敗しました (use-itemflame): " + e.getMessage());
        }


    }


    @Override
    public void onEnable() {
        // Plugin startup logic
//        region = new ProtectedCuboidRegion()
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}

