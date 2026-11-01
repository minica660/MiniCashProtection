package MiniCash.listener;

import dev.geco.gsit.api.GSitAPI;
import dev.geco.gsit.api.event.PreEntityStopSitEvent;
import dev.geco.gsit.model.StopReason;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class WindChargeUnSitListener implements Listener {

    private final JavaPlugin plugin;

    private final String METADATA_KEY = "hit_by_wind_charge";

    public WindChargeUnSitListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWindChargeExplode(EntityExplodeEvent event) {

        if (event.getEntity() instanceof WindCharge) {

            double radius = 3.0;

            for (Entity entity : event.getLocation().getWorld().getNearbyEntities(event.getLocation(), radius, radius, radius)) {

                if (entity instanceof Player player) {

                    player.setMetadata(METADATA_KEY, new FixedMetadataValue(plugin, true));

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {

                        if (player.isOnline()) {
                            player.removeMetadata(METADATA_KEY, plugin);
                        }

                    }, 1L);

                }


            }
        }
    }

    @EventHandler
    public void onWindCharge(PreEntityStopSitEvent event) {

        if (event.getEntity() instanceof Player player) {

            if (event.getReason() == StopReason.BLOCK_BREAK && player.hasMetadata(METADATA_KEY)) {

                event.setCancelled(true);

                player.removeMetadata(METADATA_KEY, plugin);

            }

        }


    }

}
