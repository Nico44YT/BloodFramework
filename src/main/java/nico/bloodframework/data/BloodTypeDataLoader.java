package nico.bloodframework.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import nico.bloodframework.Bloodframework;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BloodTypeDataLoader implements SimpleSynchronousResourceReloadListener {
    private static BloodTypeDataLoader instance;
    private static final Gson GSON = new Gson();

    // Looks for data/<namespace>/blood_type/*.json
    private static final String DIRECTORY = "blood_type";

    private final Map<Identifier, LoadedBloodTypeContainer> data = new HashMap<>();

    public BloodTypeDataLoader() {
        super();
        instance = this;
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new BloodTypeDataLoader());
    }

    @Override
    public Identifier getFabricId() {
        return Bloodframework.id("blood_type_loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        data.clear();

        Map<Identifier, Resource> resources = manager.findResources(
                DIRECTORY,
                id -> id.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier id = entry.getKey();

            try (InputStreamReader reader =
                         new InputStreamReader(entry.getValue().getInputStream())) {

                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                // Remove ".json" from the path
                String path = id.getPath()
                        .substring(DIRECTORY.length() + 1)
                        .replaceAll("\\.json$", "");

                String key = id.getNamespace() + ":" + path;
                data.put(Identifier.tryParse(key), LoadedBloodTypeContainer.fromJson(Identifier.tryParse(key), json));

            } catch (Exception e) {
                System.err.println("Failed to load " + id);
                e.printStackTrace();
            }
        }
    }

    public static Map<Identifier, LoadedBloodTypeContainer> getData() {
        return instance.data;
    }
}