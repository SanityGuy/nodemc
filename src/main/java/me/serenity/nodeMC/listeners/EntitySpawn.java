package me.serenity.nodeMC.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

        String name = PlainTextComponentSerializer.plainText().serialize(entity.name());

        entity.customName(MM.deserialize(
                "<dark_gray>[<gray>Lv"+level+"</gray>]</dark_gray> " +
                        "<red>"+name+"</red> " +
                        "<green>"+health+"<white>/</white>"+maxHealth+"</green>" +
                        "<red>❤️</red>"));

        entity.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
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

        String name = PlainTextComponentSerializer.plainText().serialize(entity.name());

        entity.customName(MM.deserialize(
                "<dark_gray>[<gray>Lv"+level+"</gray>]</dark_gray> " +
                        "<red>"+name+"</red> " +
                        "<green>"+health+"<white>/</white>"+maxHealth+"</green>" +
                        "<red>❤️</red>"));

        entity.setCustomNameVisible(true);
    }
}
