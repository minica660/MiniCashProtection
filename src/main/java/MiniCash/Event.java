package MiniCash;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Event implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof ItemFrame) || !(event.getEntity() instanceof GlowItemFrame)) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        Entity entity =  event.getEntity();

        // WorldGuardのクエリ作成
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

        Location location = BukkitAdapter.adapt(entity.getLocation());

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        if (!query.testState(location, localPlayer, MiniCashProtection.USE_ITEMFLAME)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(event.isBlockInHand()) {


                ItemStack eventItem = event.getItem();

                if (eventItem != null && event.getItem().getType().isBlock()) {

                    Player player = event.getPlayer();

                    if (player.hasPermission("minicashprotection.bypass.blockplace")) {
                        return;
                    }

                    RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                    LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

                    StateFlag.State state = query.queryState(BukkitAdapter.adapt(event.getClickedBlock().getLocation()), localPlayer, MP_BLOCK_PLACE_FLAG);

                    if (state == StateFlag.State.DENY){

                        event.setCancelled(true);

                    }
                }

            }
        }
    }
}
