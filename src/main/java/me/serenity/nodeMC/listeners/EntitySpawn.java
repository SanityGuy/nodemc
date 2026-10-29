
package me.serenity.nodeMC.listeners;

import me.serenity.nodeMC.NodeMC;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Locale;

public class EntitySpawn implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final NamespacedKey MANAGED = new NamespacedKey(NodeMC.get(), "managed_mob");

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!(e.getEntity() instanceof LivingEntity entity)) return;
        if (entity instanceof Player) return;
        if (entity.customName() != null) return;

        entity.getPersistentDataContainer().set(MANAGED, PersistentDataType.BYTE, (byte) 1);
        updateDisplay(entity);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof LivingEntity entity)) return;
        scheduleUpdate(entity);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        scheduleUpdate((LivingEntity) e.getEntity());
    }

    private void scheduleUpdate(LivingEntity entity) {
        if (entity instanceof Player) return;
        if (!entity.getPersistentDataContainer().has(MANAGED, PersistentDataType.BYTE)) return;

        Bukkit.getScheduler().runTask(NodeMC.get(), () -> {
            if (!entity.isValid() || entity.isDead()) return;
            updateDisplay(entity);
        });
    }

    private void updateDisplay(LivingEntity entity) {
        var attribute = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null) return;

        int maxHealth = (int) Math.ceil(attribute.getValue());
        int health = (int) Math.ceil(entity.getHealth());
        int level = getLevel(entity.getType());

        String name = entity.getType().name()
                .toLowerCase(Locale.ROOT)
                .replace('_', ' ');

        name = toTitleCase(name);

        entity.customName(MM.deserialize(
                "<dark_gray>[<gray>Lv" + level + "</gray>]</dark_gray> " +
                        "<red>" + name + "</red> " +
                        "<green>" + health + "<white>/</white>" + maxHealth + "</green> " +
                        "<red>❤</red>"
        ));

        entity.setCustomNameVisible(true);
    }

    private int getLevel(EntityType type) {
        return switch (type) {
            case ZOMBIE, SKELETON, SPIDER -> 5;
            case COPPER_GOLEM, IRON_GOLEM -> 15;
            case WARDEN, ENDER_DRAGON -> 30;
            default -> 1;
        };
    }

    private String toTitleCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }

        return result.toString();
    }

}
