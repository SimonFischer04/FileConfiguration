package at.sf.FileConfiguration;

import com.google.gson.JsonPrimitive;

public class Main {
    public static void main(String[] args) {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //new JsonObject()
        System.out.println(System.getProperty("user.dir"));
        FileConfiguration c = new FileConfiguration("test.json", true, true);
        //c.set("test", "lol");

        //int i = c.get("a.c.d", int.class);
        JsonPrimitive p = c.getAsJsonPrimitive("a.b.c.d");
        System.out.println();

        c.set("a.b.c.ee.f", 1);
        String s = c.get("a.b.c.d", String.class);

        System.out.println();
        //c.set("a.b.c", "hallo");

        //c.save();

    }
}
