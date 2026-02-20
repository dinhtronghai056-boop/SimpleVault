package me.hai.simplevault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {

    private boolean enabled = true;
    private final Set<Material> allowed = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SimpleVault enabled!");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!enabled) return;

        Material type = e.getBlock().getType();
        if (!allowed.contains(type)) return;

        e.setDropItems(false);
        e.getPlayer().getInventory().addItem(new ItemStack(type, 1));
    }
}
