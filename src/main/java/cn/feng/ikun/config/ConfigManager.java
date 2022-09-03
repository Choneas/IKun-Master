package cn.feng.ikun.config;

import cn.feng.ikun.Client;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.ui.gui.altmanager.Alt;
import cn.feng.ikun.utils.misc.FileUtils;
import cn.feng.ikun.utils.misc.LogUtils;
import cn.feng.ikun.utils.misc.MC;
import cn.feng.ikun.utils.misc.RandomUtil;
import cn.feng.ikun.utils.time.TimeHelper;
import cn.feng.ikun.value.*;
import com.google.gson.*;

import java.io.*;
import java.util.Map;

public class ConfigManager extends MC {
    private final File root = new File(mc.mcDataDir, Client.instance.name);
    private final File configFile = new File(root, "config.json");

    public ConfigManager() {
        if (!root.exists()) {
            root.mkdirs();
        }
        load();
        Thread t = new Thread(() -> {
            TimeHelper helper = new TimeHelper();
           while (true) {
               if (helper.timePassed(10000) && !Client.instance.isLoading) {
                   save();
                   helper.reset();
               } else {
                   try {
                       Thread.sleep(5000);
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
               }
           }
        });
        t.setName("Config-Save Thread");
        t.start();
    }

    public void load() {
        try {
            if (!configFile.exists()) {
                LogUtils.info("Skipping loading config because the config file wasn't exists.");
                return;
            }
            JsonObject configObject = new JsonParser().parse(new BufferedReader(new FileReader(configFile))).getAsJsonObject();
            if (configObject.has("modules")) {
                JsonObject modulesObject = configObject.get("modules").getAsJsonObject();
                for (Module module : Client.instance.moduleManager.modules) {
                    if (modulesObject.has(module.getName())) {
                        JsonObject moduleObject = modulesObject.get(module.getName()).getAsJsonObject();
                        if (moduleObject.has("toggle")) {
                            module.setToggle(moduleObject.get("toggle").getAsBoolean());
                        }
                        if (moduleObject.has("key")) {
                            module.setKey(moduleObject.get("key").getAsInt());
                        }
                        if (moduleObject.has("visible")) {
                            module.setVisible(moduleObject.get("visible").getAsBoolean());
                        }


                        if (moduleObject.has("values")) {
                            JsonObject valuesObject = moduleObject.get("values").getAsJsonObject();
                            for (Value value : module.getValues()) {
                                if (valuesObject.has(value.getName())) {
                                    if (value instanceof BoolValue) {
                                        ((BoolValue) value).setValue(valuesObject.get(value.getName()).getAsBoolean());
                                    } else if (value instanceof IntValue) {
                                        ((IntValue) value).setValue(valuesObject.get(value.getName()).getAsInt());
                                    } else if (value instanceof FloatValue) {
                                        ((FloatValue) value).setValue(valuesObject.get(value.getName()).getAsFloat());
                                    } else if (value instanceof ListValue) {
                                        ((ListValue) value).setValue(valuesObject.get(value.getName()).getAsString());
                                    }
                                }
                            }
                        }
                    } else {
                        LogUtils.info("Skipping loading module " + module.getName() + "'s config because of the broken config file.");
                    }
                }
            } else {
                LogUtils.info("Skipping loading config because of the broken config file.");
            }

            // Alts
            if (configObject.has("alts")) {
                JsonObject altsObject = configObject.getAsJsonObject("alts");
                for (Map.Entry<String, JsonElement> altObject : altsObject.entrySet()) {
                    if (altObject.getValue() instanceof JsonObject) {
                        JsonObject object = altObject.getValue().getAsJsonObject();
                        if (object.has("username") && object.has("password") && object.has("mask")) {
                            Alt a = new Alt(object.get("username").getAsString(), object.get("password").getAsString(), object.get("mask").getAsString());
                            Client.instance.altManager.addAlt(a);
                            if (altObject.getKey().equals("last")) {
                                Client.instance.altManager.setLastAlt(a);
                            }
                        }
                    }
                }
            } else {
                LogUtils.info("Skipping loading alts because of the broken config file.");
            }

            // Friends
            if (configObject.has("friends")) {
                JsonObject friendsObject = configObject.getAsJsonObject("friends");
                for (Map.Entry<String, JsonElement> entry : friendsObject.entrySet()) {
                    Client.instance.friendManager.addFriend(entry.getKey());
                }
            } else {
                LogUtils.info("Skipping loading friends because of the broken config file.");
            }
        } catch (Exception e) {
            LogUtils.info("Failed to load config.");
        }

        Client.instance.moduleManager.modules.stream().filter(Module::isToggle).forEach(it -> Client.instance.eventManager.register(it));
    }

    public void save() {
        JsonObject configObject = new JsonObject();

        // Modules
        JsonObject modulesObject = new JsonObject();
        for (Module m : Client.instance.moduleManager.modules) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("toggle", m.isToggle());
            moduleObject.addProperty("key", m.getKey());
            moduleObject.addProperty("visible", m.isVisible());
            JsonObject valuesObject = new JsonObject();
            for (Value v : m.getValues()) {
                if (v instanceof BoolValue) {
                    valuesObject.addProperty(v.getName(), ((BoolValue) v).getValue());
                } else if (v instanceof IntValue) {
                    valuesObject.addProperty(v.getName(), ((IntValue) v).getValue());
                } else if (v instanceof FloatValue) {
                    valuesObject.addProperty(v.getName(), ((FloatValue) v).getValue());
                } else if (v instanceof ListValue) {
                    valuesObject.addProperty(v.getName(), ((ListValue) v).getValue());
                }
            }
            moduleObject.add("values", valuesObject);
            modulesObject.add(m.getName(), moduleObject);
        }

        // Alts

        JsonObject altsObject = new JsonObject();
        for (Alt a : Client.instance.altManager.alts) {
            JsonObject altObject = new JsonObject();
            altObject.addProperty("username", a.getUsername());
            altObject.addProperty("password", a.getPassword());
            altObject.addProperty("mask", a.getMask());
            altsObject.add(Client.instance.altManager.lastAlt == a? "last" : new RandomUtil().getStringRandom(10), altObject);
        }

        // Friends
        JsonObject friendsObject = new JsonObject();
        for (String s : Client.instance.friendManager.friends) {
            friendsObject.addProperty(s, "");
        }

        configObject.add("friends", friendsObject);
        configObject.add("alts", altsObject);
        configObject.add("modules", modulesObject);
        FileUtils.INSTANCE.write(configFile, configObject);
        LogUtils.info("Saved config.");
    }
}
