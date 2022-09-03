package cn.feng.ikun.ui.font;

public interface IBFFontRenderer
{
    HStringCache getStringCache();

    void setStringCache(HStringCache value);

    boolean isDropShadowEnabled();

    void setDropShadowEnabled(boolean value);

    boolean isEnabled();

    void setEnabled(boolean value);
}
