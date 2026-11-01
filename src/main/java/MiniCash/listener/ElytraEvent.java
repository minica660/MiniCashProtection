package MiniCash.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import static MiniCash.MiniCashProtection.ELYTRA_USE_FLAG;

public class ElytraEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void elytraGlide(EntityToggleGlideEvent event) {

        if (event.getEntity() instanceof Player player && event.isGliding()) {
            

            if (player.hasPermission("minicashprotection.access.elytra")) {
                return;
            }

            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);


            StateFlag.State state = query.queryState(BukkitAdapter.adapt(player.getLocation()), localPlayer, ELYTRA_USE_FLAG);

            if (state == StateFlag.State.DENY) {

                event.setCancelled(true);

            }

        }

    }

}
