package me.hai.simplevault;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Inventory> vaults = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SimpleVault da bat!");
    }

    // Tạo hoặc lấy kho của người chơi
    private Inventory getVault(Player player) {
        return vaults.computeIfAbsent(player.getUniqueId(),
                uuid -> Bukkit.createInventory(null, 54, "Kho cua " + player.getName()));
    }

    // Lệnh /kho
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Chi nguoi choi moi dung duoc lenh nay!");
            return true;
        }

        Player player = (Player) sender;

        // /kho -> mở kho của mình
        if (args.length == 0) {
            player.openInventory(getVault(player));
            return true;
        }

        // /kho <player> -> admin xem kho người khác
        if (!player.hasPermission("simplevault.admin")) {
            player.sendMessage("Ban khong co quyen!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("Nguoi choi khong online!");
            return true;
        }

        player.openInventory(getVault(target));
        player.sendMessage("Dang xem kho cua " + target.getName());
        return true;
    }

    // Hút vật phẩm khi đào block
    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();
        Inventory vault = getVault(player);

        for (ItemStack drop : e.getBlock().getDrops()) {
            vault.addItem(drop);
        }

        e.setDropItems(false);
    }
}
