package MiniCash;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static MiniCash.MiniCashProtection.MP_BLOCK_PLACE_FLAG;
import static MiniCash.MiniCashProtection.USE_ITEMFLAME;

public class Event implements Listener {

    private final MiniCashProtection plugin;

    public Event(MiniCashProtection plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }


        Entity entity = event.getEntity();

        // WorldGuardのクエリ作成
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);


        StateFlag.State state = query.queryState(BukkitAdapter.adapt(entity.getLocation()), localPlayer, USE_ITEMFLAME);

        if (state == StateFlag.State.DENY) {

            if (player.hasPermission("minicashprotection.access.useitemflame")) {
                return;
            }

            event.setCancelled(true);

        }

    }







}
