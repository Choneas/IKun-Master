package cn.feng.ikun.ui.gui.altmanager;

import java.util.ArrayList;
import java.util.List;

public class AltManager {
    public final List<Alt> alts = new ArrayList<>();
    public Alt lastAlt;
    public void setLastAlt(Alt alt) {
        lastAlt = alt;
    }
    public void addAlt(Alt alt) {
        alts.add(alt);
    }
}
