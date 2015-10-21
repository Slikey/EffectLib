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
    private final WeakReference<Entity> entity;
    private Vector offset;
    private Vector entityOffset;
    private boolean appliedOffset = false;
    private boolean updateLocation = true;
    private boolean updateDirection = true;

    public DynamicLocation(Location location) {
        if (location != null) {
            this.location = location.clone();
        } else {
            this.location = null;
        }
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
        } else {
            this.entity = null;
        }
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public void addOffset(Vector offset) {
        if (this.offset == null) {
            this.offset = offset.clone();
        } else {
            this.offset.add(offset);
        }
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
    }

    public void updateFrom(Location newLocation) {
        location.setX(newLocation.getX());
        location.setY(newLocation.getY());
        location.setZ(newLocation.getZ());
        if (offset != null) {
            location.add(offset);
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
                location.setDirection(currentLocation.getDirection());
            }
            if (updateLocation)
            {
                if (entityOffset != null) {
                    currentLocation.add(entityOffset);
                } else {
                    entityOffset = location.toVector().subtract(currentLocation.toVector());
                    currentLocation = location;
                }
                if (offset != null && !appliedOffset) {
                    // Apply the offset once initially, fold it into the entityOffset after that.
                    currentLocation.add(offset);
                    entityOffset.add(offset);
                    appliedOffset = true;
                }
                location.setX(currentLocation.getX());
                location.setY(currentLocation.getY());
                location.setZ(currentLocation.getZ());
            }
        } else if (!appliedOffset && offset != null) {
            // Only offset a fixed location once!
            location.add(offset);
            appliedOffset = true;
        }
    }

    public void setUpdateDirection(boolean updateDirection) {
        this.updateDirection = updateDirection;
    }
}
