package de.slikey.effectlib.util;

import java.lang.ref.WeakReference;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

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

    public DynamicLocation(Location location) {
        if (location != null) {
            this.location = location.clone();
        } else {
            this.location = null;
        }
        this.originalLocation = location;
        this.entity = null;
    }

    public DynamicLocation(Entity entity) {
        if (entity != null) {
            this.entity = new WeakReference<Entity>(entity);
            this.location = getEntityLocation(entity);
        } else {
            this.entity = null;
            this.location = null;
        }
        this.originalLocation = location;
    }

    public DynamicLocation(Location location, Entity entity) {
        if (location != null) {
            this.location = location.clone();
        } else if (entity != null) {
            this.location = getEntityLocation(entity);
        } else {
            this.location = null;
        }
        if (entity != null) {
            this.entity = new WeakReference<Entity>(entity);
            this.entityOffset = this.location.toVector().subtract(getEntityLocation(entity).toVector());
        } else {
            this.entity = null;
        }
        this.originalLocation = location;
    }

    public void addOffset(Vector offset) {
        if (this.offset == null) {
            this.offset = offset.clone();
        } else {
            this.offset.add(offset);
        }
        this.updateOffsets();
    }

    public void addRelativeOffset(Vector offset) {
        if (this.relativeOffset == null) {
            this.relativeOffset = offset.clone();
        } else {
            this.relativeOffset.add(offset);
        }
        this.updateOffsets();
    }

    public Entity getEntity() {
        return entity == null ? null : entity.get();
    }

    public Location getLocation() {
        return location;
    }

    protected Location getEntityLocation(Entity entity) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).getEyeLocation();
        }
        return entity.getLocation();
    }

    public void setDirection(Vector direction) {
        location.setDirection(direction);
        updateDirectionOffsets();
    }

    protected void updateDirectionOffsets() {
        if (yawOffset != 0) {
            location.setYaw(location.getYaw() + yawOffset);
        }
        if (pitchOffset != 0) {
            location.setPitch(location.getPitch() + pitchOffset);
        }
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
        if (offset != null) {
            location.add(offset);
        }
        if (relativeOffset != null) {
            location.add(VectorUtils.rotateVector(relativeOffset, location));
        }
        if (entityOffset != null) {
            location.add(entityOffset);
        }
    }

    public void setUpdateLocation(boolean update) {
        updateLocation = update;
    }

    public void update() {
        if (location == null || (!updateLocation && !updateDirection)) {
            return;
        }

        Entity entityReference = entity == null ? null : entity.get();
        if (entityReference != null) {
            Location currentLocation = getEntityLocation(entityReference);
            if (updateDirection)
            {
                setDirection(currentLocation.getDirection());
            }
            if (updateLocation)
            {
                updateFrom(currentLocation);
            }
        }
    }

    public void setUpdateDirection(boolean updateDirection) {
        this.updateDirection = updateDirection;
    }

    public void setDirectionOffset(float yawOffset, float pitchOffset) {
        this.pitchOffset = pitchOffset;
        this.yawOffset = yawOffset;
        updateDirectionOffsets();
    }
}
