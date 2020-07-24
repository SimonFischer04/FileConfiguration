# FileConfiguration
This is a simple tool to easily work with GSON.

Special about this library is that you can easily create "sub-json-objects" by using dots in the key.

## Example:
---
- conf.set("a.b.c", "test");
  
will create a config that looks like this:
{
  "a": {
    "b": {
      "c": "test"
    }
  }
}
- conf.getString("a.b.c")

will return it ("test") again

## TODO:
---
- add Documentation/JavaDoc
