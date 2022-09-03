package cn.feng.ikun.module;

import cn.feng.ikun.Client;
import cn.feng.ikun.event.EventKey;
import cn.feng.ikun.module.modules.combat.*;
import cn.feng.ikun.module.modules.movement.Eagle;
import cn.feng.ikun.module.modules.movement.NoSlow;
import cn.feng.ikun.module.modules.movement.Speed;
import cn.feng.ikun.module.modules.movement.Sprint;
import cn.feng.ikun.module.modules.player.*;
import cn.feng.ikun.module.modules.render.*;
import cn.feng.ikun.module.modules.world.NoCommand;
import cn.feng.ikun.utils.KtUtils;
import cn.feng.ikun.utils.misc.LogUtils;
import com.darkmagician6.eventapi.EventTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Client.instance.eventManager.register(this);
        init();
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        return modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }

    public List<Module> getModuleInCategory(Type type) {
        return KtUtils.INSTANCE.sortModulesByName(modules.stream().filter(it -> it.getType() == type).collect(Collectors.toList()));
    }

    public ArrayList<Module> getEnabledModList() {
        ArrayList<Module> enabledModList = new ArrayList<>();
        for (Module m : modules) {
            if (m.isToggle()) {
                enabledModList.add(m);
            }
        }
        return enabledModList;
    }

    private void init() {
        LogUtils.info("Loading modules...");
        modules.add(new Sprint());
        modules.add(new FullBright());
        modules.add(new HUD());
        modules.add(new ClickGUI());
        modules.add(new AutoClicker());
        modules.add(new AimBot());
        modules.add(new Velocity());
        modules.add(new Reach());
        modules.add(new Speed());
        modules.add(new AntiBots());
        modules.add(new Eagle());
        modules.add(new FastPlace());
        modules.add(new NoSlow());
        modules.add(new Critical());
        modules.add(new HitBox());
        modules.add(new Trigger());
        modules.add(new AutoFill());
        modules.add(new NoCommand());
        modules.add(new BowAimBot());
        modules.add(new Ambience());
        modules.add(new BlockAnimation());
        modules.add(new ESP());
        modules.add(new Teams());
        modules.add(new Tracers());
        modules.add(new MusicPlayer());
        modules.add(new AutoMLG());
        modules.add(new AutoPlace());
        modules.add(new AutoSword());
        modules.add(new ItemPhysic());
        modules.add(new NameTags());

        modules = KtUtils.INSTANCE.sortModulesByName(modules);
        LogUtils.info("Loaded " + modules.size() + " modules.");
    }

    @EventTarget
    public void onKey(EventKey e) {
        modules.stream().filter(it -> it.getKey() == e.getKey()).forEach(Module::onToggle);
    }
}
