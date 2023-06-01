package ayane.tablisthidden;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Tablisthidden extends JavaPlugin {
    private List<String> hiddenPlayers;

    @Override
    public void onEnable() {
        getLogger().info("Tablisthidden has been enabled.");
        getLogger().info("Author: SukuyaAyane");
        saveDefaultConfig();
        hiddenPlayers = getConfig().getStringList("hidden-players");

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.TAB_COMPLETE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO || event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
                    List<PlayerInfoData> playerInfoDataList = event.getPacket().getPlayerInfoDataLists().read(0);
                    playerInfoDataList.removeIf(playerInfoData -> hiddenPlayers.contains(playerInfoData.getProfile().getName()));
                    event.getPacket().getPlayerInfoDataLists().write(0, playerInfoDataList);
                }
            }
        });

    }
}
