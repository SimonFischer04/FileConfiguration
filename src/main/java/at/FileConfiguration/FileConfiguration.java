package at.FileConfiguration;

import com.google.gson.*;

import java.io.*;

public class FileConfiguration {
    private final File file;
    private final String jsonString;
    private JsonObject jsonObject;
    private Gson gson;

    private final boolean prettyPrint, autoSave;

    private FileConfiguration(File file, String jsonString, boolean prettyPrint, boolean autoSave) {
        this.file = file;
        this.jsonString = jsonString;
        this.prettyPrint = prettyPrint;
        this.autoSave = autoSave;

        load();
    }

    public FileConfiguration(File file, boolean prettyPrint, boolean autoSave) {
        this(file, null, prettyPrint, autoSave);
    }

    public FileConfiguration(File file) {
        this(file, true, true);
    }


    public FileConfiguration(String jsonString, boolean prettyPrint, boolean autoSave) {
        this(null, jsonString, prettyPrint, autoSave);
    }

    public FileConfiguration(String jsonString) {
        this(jsonString, true, true);
    }

    public static FileConfiguration fromPath(String path, boolean prettyPrint, boolean autoSave) {
        return new FileConfiguration(new File(path), prettyPrint, autoSave);
    }

    public static FileConfiguration fromPath(String path) {
        return FileConfiguration.fromPath(path, true, true);
    }

    public void load() {
        StringBuilder jsonBuilder = new StringBuilder();
        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                reader.lines().forEach(jsonBuilder::append);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            jsonBuilder.append(jsonString);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        gson = gsonBuilder.create();
        jsonObject = new Gson().fromJson(jsonBuilder.toString(), JsonObject.class);
    }

    public boolean contains(String key) {
        return getJsonPrimitive(key) != null;
    }

    public <T> T get(String key, Class<T> classOfT) {
        return gson.fromJson(getString(key), classOfT);
    }

    public String getString(String key) {
        JsonPrimitive p = getJsonPrimitive(key);
        if (p == null)
            return null;
        return p.getAsString();
    }

    public Integer getInt(String key) {
        return getJsonPrimitive(key).getAsInt();
    }

    public Double getDouble(String key) {
        return getJsonPrimitive(key).getAsDouble();
    }

    public Boolean getBoolean(String key) {
        return getJsonPrimitive(key).getAsBoolean();
    }

    public JsonObject getJsonObject(String key) {
        String[] subTypes = key.split("\\.");
        JsonObject object = jsonObject;
        for (String subType : subTypes) {
            if (!object.has(subType) && !(object.get(subType) instanceof JsonObject)) {
                return null;
            }
            object = object.get(subType).getAsJsonObject();
        }
        return object;
    }

    public JsonPrimitive getJsonPrimitive(String key) {
        String[] subTypes = key.split("\\.");
        JsonObject object = (subTypes.length == 1 ? jsonObject : getJsonObject(key.substring(0, key.lastIndexOf("."))));
        return gson.fromJson(object.get(subTypes[subTypes.length - 1]), JsonPrimitive.class);
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

        /*
            Store as right type
         */
        if (value instanceof Number) {
            currObject.addProperty(subtypes[subtypes.length - 1], (Number) value);
        } else if (value instanceof String) {
            currObject.addProperty(subtypes[subtypes.length - 1], (String) value);
        } else if (value instanceof Boolean) {
            currObject.addProperty(subtypes[subtypes.length - 1], (Boolean) value);
        } else if (value instanceof Character) {
            currObject.addProperty(subtypes[subtypes.length - 1], (Character) value);
        } else {
            currObject.addProperty(subtypes[subtypes.length - 1], gson.toJson(value));
        }

        if (autoSave) {
            save();
        }
    }

    public void save(String path) {
        if (path == null && file == null)
            throw new RuntimeException("If you want to save a configuration that was created using a json String, you must specify a File path");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter((file != null ? file.getAbsolutePath() : path)));
            bw.write(gson.toJson(jsonObject));
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        save(null);
    }

    public void clear() {
        jsonObject = new JsonObject();
        if (autoSave) {
            save();
        }
    }
}
