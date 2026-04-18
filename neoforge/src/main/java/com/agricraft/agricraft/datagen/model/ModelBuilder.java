package com.agricraft.agricraft.datagen.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelBuilder<T extends ModelBuilder<T>> {
    protected final Identifier outputLocation;
    private String parent;
    private final Map<String, String> textures = new LinkedHashMap<>();

    public ModelBuilder(Identifier outputLocation) {
        this.outputLocation = outputLocation;
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    public T parent(String parent) {
        this.parent = parent;
        return self();
    }

    public T texture(String key, String texture) {
        this.textures.put(key, texture);
        return self();
    }

    public Identifier outputLocation() {
        return this.outputLocation;
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        if (this.parent != null && !this.parent.isEmpty()) {
            root.addProperty("parent", this.parent);
        }

        if (!this.textures.isEmpty()) {
            JsonObject texturesJson = new JsonObject();
            for (Map.Entry<String, String> entry : this.textures.entrySet()) {
                texturesJson.addProperty(entry.getKey(), entry.getValue());
            }
            root.add("textures", texturesJson);
        }

        return root;
    }
}
