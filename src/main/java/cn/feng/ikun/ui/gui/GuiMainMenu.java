package cn.feng.ikun.ui.gui;


import cn.feng.ikun.Client;
import cn.feng.ikun.ui.elements.GLSLSandboxShader;
import cn.feng.ikun.ui.elements.HydraButton;
import cn.feng.ikun.ui.gui.altmanager.GuiAltManager;
import cn.feng.ikun.utils.WebUtils;
import cn.feng.ikun.utils.misc.LogUtils;
import cn.feng.ikun.utils.render.NovoRenderUtils;
import cn.feng.ikun.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import oh.yalan.NativeMethod;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    public static int BUTTON_COUNT = 5;
    private final Color blackish = new Color(62, 95, 127);
    private final Color black = new Color(40, 46, 51);
    private final Color blueish = new Color(108, 0, 150);
    private final Color blue = new Color(0xFF2B71B1);
    private final Color whiteish = new Color(0xFFF4F5F8);
    private GLSLSandboxShader shader;
    public float totalHeight = BUTTON_COUNT * 20 + 20 + BUTTON_COUNT * 3;
    public float halfTotalHeight = totalHeight / 2, fraction = 0;
    public boolean darkTheme = true, guiWasSwitched;
    public int alpha = 0;
    private float hHeight = 540;
    private float hWidth = 960;
    HydraButton singleplayerworlds = new HydraButton(1, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 10, 140, 20, "Singleplayer");
    HydraButton multiplayer = new HydraButton(2, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 33, 140, 20, "Multiplayer");
    HydraButton altrepository = new HydraButton(1337, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 56, 140, 20, "Alt Repository");
    HydraButton options = new HydraButton(0, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 79, 140, 20, "Options");
    HydraButton shutdown = new HydraButton(4, (int) hWidth - 70, (int) hHeight - (int) halfTotalHeight + 102, 140, 20, "Shutdown");
    private long initTime;
    private int mode = 0;
    public GuiMainMenu() {
        try {
            this.shader = new GLSLSandboxShader("/assets/minecraft/ikun/shaders/menu" + mode + ".fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }
        initTime = System.currentTimeMillis();
    }

    @NativeMethod
    public void checkUpdate() throws IOException {
        if (!Client.instance.ver.equals(WebUtils.get("http://ikunclient.vus.tax/version.txt"))) {
            System.out.println(WebUtils.get("http://ikunclient.vus.tax/version.txt"));
            JOptionPane.showMessageDialog(null, "Your client is outdated. Go to ikunclient.svc.vin to get the latest version.", "Update Available!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void initGui() {
        try {
            checkUpdate();
        } catch (IOException e) {
            try {
                checkUpdate();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to verify.");
                System.exit(114514);
            }
        }
        buttonList.add(singleplayerworlds);
        buttonList.add(multiplayer);
        buttonList.add(altrepository);
        buttonList.add(options);
        buttonList.add(shutdown);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        hWidth = scaledResolution.getScaledWidth() / 2f;
        alpha = 100;

        if (!guiWasSwitched) {
            guiWasSwitched = true;
        }

        darkTheme = true;
        initTime = System.currentTimeMillis();
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (guiWasSwitched && darkTheme && fraction != 1.0049993F) {
            fraction = 1.0049993F;
        }

        if (darkTheme && fraction < 1) {
            fraction += 0.015;
        } else if (!darkTheme && fraction > 0) {
            fraction -= 0.015;
        }

        if (mouseX <= 20 && mouseY <= 20 && alpha < 255) {
            alpha++;
        } else if (alpha > 100) {
            alpha--;
        }

        GlStateManager.disableCull();
        singleplayerworlds.setColor(interpolateColor(blue, blueish, fraction));
        multiplayer.setColor(interpolateColor(blue, blueish, fraction));
        altrepository.setColor(interpolateColor(blue, blueish, fraction));
        options.setColor(interpolateColor(blue, blueish, fraction));
        shutdown.setColor(interpolateColor(blue, blueish, fraction));

        singleplayerworlds.setColor(interpolateColor(
                singleplayerworlds.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                singleplayerworlds.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        multiplayer.setColor(interpolateColor(
                multiplayer.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                multiplayer.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        altrepository.setColor(interpolateColor(
                altrepository.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                altrepository.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        options.setColor(interpolateColor(
                options.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                options.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));
        shutdown.setColor(interpolateColor(
                shutdown.hovered(mouseX, mouseY) ? blue.brighter() : blue,
                shutdown.hovered(mouseX, mouseY) ? blueish.brighter() : blueish,
                fraction));


        singleplayerworlds.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 10);
        multiplayer.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 33);
        altrepository.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 56);
        options.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 79);
        shutdown.updateCoordinates(hWidth - 70, hHeight - halfTotalHeight + 102);

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaledWidthScaled = scaledResolution.getScaledWidth();
        int scaledHeightScaled = scaledResolution.getScaledHeight();
        hHeight = hHeight + (scaledHeightScaled / 2 - hHeight) * 0.02f;
        hWidth = scaledWidthScaled / 2;

        NovoRenderUtils.drawRect(0, 0, scaledWidthScaled, scaledHeightScaled, new Color(0, 0, 0, 150).getRGB());
        NovoRenderUtils.drawBorderedRect(
                hWidth - 80,
                hHeight - halfTotalHeight,
                hWidth + 80,
                hHeight + halfTotalHeight,
                0.3f,
                new Color(0, 0, 0, 50).getRGB(),
                new Color(0, 0, 0, 50).getRGB());


        // LOGO
        Client.instance.fontLoaders.get("baloo", 24).drawString(
                "IKun",
                hWidth - Client.instance.fontLoaders.get("baloo", 24).getStringWidth("IKun") / 2f,
                hHeight - halfTotalHeight - 35,
                interpolateColor(blue, blueish, fraction));

        int y = 5;
        for (String s : Client.instance.logs) {
            Client.instance.fontLoaders.get("poppins", 14).drawStringWithShadow(s, 5, y, Color.WHITE.getRGB());
            y += 2;
            y += Client.instance.fontLoaders.get("poppins", 14).FONT_HEIGHT;
        }

        RenderUtils.drawIcon(scaledResolution.getScaledWidth() - 60, scaledResolution.getScaledHeight() - 60, 50, 50, new ResourceLocation("ikun/imgs/yzw.jpg"));

        singleplayerworlds.drawButton(mc, mouseX, mouseY);
        multiplayer.drawButton(mc, mouseX, mouseY);
        altrepository.drawButton(mc, mouseX, mouseY);
        options.drawButton(mc, mouseX, mouseY);
        shutdown.drawButton(mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1337:
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;

            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;

            case 4:
                this.mc.shutdown();
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mouseX > sr.getScaledWidth() - 70 && mouseY > mc.displayHeight - sr.getScaledHeight() - 70) {
            mode++;
            if (mode == 7) {
                mode = 0;
            }
            this.shader = new GLSLSandboxShader("/assets/minecraft/ikun/shaders/menu" + mode + ".fsh");
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * fraction);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        } catch (Exception ex) {
            return 0xffffffff;
        }
    }
}