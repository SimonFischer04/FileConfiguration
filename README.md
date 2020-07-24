# FileConfiguration
This is a simple tool to easily work with GSON.

Special about this library is that you can easily create "sub-json-objects" by using dots in the key.

## Example:

conf.set("a.b.c", "test");
---
will create a config that looks like this:
{
  "a": {
    "b": {
      "c": "test"
    }
  }
}

## Of course this works the other way round:

conf.getString("a.b.c")
---
will return "test"
