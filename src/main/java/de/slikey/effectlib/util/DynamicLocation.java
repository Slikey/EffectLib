package de.slikey.effectlib.util;

import java.lang.ref.WeakReference;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Represents a Location that can move, possibly bound to an Entity.
 */
public class DynamicLocation {

    private final Location location;
    private final Location originalLocation;
    private final WeakReference<Entity> entity;
    private float yawOffset;
    private float pitchOffset;
    private Vector offset;
    private Vector relativeOffset;
    private Vector entityOffset;
    private boolean updateLocation = true;
    private boolean updateDirection = true;
    private Float yaw = null;
    private Float pitch = null;

    public DynamicLocation(Location location) {
        if (location != null) this.location = location.clone();
        else this.location = null;
        originalLocation = location;
        entity = null;
    }

    public DynamicLocation(Entity entity) {
        if (entity != null) {
            this.entity = new WeakReference<>(entity);
            location = getEntityLocation(entity);
        } else {
            this.entity = null;
            location = null;
        }
        originalLocation = location;
    }

    public DynamicLocation(Location location, Entity entity) {
        if (location != null) this.location = location.clone();
        else if (entity != null) this.location = getEntityLocation(entity);
        else this.location = null;

        if (entity != null) {
            this.entity = new WeakReference<>(entity);
            entityOffset = this.location.toVector().subtract(getEntityLocation(entity).toVector());
        } else {
            this.entity = null;
        }

        originalLocation = this.location == null ? null : this.location.clone();
    }

    public void addOffset(Vector offset) {
        if (this.offset == null) this.offset = offset.clone();
        else this.offset.add(offset);
        updateOffsets();
    }

    public void subtractOffset(Vector offset) {
        if (this.offset == null) this.offset = offset.clone();
        else this.offset.subtract(offset);
        updateOffsets();
    }

    public void addRelativeOffset(Vector offset) {
        if (relativeOffset == null) relativeOffset = offset.clone();
        else relativeOffset.add(offset);
        updateOffsets();
    }

    public void subtractRelativeOffset(Vector offset) {
        if (relativeOffset == null) relativeOffset = offset.clone();
        else relativeOffset.subtract(offset);
        updateOffsets();
    }

    public Entity getEntity() {
        return entity == null ? null : entity.get();
    }

    public Location getLocation() {
        return location;
    }

    protected Location getEntityLocation(Entity entity) {
        if (entity instanceof LivingEntity) return ((LivingEntity) entity).getEyeLocation();
        return entity.getLocation();
    }

    public void setDirection(Vector direction) {
        if (location == null || direction == null) return;
        location.setDirection(direction);
        updateDirection();
    }

    public void updateDirection() {
        if (location == null) return;
        if (yaw != null) location.setYaw(yaw);
        if (pitch != null) location.setPitch(pitch);
        if (yawOffset != 0) location.setYaw(location.getYaw() + yawOffset);
        if (pitchOffset != 0) location.setPitch(location.getPitch() + pitchOffset);
    }

    public void updateFrom(Location newLocation) {
        if (originalLocation != null) {
            originalLocation.setX(newLocation.getX());
            originalLocation.setY(newLocation.getY());
            originalLocation.setZ(newLocation.getZ());
        }
        updateOffsets();
    }

    public void updateOffsets() {
        if (originalLocation == null || location == null) return;
        location.setX(originalLocation.getX());
        location.setY(originalLocation.getY());
        location.setZ(originalLocation.getZ());

        if (offset != null) location.add(offset);
        if (relativeOffset != null) location.add(VectorUtils.rotateVector(relativeOffset, location));
        if (entityOffset != null) location.add(entityOffset);
    }

    public void setUpdateLocation(boolean update) {
        updateLocation = update;
    }

    public void update() {
        if (location == null || (!updateLocation && !updateDirection)) return;

        Entity entityReference = entity == null ? null : entity.get();
        if (entityReference == null) return;

        Location currentLocation = getEntityLocation(entityReference);
        if (updateDirection) setDirection(currentLocation.getDirection());
        if (updateLocation) updateFrom(currentLocation);
    }

    public void setUpdateDirection(boolean updateDirection) {
        this.updateDirection = updateDirection;
    }

    public void setDirectionOffset(float yawOffset, float pitchOffset) {
        this.pitchOffset = pitchOffset;
        this.yawOffset = yawOffset;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public boolean hasValidEntity() {
        Entity entity = this.getEntity();
        return entity != null && entity.isValid();
    }

}
