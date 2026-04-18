package com.agricraft.agricraft.datagen.model;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ModelProvider<T extends ModelBuilder<T>> implements DataProvider {
    protected final String modid;
    protected final String folder;
    private final Function<Identifier, T> builderFactory;
    private final PackOutput.PathProvider pathProvider;
    private final Map<Identifier, T> generatedModels = new LinkedHashMap<>();

    public ModelProvider(PackOutput output, String modid, String folder, Function<Identifier, T> builderFactory) {
        this.modid = modid;
        this.folder = folder;
        this.builderFactory = builderFactory;
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    public T withExistingParent(String name, String parent) {
        Identifier id = this.modelId(name);
        T builder = this.generatedModels.computeIfAbsent(id, this.builderFactory);
        return builder.parent(parent);
    }

    protected abstract void registerModels();

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.generatedModels.clear();
        this.registerModels();

        CompletableFuture<?>[] futures = this.generatedModels.values().stream()
                .map(model -> DataProvider.saveStable(cachedOutput, model.toJson(), this.modelPath(model.outputLocation())))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return "Models for: %s:%s".formatted(this.modid, this.folder);
    }

    private Identifier modelId(String name) {
        if (name.contains(":")) {
            return Identifier.parse(name);
        }
        return Identifier.fromNamespaceAndPath(this.modid, this.folder + "/" + name);
    }

    private Path modelPath(Identifier id) {
        return this.pathProvider.json(id);
    }
}
