package MiniCash.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import static MiniCash.MiniCashProtection.ELYTRA_USE_FLAG;

public class ElytraEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void elytraGlide(EntityToggleGlideEvent event) {

        if (event.getEntity() instanceof Player player && event.isGliding()) {

            if (shouldDenyElytra(player)) {

                Location eventLocation = player.getLocation();


                event.setCancelled(true);

                player.setGliding(false);

                player.teleport(eventLocation);

//                player.sendMessage("§cこのエリアではエリトラでの飛行は禁止されています");

            }


        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();

        if (chestplate != null && chestplate.getType() == Material.ELYTRA) {

            if (shouldDenyElytra(player)) {

                event.setCancelled(true);

//                player.sendMessage("§cこのエリアではエリトラを装備したままジャンプすることはできません！外してください");
            }
        }
    }

//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onPlayerMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//        ItemStack chestplate = player.getInventory().getChestplate();
//
//        if (chestplate != null && chestplate.getType() == Material.ELYTRA) {
//
//            Location from = event.getFrom();
//            Location to = event.getTo();
//            if (to != null && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
//
//                if (shouldDenyElytra(player)) {
//
//                    if (player.isGliding()) {
//                        player.setGliding(false);
//                    }
//
//                    event.setCancelled(true);
//
//                    player.sendMessage("§cこのエリアではエリトラを装備したまま移動することはできません！外してください。");
//                }
//            }
//        }
//    }

    /**
     * 指定したプレイヤーのいる位置でエリトラ使用が禁止されているかどうかを判定します
     * @param player 対象プレイヤー
     * @return true 禁止されている
     */
    private boolean shouldDenyElytra(Player player){


        if (player.hasPermission("minicashprotection.access.elytra")) {
            return false;
        }

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);


        StateFlag.State state = query.queryState(BukkitAdapter.adapt(player.getLocation()), localPlayer, ELYTRA_USE_FLAG);

        return state == StateFlag.State.DENY;


    }



}
