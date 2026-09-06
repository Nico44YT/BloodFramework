package nico.bloodframework.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import nico.bloodframework.Bloodframework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BloodTypeDataLoader implements SimpleSynchronousResourceReloadListener {
    private static BloodTypeDataLoader instance;

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
                BloodTypeDataLoader::allowedFile
        );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier id = entry.getKey();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(entry.getValue().getInputStream()))) {

                String fullJson = cleanFromComments(reader.lines().collect(Collectors.joining("\n")));
                JsonObject json = JsonParser.parseString(fullJson).getAsJsonObject();

                // Remove file extension from the path
                String path = id.getPath().substring(DIRECTORY.length() + 1, id.getPath().length() - getFileExtension(id).length() - 1);

                Identifier key = Identifier.of(id.getNamespace(), path);
                data.put(key, LoadedBloodTypeContainer.fromJson(key, json));

            } catch (Exception e) {
                System.err.println("Failed to load " + id);
                e.printStackTrace();
            }
        }
    }

    public static Map<Identifier, LoadedBloodTypeContainer> getData() {
        return instance.data;
    }

    // https://stackoverflow.com/questions/8626866/java-removing-comments-from-string
    private static String cleanFromComments(String string) {
        return string.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
    }

    private static boolean allowedFile(Identifier fileId) {
        return fileId.getPath().endsWith(".jsonc") ||
                fileId.getPath().endsWith(".json");
    }

    private static String getFileExtension(Identifier fileId) {
        String[] parts = fileId.getPath().split("\\.");
        return parts[parts.length - 1];
    }
}