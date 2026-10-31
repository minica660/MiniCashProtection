package MiniCash.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Collection;

public class ItemFrameListener implements Listener {

    private final StateFlag useItemFrameFlag;

    public ItemFrameListener(StateFlag  stateFlag ) {
        this.useItemFrameFlag = stateFlag;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof ItemFrame itemFrame)) {
            return;
        }

        Player player = null;

        // 直接の攻撃者がプレイヤーの場合
        if (event.getDamager() instanceof Player p) {
            player = p;
        }

        else if (event.getDamager() instanceof Projectile projectile) {

            if (projectile.getShooter() instanceof Player p) {

                player = p;
            }

        }

        if (player == null) {
            return;
        }


        // WorldGuardのクエリ作成
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);


        StateFlag.State state = query.queryState(BukkitAdapter.adapt(itemFrame.getLocation()), localPlayer, useItemFrameFlag);

        if (state == StateFlag.State.DENY) {

            if (player.hasPermission("minicashprotection.access.useitemflame")) {
                return;
            }

            event.setCancelled(true);

            player.sendMessage(Component.text("§cHey! Sorry, but you can't use that here."));


        }

    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockFromTo(BlockFromToEvent event) {

        Block toBlock = event.getToBlock();

        // 流入先のブロックに額縁が存在するか確認
        Collection<Entity> entities = toBlock.getWorld().getNearbyEntities(toBlock.getLocation().add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5);

        boolean hasItemFrame = false;

        for (Entity entity : entities) {

            if (entity instanceof ItemFrame) {

                hasItemFrame = true;

                break;

            }

        }

        if (!hasItemFrame) {
            return;
        }

        event.setCancelled(true);
//        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//        RegionQuery query = container.createQuery();
//
//        StateFlag.State state = query.queryState(BukkitAdapter.adapt(toBlock.getLocation()), null, useItemFrameFlag);
//
//        if (state == StateFlag.State.DENY) {
//            event.setCancelled(true);
//        }


    }




    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        Block placedBlock = event.getBlockPlaced();
        Player player = event.getPlayer();

        Collection<Entity> entities = placedBlock.getWorld().getNearbyEntities(placedBlock.getLocation().add(0.5, 0.5, 0.5), 0.6, 0.6, 0.6);

        boolean nearItemFrame = false;

        for (Entity entity : entities) {

            if (entity instanceof ItemFrame) {

                nearItemFrame = true;

                break;

            }

        }

        if (!nearItemFrame) {
            return;
        }

        event.setCancelled(true);

//        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
//        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
//
//        StateFlag.State state = query.queryState(BukkitAdapter.adapt(placedBlock.getLocation()), localPlayer, useItemFrameFlag);
//
//        if (state == StateFlag.State.DENY) {
//
//            if (player.hasPermission("minicashprotection.access.useitemflame")) {
//                return;
//            }
//
//            event.setCancelled(true);
//
//        }
    }





}
