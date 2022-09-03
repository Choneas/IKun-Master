package cn.feng.ikun.ui.gui.clickgui;

import cn.feng.ikun.Client;
import cn.feng.ikun.module.Module;
import cn.feng.ikun.module.Type;
import cn.feng.ikun.ui.font.HFontRenderer;
import cn.feng.ikun.utils.render.Opacity;
import cn.feng.ikun.utils.render.RenderUtils;
import cn.feng.ikun.value.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class AzlipsClickGUI extends GuiScreen implements GuiYesNoCallback {
    public static float scale = 1f;
    private final Opacity opacity = new Opacity(0);
    private final HFontRenderer LogoFont = Client.instance.fontLoaders.get("baloo", 20);
    ScaledResolution sr;
    private Type currentModuleType = Type.COMBAT;
    private Module currentModule = Client.instance.moduleManager.getModuleInCategory(currentModuleType).size() != 0
            ? Client.instance.moduleManager.getModuleInCategory(currentModuleType).get(0)
            : null;
    private float startX = 100, startY = 85;
    private int moduleStart = 0;
    private int valueStart = 0;
    private boolean previousMouse = true;
    private boolean mouse;
    private float moveX = 0, moveY = 0;
    private int animationHeight = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        sr = new ScaledResolution(mc);

        if (sr.getScaledHeight() > 420 && sr.getScaledWidth() > 570) {
            scale = 1.0f;
        } else {
            scale = 0.8f;
        }
        RenderUtils.drawImage(new ResourceLocation("ikun/imgs/background.png"), 0, 0, (int) (sr.getScaledWidth() * (1 / scale)), (int) (sr.getScaledHeight() * (1 / scale)));
        if (isHovered(startX - 40, startY, startX + 280, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
            this.previousMouse = true;
        } else if (moveX != 0 || moveY != 0) {
            moveX = 0;
            moveY = 0;
        }
        HFontRenderer font = Client.instance.fontLoaders.get("baloo", 19);
        int opacityX = 255;
        this.opacity.interpolate((float) opacityX);
        RenderUtils.drawRoundedRect2(startX - 40, startY, startX + 280, startY + 245, 4,
                new Color(49, 52, 57, (int) opacity.getOpacity()).getRGB());
        RenderUtils.drawRoundedRect2(startX + 170, startY, startX + 300, startY + 245, 4,
                new Color(64, 68, 75, (int) opacity.getOpacity()).getRGB());
        LogoFont.drawString(Client.instance.name, startX - 20, startY + 10, new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
        font.drawString(Client.instance.ver, startX + 5, startY + 25, new Color(200, 200, 200, (int) opacity.getOpacity()).getRGB());
        for (int i = 0; i < Type.values().length; i++) {
            Type[] iterator = Type.values();
            if (iterator[i] == currentModuleType) {
                int finishHeight = i * 30;
                RenderUtils.drawRoundedRect2(startX - 40, startY + 50 + animationHeight, startX + 55, startY + 75 + animationHeight, 7, new Color(66, 134, 245).getRGB());
                if (animationHeight < finishHeight) {
                    if (finishHeight - animationHeight < 30) {
                        animationHeight += 2;
                    } else {
                        animationHeight += 4;
                    }
                } else if (animationHeight > finishHeight) {
                    if (animationHeight - finishHeight < 30) {
                        animationHeight -= 2;
                    } else {
                        animationHeight -= 4;
                    }
                }
                if (animationHeight == finishHeight) {
                    font.drawString(iterator[i].name(), startX - 8, startY + 60 + i * 30, new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
                } else {
                    RenderUtils.drawRect(startX - 20, startY + 50 + i * 30, startX + 60, startY + 75 + i * 30, new Color(255, 255, 255, 0).getRGB());
                    font.drawString(iterator[i].name(), startX - 8, startY + 60 + i * 30, new Color(196, 196, 196, (int) opacity.getOpacity()).getRGB());
                }
            } else {
                RenderUtils.drawRect(startX - 20, startY + 50 + i * 30, startX + 60, startY + 75 + i * 30, new Color(255, 255, 255, 0).getRGB());
                font.drawString(iterator[i].name(), startX - 8, startY + 60 + i * 30, new Color(196, 196, 196, (int) opacity.getOpacity()).getRGB());
            }
            try {
                if (this.isCategoryHovered(startX - 40, startY + 50 + i * 30, startX + 60, startY + 75 + i * 40, mouseX,
                        mouseY) && Mouse.isButtonDown(0)) {
                    currentModuleType = iterator[i];
                    currentModule = Client.instance.moduleManager.getModuleInCategory(currentModuleType).size() != 0
                            ? Client.instance.moduleManager.getModuleInCategory(currentModuleType).get(0)
                            : null;
                    moduleStart = 0;
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        int m = Mouse.getDWheel();
        if (this.isCategoryHovered(startX + 68, startY, startX + 169, startY + 245, mouseX, mouseY)) {
            if (m < 0 && moduleStart < Client.instance.moduleManager.getModuleInCategory(currentModuleType).size() - 1) {
                moduleStart++;
            }
            if (m > 0 && moduleStart > 0) {
                moduleStart--;
            }
        }
        if (this.isCategoryHovered(startX + 170, startY, startX + 300, startY + 245, mouseX, mouseY)) {
            if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
                valueStart++;
            }
            if (m > 0 && valueStart > 0) {
                valueStart--;
            }
        }
        Client.instance.fontLoaders.get("poppins", 13).drawString(
                currentModule == null ? currentModuleType.name()
                        : currentModuleType.name() + " - " + currentModule.getName() + " - " + currentModule.getDesc(),
                startX + 70, startY + 12, new Color(255, 255, 255).getRGB());
        if (currentModule != null) {
            float mY = startY + 30;
            for (int i = 0; i < Client.instance.moduleManager.getModuleInCategory(currentModuleType).size(); i++) {
                Module module = Client.instance.moduleManager.getModuleInCategory(currentModuleType).get(i);
                if (mY > startY + 220)
                    break;
                if (i < moduleStart) {
                    continue;
                }

                RenderUtils.drawRect(startX + 75, mY, startX + 185, mY + 2,
                        new Color(246, 246, 246, 0).getRGB());
                if (isSettingsButtonHovered(startX + 70, mY + 2,
                        startX + 165,
                        mY + 20, mouseX, mouseY)) {
                    if (module.isToggle()) {
                        RenderUtils.drawBorderRect(startX + 70, mY + 2, startX + 165, mY + 20, 3, new Color(74, 78, 85).getRGB());
                        font.drawString(module.getName(), startX + 80, mY + 8,
                                new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB(), false);
                    } else {
                        RenderUtils.drawBorderRect(startX + 70, mY + 2, startX + 165, mY + 20, 3, new Color(60, 64, 70).getRGB());
                        font.drawString(module.getName(), startX + 80, mY + 8,
                                new Color(107, 107, 107, (int) opacity.getOpacity()).getRGB(), false);
                    }
                } else {
                    if (module.isToggle()) {
                        RenderUtils.drawBorderRect(startX + 70, mY + 2, startX + 165, mY + 20, 3, new Color(64, 68, 75).getRGB());
                        font.drawString(module.getName(), startX + 80, mY + 8,
                                new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB(), false);
                    } else {
                        RenderUtils.drawBorderRect(startX + 70, mY + 2, startX + 165, mY + 20, 3, new Color(55, 59, 66).getRGB());
                        font.drawString(module.getName(), startX + 80, mY + 8,
                                new Color(107, 107, 107, (int) opacity.getOpacity()).getRGB(), false);
                    }
                }


                if (isSettingsButtonHovered(startX + 75, mY,
                        startX + 100 + (font.getStringWidth(module.getName())),
                        mY + 8 + font.FONT_HEIGHT, mouseX, mouseY)) {
                    if (!this.previousMouse && Mouse.isButtonDown(0)) {
                        module.onToggle();
                        previousMouse = true;
                    }
                    if (!this.previousMouse && Mouse.isButtonDown(1)) {
                        previousMouse = true;
                    }
                }

                if (!Mouse.isButtonDown(0)) {
                    this.previousMouse = false;
                }
                if (isSettingsButtonHovered(startX + 70, mY,
                        startX + 165,
                        mY + 8 + font.FONT_HEIGHT, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    currentModule = module;
                    valueStart = 0;
                }
                mY += 20;
            }
            mY = startY + 30;

            for (int i = 0; i < currentModule.getValues().size(); i++) {
                if (mY > startY + 220)
                    break;
                if (i < valueStart) {
                    continue;
                }
                Value value = currentModule.getValues().get(i);
                if (value instanceof BoolValue) {
                    BoolValue boolValue = (BoolValue) value;
                    float x = startX + 190;
                    font.drawString(boolValue.getName(), x - 10, mY, new Color(255, 255, 255).getRGB());
                    if (boolValue.getValue()) {
                        RenderUtils.drawRoundedRect2(x + 80, mY, x + 100, mY + 10, 4, new Color(66, 134, 245).getRGB());
                        RenderUtils.circle(x + 96, mY + 5, 4, new Color(255, 255, 255).getRGB());
                    } else {
                        RenderUtils.drawRoundedRect2(x + 80, mY, x + 100, mY + 10, 4, new Color(114, 118, 125).getRGB());
                        RenderUtils.circle(x + 84, mY + 5, 4, new Color(164, 168, 175).getRGB());
                    }
                    if (this.isCheckBoxHovered(x + 80, mY, x + 100, mY + 9, mouseX, mouseY)) {
                        if (!this.previousMouse && Mouse.isButtonDown(0)) {
                            mc.thePlayer.playSound("random.click", 1, 1);
                            this.previousMouse = true;
                            this.mouse = true;
                        }

                        if (this.mouse) {
                            boolValue.setValue(!boolValue.getValue());
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousMouse = false;
                    }
                    mY += 25;
                }
                if (value instanceof ListValue) {
                    ListValue listValue = (ListValue) value;
                    float x = startX + 190;
                    font.drawString(listValue.getName(), x - 10, mY - 1, new Color(255, 255, 255).getRGB());
                    RenderUtils.drawRoundedRect2(x - 10, mY + 10, x + 75, mY + 26, 3,
                            new Color(86, 154, 255, (int) opacity.getOpacity()).getRGB());
                    font.drawString(listValue.getValue(),
                            x + 30 - ((float) font.getStringWidth(listValue.getValue()) / 2), mY + 15, -1);
                    if (this.isStringHovered(x - 10, mY + 10, x + 75, mY + 26, mouseX, mouseY)) {
                        if (Mouse.isButtonDown(0) && !this.previousMouse) {
                            mc.thePlayer.playSound("random.click", 1, 1);
                            if (listValue.getValues().length <= listValue.getModeListNumber(listValue.getValue()) + 1) {
                                listValue.setValue(listValue.getValues()[0]);
                            } else {
                                listValue.setValue(listValue.getValues()[listValue.getModeListNumber(listValue.getValue()) + 1]);
                            }
                            this.previousMouse = true;
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousMouse = false;
                        }

                    }
                    mY += 35;
                }
                if (value instanceof IntValue) {
                    IntValue IntValue = (IntValue) value;
                    float x = startX + 190;
                    double render = (68.0F
                            * ((IntValue.getValue()) - (IntValue).getMin())
                            / ((IntValue).getMax()
                            - (IntValue).getMin()));
                    RenderUtils.drawRect(x - 11, mY + 7, (float) ((double) x + 70), mY + 8,
                            (new Color(213, 213, 213, (int) opacity.getOpacity())).getRGB());
                    RenderUtils.drawRect(x - 11, mY + 7, (float) ((double) x + render + 0.5D), mY + 8,
                            (new Color(88, 182, 255, (int) opacity.getOpacity())).getRGB());
                    RenderUtils.circle((float) (x + render + 2D), mY + 7, 2, new Color(0, 144, 255).getRGB());
                    font.drawString(IntValue.getName() + ": " + IntValue.getValue(), startX + 180, mY - 5, new Color(255, 255, 255).getRGB());
                    if (!Mouse.isButtonDown(0)) {
                        this.previousMouse = false;
                    }
                    if (this.isButtonHovered(x, mY - 4, x + 100, mY + 9, mouseX, mouseY)
                            && Mouse.isButtonDown(0)) {
                        if (!this.previousMouse && Mouse.isButtonDown(0)) {
                            render = (IntValue).getMin();
                            double max = (IntValue).getMax();
                            double inc = 1;
                            double valAbs = (double) mouseX - ((double) x + 1.0D);
                            double perc = valAbs / 68.0D;
                            perc = Math.min(Math.max(0.0D, perc), 1.0D);
                            double valRel = (max - render) * perc;
                            double val = render + valRel;
                            val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                            IntValue.setValue((int) val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousMouse = false;
                        }
                    }
                    mY += 25;
                }
                if (value instanceof FloatValue) {
                    FloatValue floatValue = (FloatValue) value;
                    float x = startX + 190;
                    double render = (68.0F
                            * ((floatValue.getValue()) - (floatValue).getMin())
                            / ((floatValue).getMax()
                            - (floatValue).getMin()));
                    RenderUtils.drawRect(x - 11, mY + 7, (float) ((double) x + 70), mY + 8,
                            (new Color(213, 213, 213, (int) opacity.getOpacity())).getRGB());
                    RenderUtils.drawRect(x - 11, mY + 7, (float) ((double) x + render + 0.5D), mY + 8,
                            (new Color(88, 182, 255, (int) opacity.getOpacity())).getRGB());
                    RenderUtils.circle((float) (x + render + 2D), mY + 7, 2, new Color(0, 144, 255).getRGB());
                    font.drawString(floatValue.getName() + ": " + floatValue.getValue(), startX + 180, mY - 5, new Color(255, 255, 255).getRGB());
                    if (!Mouse.isButtonDown(0)) {
                        this.previousMouse = false;
                    }
                    if (this.isButtonHovered(x, mY - 4, x + 100, mY + 9, mouseX, mouseY)
                            && Mouse.isButtonDown(0)) {
                        if (!this.previousMouse && Mouse.isButtonDown(0)) {
                            render = (floatValue).getMin();
                            double max = (floatValue).getMax();
                            double inc = 0.1;
                            double valAbs = (double) mouseX - ((double) x + 1.0D);
                            double perc = valAbs / 68.0D;
                            perc = Math.min(Math.max(0.0D, perc), 1.0D);
                            double valRel = (max - render) * perc;
                            double val = render + valRel;
                            val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                            floatValue.setValue((float) val);
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousMouse = false;
                        }
                    }
                    mY += 25;
                }
            }

            if (mY > startY + 220)
                return;
            float x = startX + 190;
            font.drawString("Hide", x - 10, mY, new Color(255, 255, 255).getRGB());
            if (!currentModule.isVisible()) {
                RenderUtils.drawRoundedRect2(x + 80, mY, x + 100, mY + 10, 4, new Color(66, 134, 245).getRGB());
                RenderUtils.circle(x + 95, mY + 5, 4, new Color(255, 255, 255).getRGB());
            } else {
                RenderUtils.drawRoundedRect2(x + 80, mY, x + 100, mY + 10, 4, new Color(114, 118, 125).getRGB());
                RenderUtils.circle(x + 85, mY + 5, 4, new Color(164, 168, 175).getRGB());
            }
            if (this.isCheckBoxHovered(x + 80, mY, x + 100, mY + 9, mouseX, mouseY)) {
                if (!this.previousMouse && Mouse.isButtonDown(0)) {
                    mc.thePlayer.playSound("random.click", 1, 1);
                    this.previousMouse = true;
                    this.mouse = true;
                }

                if (this.mouse) {
                    currentModule.setVisible(!currentModule.isVisible());
                    this.mouse = false;
                }
            }
            if (!Mouse.isButtonDown(0)) {
                this.previousMouse = false;
            }
            mY += 25;
        }
    }

    public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {
        this.opacity.setOpacity(0);
    }

}
