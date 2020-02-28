package at.sf.FileConfiguration;

import com.google.gson.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //new JsonObject()
        System.out.println(System.getProperty("user.dir"));
        FileConfiguration conf = new FileConfiguration("test.json", true, true);
        conf.clear();

        conf.set("a.b.c.testPoint", new Point(1, 2));
        Point p = conf.get("a.b.c.testPoint", Point.class);
        System.out.println("Point p = " + p);

        conf.set("a.b.testString", "teeesssttt");
        System.out.println("conf.getString(\"a.b.testString\") = " + conf.getString("a.b.testString"));
        System.out.println("conf.get(\"a.b.testString\", String.class) = " + conf.get("a.b.testString", String.class));

        JsonObject o = conf.getJsonObject("a.b");
        System.out.println(String.format("---\nJsonObject o = \n%s\n---", gson.toJson(o)));
        System.out.println("o.get(\"testString\").getAsString() = " + o.get("testString").getAsString());

        JsonPrimitive primitive = conf.getJsonPrimitive("a.b.testString");
        System.out.println("primitive.getAsString() = " + primitive.getAsString());

        conf.set("a.b.testInt", 42);
        int i = conf.getInt("a.b.testInt");
        System.out.println("int i = " + i);

        conf.set("a.b.testDouble", 10 / 3.0);
        double d = conf.getDouble("a.b.testDouble");
        System.out.println("double d = " + d);

        conf.set("a.b.testBoolean", true);
        boolean b = conf.getBoolean("a.b.testBoolean");
        System.out.println("boolean b = " + b);

        conf.set("a.b.testChar", 'A');
        char c = conf.getJsonPrimitive("a.b.testChar").getAsCharacter();
        System.out.println("char c = " + c);
        String cs = conf.getString("a.b.testChar");
        System.out.println("String cs = " + cs);

    }
}
