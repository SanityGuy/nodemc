package me.serenity.nodeMC.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Objects;

public class EntitySpawn implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if (!(e.getEntity() instanceof LivingEntity entity)) return;

        EntityType entityType = entity.getType();
        int maxHealth = (int) Objects.requireNonNull(entity
                .getAttribute(Attribute.MAX_HEALTH)).getValue();
        int health = (int) entity.getHealth();

        int level = switch (entityType) {
            case ZOMBIE, SKELETON, SPIDER -> 5;
            case COPPER_GOLEM, IRON_GOLEM -> 15;
            case WARDEN, ENDER_DRAGON -> 30;
            default -> 1;
        };

        entity.customName(MM.deserialize(
                "<dark_gray>[<gray>Lv"+level+"</gray>]</dark_gray> " +
                        "<red>"+entity.name()+"</red> " +
                        "<green>"+health+"<white>/</white>"+maxHealth+"</green>" +
                        "<red>❤️</red>"));

        entity.setCustomNameVisible(true);
        entity.setSilent(true);
    }
}
