package me.deftware.client.framework.global.types;

public class BlockPropertyManager extends PropertyManager<BlockProperty> {

    /*
     * 26.3: Aristois recarga los chunks antes de activar el X-ray y de cargar la lista
     * de bloques, así que la recompilación usaba el estado viejo. Al cambiar el estado
     * o la lista, pedimos otra recarga en el próximo tick (una sola aunque haya muchos cambios).
     */
    private volatile boolean reloadQueued = false;

    private void queueReload() {
        if (reloadQueued) return;
        reloadQueued = true;
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        mc.execute(() -> {
            reloadQueued = false;
            if (mc.level != null) mc.levelExtractor.allChanged();
        });
    }

    @Override
    public void setActive(boolean active) {
        boolean changed = active != isActive();
        super.setActive(active);
        if (changed) queueReload();
    }

    @Override
    public PropertyManager<BlockProperty> register(BlockProperty property) {
        super.register(property);
        if (isActive()) queueReload();
        return this;
    }

    /**
     * If enabled, only blocks touching air will be rendered (From selected blocks)
     */
    private boolean exposedOnly = false;

    /**
     * If enabled, non selected blocks touching cave air will not be rendered
     */
    private boolean disableCaveRendering = false;

    /**
     * Render non selected blocks with the selected opacity
     */
    private boolean opacityMode = true;

    /**
     * Opacity of which to render non selected blocks
     */
    private float opacity = 100f;

    public boolean isExposedOnly() {
        return exposedOnly;
    }

    public void setExposedOnly(boolean exposedOnly) {
        this.exposedOnly = exposedOnly;
    }

    public boolean isDisableCaveRendering() {
        return disableCaveRendering;
    }

    public void setDisableCaveRendering(boolean disableCaveRendering) {
        this.disableCaveRendering = disableCaveRendering;
    }

    public boolean isOpacityMode() {
        return opacityMode;
    }

    public void setOpacityMode(boolean opacityMode) {
        this.opacityMode = opacityMode;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

}
