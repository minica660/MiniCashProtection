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

        Location location = BukkitAdapter.adapt(entity.getLocation());

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        if (!query.testState(location, localPlayer, MiniCashProtection.USE_ITEMFLAME)) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(BlockPlaceEvent event) {

        Player player = event.getPlayer();


        if (player.hasPermission("minicashprotection.bypass.blockplace")) {
            return;
        }

        Block block = event.getBlockPlaced();



        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        StateFlag.State state = query.queryState(BukkitAdapter.adapt(block.getLocation()), localPlayer, MP_BLOCK_PLACE_FLAG);

        if (state == StateFlag.State.DENY) {


            event.setCancelled(true);

            org.bukkit.Location playerLocation = player.getLocation();

            if (isJumpBlock(playerLocation, block.getLocation())) {

                player.setVelocity(player.getVelocity().setY(-0.12));

            }
        }

    }





    private boolean isJumpBlock(org.bukkit.Location playerLoc, org.bukkit.Location blockLocation) {

        if (!playerLoc.getWorld().equals(blockLocation.getWorld())){
            return false;
        }

        double pX = playerLoc.getX();
        double pY = playerLoc.getY();
        double pZ = playerLoc.getZ();

        double bX = blockLocation.getBlockX();
        double bY = blockLocation.getBlockY();
        double bZ = blockLocation.getBlockZ();

        boolean yMatches = (pY >= bY && pY <= bY + 1.5);
        boolean xMatches = (pX >= bX - 0.3 && pX <= bX + 1.3);
        boolean zMatches = (pZ >= bZ - 0.3 && pZ <= bZ + 1.3);

        return yMatches && xMatches && zMatches;
    }

}
