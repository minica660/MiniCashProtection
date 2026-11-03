package MiniCash.listener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGlitchPlaceBlock extends PacketListenerAbstract {

    private final JavaPlugin plugin;
    private final StateFlag stateFlag;

    public AntiGlitchPlaceBlock(JavaPlugin plugin, StateFlag stateFlag) {
        this.plugin = plugin;
        this.stateFlag = stateFlag;
    }


    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {

            Player player = event.getPlayer();

            if (player == null || player.hasPermission("minicashprotection.access.mp-block-place")) {
                return;
            }

            WrapperPlayClientPlayerBlockPlacement packet = new WrapperPlayClientPlayerBlockPlacement(event);


            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

            // プレイヤーの現在地で判定
            StateFlag.State state = query.queryState(BukkitAdapter.adapt(player.getLocation()), localPlayer, stateFlag);

            if (state == StateFlag.State.DENY) {

                if (player.hasPermission("minicashprotection.access.mp-block-place")) {
                    return;
                }

                Vector3i blockPosition = packet.getBlockPosition();
                BlockFace blockFace = packet.getFace();
                if (blockPosition == null || blockFace == null) return;

                int targetX = blockPosition.getX();
                int targetY = blockPosition.getY();
                int targetZ = blockPosition.getZ();

                switch (blockFace) {
                    case UP -> targetY++;
                    case DOWN -> targetY--;
                    case NORTH -> targetZ--;
                    case SOUTH -> targetZ++;
                    case WEST -> targetX--;
                    case EAST -> targetX++;
                }

                Vector3i targetPosition = new Vector3i(targetX, targetY, targetZ);

                // 3. 🌟おっしゃる通り、ここが最も理にかなっている本質的な処理です。
                // 設置先が空気であることを伝えるため、マイクラの「空気（AIR）」のグローバルブロックID（0）を指定
                int airGlobalId = 0;

                // 「そこは空気だよ」という BlockChange パケットを作成
                WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange(targetPosition, airGlobalId);

                // 4. プレイヤーのネットワークチャンネルへ直接・最速でパケットを割り込ませて送信
                // これにより、クライアントの画面にあるゴーストブロックが即座にAIRへと上書きされて消滅します
                PacketEvents.getAPI().getProtocolManager().sendPacket(event.getChannel(), blockChange);
            }
        }
    }
}





