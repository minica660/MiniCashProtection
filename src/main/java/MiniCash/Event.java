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
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static MiniCash.MiniCashProtection.MP_BLOCK_PLACE_FLAG;

public class Event implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof ItemFrame)) {
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


            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null) {
                Material type = clickedBlock.getType();

                if (!player.isSneaking()) {

                    if (Tag.SHULKER_BOXES.isTagged(type)
                            || Tag.ALL_SIGNS.isTagged(type)
                            || Tag.DOORS.isTagged(type)
                            || Tag.BUTTONS.isTagged(type)
                            || Tag.TRAPDOORS.isTagged(type)
                            || Tag.FENCE_GATES.isTagged(type)
                            || type == Material.CHEST
                            || type == Material.TRAPPED_CHEST
                            || type == Material.BARREL
                            || type == Material.FURNACE
                            || type == Material.BLAST_FURNACE
                            || type == Material.SMOKER
                            || type == Material.CRAFTING_TABLE
                            || type == Material.ANVIL
                            || type == Material.CHIPPED_ANVIL
                            || type == Material.DAMAGED_ANVIL
                            || type == Material.ENCHANTING_TABLE
                            || type == Material.LOOM
                            || type == Material.CARTOGRAPHY_TABLE
                            || type == Material.GRINDSTONE
                            || type == Material.SMITHING_TABLE
                            || type == Material.STONECUTTER
                            || type == Material.BEACON
                            || type == Material.HOPPER
                            || type == Material.DROPPER
                            || type == Material.DISPENSER
                            || type == Material.LECTERN
                            || type == Material.BREWING_STAND
                            || type == Material.CRAFTER) {


                        return;
                    }

                }
            }

            if (event.isBlockInHand()) {

                if (event.getClickedBlock() != null && event.getClickedBlock().getType().isBlock()) {


                    ItemStack eventItem = event.getItem();

                    if (eventItem != null && event.getItem().getType().isBlock()) {


                        if (player.hasPermission("minicashprotection.bypass.blockplace")) {
                            return;
                        }

                        Block targetBlock = clickedBlock.getRelative(event.getBlockFace());

                        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
                        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

                        StateFlag.State state = query.queryState(BukkitAdapter.adapt(targetBlock.getLocation()), localPlayer, MP_BLOCK_PLACE_FLAG);

                        if (state == StateFlag.State.DENY) {

                            event.setCancelled(true);

                        }
                    }

                }
            }
        }
    }
}
