package com.denizenscript.denizen.nms.v1_16.impl.entities;

import com.denizenscript.denizen.nms.interfaces.ItemProjectile;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class CraftItemProjectileImpl extends CraftEntity implements ItemProjectile {

    private boolean doesBounce;

    public CraftItemProjectileImpl(CraftServer server, EntityItemProjectileImpl entity) {
        super(server, entity);
    }

    @Override
    public EntityItemProjectileImpl getHandle() {
        return (EntityItemProjectileImpl) super.getHandle();
    }

    @Override
    public String getEntityTypeName() {
        return getType().name();
    }

    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asBukkitCopy(getHandle().getItemStack());
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        getHandle().setItemStack(CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public int getPickupDelay() {
        return 0;
    }

    @Override
    public void setPickupDelay(int i) {
        // Do nothing
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource projectileSource) {
        if (projectileSource instanceof CraftEntity) {
            getHandle().setShooter(((CraftEntity) projectileSource).getHandle());
        }
        else {
            getHandle().projectileSource = projectileSource;
        }
    }

    @Override
    public boolean doesBounce() {
        return doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
