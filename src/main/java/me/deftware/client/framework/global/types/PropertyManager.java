package me.deftware.client.framework.global.types;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

/**
 * @author Deftware
 */
public class PropertyManager<T extends IProperty> {

    private final Int2ObjectArrayMap<T> properties = new Int2ObjectArrayMap<>();

    private boolean active = false;

    public PropertyManager<T> register(T property) {
        if (Boolean.getBoolean("emc.debugProps")) System.out.println("[EMC-DEBUG] register " + property.getId() + " " + net.minecraft.core.registries.BuiltInRegistries.BLOCK.byId(property.getId()));
        this.properties.put(property.getId(), property);
        return this;
    }

    public void clear() {
        this.properties.clear();
    }

    public int size() {
        return properties.size();
    }

    public T get(int id) {
        return properties.get(id);
    }

    public T remove(int id) {
        return properties.remove(id);
    }

    public boolean contains(int propertyId) {
        return this.properties.containsKey(propertyId);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (Boolean.getBoolean("emc.debugProps")) {
            StringBuilder sb = new StringBuilder();
            for (int id : properties.keySet()) sb.append(id).append('=').append(net.minecraft.core.registries.BuiltInRegistries.BLOCK.byId(id)).append(' ');
            System.out.println("[EMC-DEBUG] " + getClass().getSimpleName() + " active=" + active + " size=" + properties.size() + " -> " + sb);
        }
    }

}
