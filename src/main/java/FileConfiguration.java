import com.google.gson.*;

import java.io.*;

public class FileConfiguration {
    private String path;
    private JsonObject jsonObject;
    private Gson gson;

    private boolean prettyPrint, autoSave;

    public FileConfiguration(String path, boolean prettyPrint, boolean autoSave) {
        this.path = path;
        this.prettyPrint = prettyPrint;
        this.autoSave = autoSave;

        load();
    }

    public FileConfiguration(String path) {
        this(path, true, true);
    }

    public void load() {
        File f = new File(path);
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            reader.lines().forEach(jsonBuilder::append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        gson = gsonBuilder.create();
        jsonObject = new Gson().fromJson(jsonBuilder.toString(), JsonObject.class);
    }

    public boolean contains(String key) {
        return get(key, String.class) != null;
    }

    public <T> T get(String key, Class<T> classOfT) {
        String[] subTypes = key.split("\\.");
        JsonObject object = jsonObject;
        for (int i = 0; i < subTypes.length - 1; i++) {
            if (!object.has(subTypes[i]) && !(object.get(subTypes[i]) instanceof JsonObject)) {
                return null;
            }
            object = object.get(subTypes[i]).getAsJsonObject();
        }
        return gson.fromJson(object.get(subTypes[subTypes.length - 1]), classOfT);
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public Integer getInt(String key) {
        return get(key, int.class);
    }

    public Double getDouble(String key) {
        return get(key, double.class);
    }

    public JsonPrimitive getAsJsonPrimitive(String key) {
        return get(key, JsonPrimitive.class);
    }

    public void set(String key, Object value) {
        String[] subtypes = key.split("\\.");
        JsonObject currObject = jsonObject;
        for (int i = 0; i < subtypes.length - 1; i++) {
            if (!currObject.has(subtypes[i])) {
                currObject.add(subtypes[i], new JsonObject());
            }
            if (!(currObject.get(subtypes[i]) instanceof JsonObject)) { //ERROR: key already exists, but is not "JsonObject"
                StringBuilder b = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    b.append(subtypes[j]);
                    if (j < i) {
                        b.append(".");
                    }
                }
                throw new RuntimeException(String.format("Key: \"%s\" already exists as type(String, int, ...)", b));
            }
            currObject = currObject.get(subtypes[i]).getAsJsonObject();
        }
        currObject.addProperty(subtypes[subtypes.length - 1], gson.toJson(value));

        if(autoSave){
            save();
        }
    }

    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(gson.toJson(jsonObject));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
