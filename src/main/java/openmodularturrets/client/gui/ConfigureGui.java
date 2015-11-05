package openmodularturrets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import openmodularturrets.ModularTurrets;
import openmodularturrets.client.gui.containers.ConfigContainer;
import openmodularturrets.network.*;
import openmodularturrets.reference.ModInfo;
import openmodularturrets.tileentity.turretbase.TurretBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class ConfigureGui extends GuiContainer {
    TurretBase base;
    GuiTextField textFieldAddTrustedPlayer;
    EntityPlayer player;
    private int mouseX;
    private int mouseY;

    public ConfigureGui(InventoryPlayer inventoryPlayer, TurretBase tileEntity) {
        super(new ConfigContainer(inventoryPlayer, tileEntity));
        this.base = tileEntity;
        player = inventoryPlayer.player;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        String mobsButton = "Attack Mobs: " + (base.isAttacksMobs() ? "\u00A72Yes" : "\u00A7cNo");
        String neutralsButton = "Attack Neutrals: " + (base.isAttacksNeutrals() ? "\u00A72Yes" : "\u00A7cNo");
        String playersButton = "Attack Players: " + (base.isAttacksPlayers() ? "\u00A72Yes" : "\u00A7cNo");

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        textFieldAddTrustedPlayer = new GuiTextField(fontRenderer, 11, 99, 100, 18);
        textFieldAddTrustedPlayer.setMaxStringLength(50);
        textFieldAddTrustedPlayer.setFocused(true);

        this.buttonList.add(new GuiButton(1, x + 10, y + 20, 155, 20, mobsButton));
        this.buttonList.add(new GuiButton(2, x + 10, y + 40, 155, 20, neutralsButton));
        this.buttonList.add(new GuiButton(3, x + 10, y + 60, 155, 20, playersButton));
        this.buttonList.add(new GuiButton(4, x + 114, y + 98, 51, 20, "+"));
        this.buttonList.add(new GuiButton(5, x + 35, y + 135, 30, 20, "-"));
        this.buttonList.add(new GuiButton(6, x + 10, y + 135, 20, 20, "<<"));
        this.buttonList.add(new GuiButton(7, x + 145, y + 135, 20, 20, ">>"));

    
     this.buttonList.add(new GuiButton(8, x + 70, y + 135, 23, 20));
       this.buttonList.add(new GuiButton(9, x + 93, y + 135, 23, 20));
      this.buttonList.add(new GuiButton(10, x + 116, y + 135, 23, 20));
  
  

    public void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        textFieldAddTrustedPlayer.mouseClicked(i - guiLeft, j - guiTop, k);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (!textFieldAddTrustedPlayer.isFocused()) {
            super.keyTyped(par1, par2);
        } else {
            textFieldAddTrustedPlayer.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.id == 1) { //change Attack Mobs
            if (player.getUniqueID().toString().equals(base.getOwner())) {
                sendChangeToServerMobs(!base.isAttacksMobs());
                guibutton.displayString = "Attack Mobs: " + (!base.isAttacksMobs() ? "\u00A72Yes" : "\u00A7cNo");
            } else if (base.getTrustedPlayer(player.getUniqueID()).canChangeTargeting) {
                sendChangeToServerMobs(!base.isAttacksMobs());
                guibutton.displayString = "Attack Mobs: " + (!base.isAttacksMobs() ? "\u00A72Yes" : "\u00A7cNo");
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 2) { //change Attack Neutrals
            if (player.getUniqueID().toString().equals(base.getOwner())) {
                sendChangeToServerNeutrals(!base.isAttacksNeutrals());
                guibutton.displayString = "Attack Neutrals: " + (!base.isAttacksNeutrals() ? "\u00A72Yes" : "\u00A7cNo");
            } else if (base.getTrustedPlayer(player.getUniqueID()).canChangeTargeting) {
                sendChangeToServerNeutrals(!base.isAttacksNeutrals());
                guibutton.displayString = "Attack Neutrals: " + (!base.isAttacksNeutrals() ? "\u00A72Yes" : "\u00A7cNo");
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 3) { // change Attack Players
            if (player.getUniqueID().toString().equals(base.getOwner())) {
                sendChangeToServerPlayers(!base.isAttacksPlayers());
                guibutton.displayString = "Attack Players: " + (!base.isAttacksPlayers() ? "\u00A72Yes" : "\u00A7cNo");
            } else if (base.getTrustedPlayer(player.getUniqueID()).canChangeTargeting) {
                sendChangeToServerPlayers(!base.isAttacksPlayers());
                guibutton.displayString = "Attack Players: " + (!base.isAttacksPlayers() ? "\u00A72Yes" : "\u00A7cNo");
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 4) { //add trusted player
            if (player.getUniqueID().toString().equals(base.getOwner())) {
                if (!textFieldAddTrustedPlayer.getText().equals("") || !textFieldAddTrustedPlayer.getText().isEmpty()) {
                    base.addTrustedPlayer(textFieldAddTrustedPlayer.getText());
                    sendChangeToServerAddTrusted();
                    textFieldAddTrustedPlayer.setText("");
                    this.base.trustedPlayerIndex = 0;
                    player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord, base.zCoord);
                }
            } else if (base.getTrustedPlayer(player.getUniqueID()).admin) {
                if (!textFieldAddTrustedPlayer.getText().equals("") || !textFieldAddTrustedPlayer.getText().isEmpty()) {
                    base.addTrustedPlayer(textFieldAddTrustedPlayer.getText());
                    sendChangeToServerAddTrusted();
                    textFieldAddTrustedPlayer.setText("");
                    this.base.trustedPlayerIndex = 0;
                    player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord, base.zCoord);
                }
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 5) { //remove trusted player
            if (base.getTrustedPlayers().size() > 0) {
                if (this.base.getTrustedPlayers().get(
                        base.trustedPlayerIndex) != null && player.getUniqueID().toString().equals(base.getOwner())) {
                    sendChangeToServerRemoveTrusted();
                    base.removeTrustedPlayer(base.getTrustedPlayers().get(base.trustedPlayerIndex).getName());
                    textFieldAddTrustedPlayer.setText("");
                    this.base.trustedPlayerIndex = 0;
                    player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord, base.zCoord);
                } else if (base.getTrustedPlayer(player.getUniqueID()).admin) {
                    if (this.base.getTrustedPlayers().get(
                            base.trustedPlayerIndex) != null && this.base.getTrustedPlayers().size() > 0) {
                        sendChangeToServerRemoveTrusted();
                        base.removeTrustedPlayer(base.getTrustedPlayers().get(base.trustedPlayerIndex).getName());
                        textFieldAddTrustedPlayer.setText("");
                        this.base.trustedPlayerIndex = 0;
                        if (this.base.getTrustedPlayers().get(base.trustedPlayerIndex).uuid.equals(
                                player.getUniqueID()) && !player.getUniqueID().toString().equals(base.getOwner())) {
                            mc.displayGuiScreen(null);
                            return;
                        }
                        player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord,
                                       base.zCoord);
                    }
                } else {
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
                }
            }
        }

        if (guibutton.id == 6) { //decrease index of trusted player list
            if ((this.base.trustedPlayerIndex - 1 >= 0)) {
                this.base.trustedPlayerIndex--;
                player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord, base.zCoord);
            }
        }

        if (guibutton.id == 7) { //increase index of trusted player list
            if (!((this.base.trustedPlayerIndex + 1) > (base.getTrustedPlayers().size() - 1))) {
                this.base.trustedPlayerIndex++;
                player.openGui(ModularTurrets.instance, 6, player.worldObj, base.xCoord, base.yCoord, base.zCoord);
            }
        }

        if (guibutton.id == 8) { //change trusted player permission for GUI opening
            if (player.getUniqueID().toString().equals(base.getOwner()) && this.base.getTrustedPlayers().get(
                    base.trustedPlayerIndex) != null) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "gui",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).canOpenGUI);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).canOpenGUI ? "\u00A72Y" : "\u00A7cN";
            } else if (this.base.getTrustedPlayers().get(base.trustedPlayerIndex) != null && base.getTrustedPlayer(
                    player.getUniqueID()).admin) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "gui",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).canOpenGUI);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).canOpenGUI ? "\u00A72Y" : "\u00A7cN";
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 9) { //change trusted player permission for targeting
            if (player.getUniqueID().toString().equals(base.getOwner()) && this.base.getTrustedPlayers().get(
                    base.trustedPlayerIndex) != null) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "targeting",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).canChangeTargeting);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).canChangeTargeting ? "\u00A72Y" : "\u00A7cN";
            } else if (this.base.getTrustedPlayers().get(base.trustedPlayerIndex) != null && base.getTrustedPlayer(
                    player.getUniqueID()).admin) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "targeting",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).canChangeTargeting);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).canChangeTargeting ? "\u00A72Y" : "\u00A7cN";
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }

        if (guibutton.id == 10) { //change trusted player permission for administering
            if (player.getUniqueID().toString().equals(base.getOwner()) && this.base.getTrustedPlayers().get(
                    base.trustedPlayerIndex) != null) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "isAdmin",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).admin);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).admin ? "\u00A72Y" : "\u00A7cN";
            } else if (this.base.getTrustedPlayers().get(base.trustedPlayerIndex) != null && base.getTrustedPlayer(
                    player.getUniqueID()).admin) {
                sendChangeToServerModifyPermissions(
                        this.base.getTrustedPlayers().get(base.trustedPlayerIndex).getName(), "isAdmin",
                        !base.getTrustedPlayers().get(base.trustedPlayerIndex).admin);
                guibutton.displayString = !base.getTrustedPlayers().get(
                        base.trustedPlayerIndex).admin ? "\u00A72Y" : "\u00A7cN";
            } else {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("status.ownership")));
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        fontRenderer.drawString("Targeting options:", 10, 8, 0);
        fontRenderer.drawString("Add Trusted Player:", 10, 87, 0);

        if (this.base.getTrustedPlayers().size() == 0) {
            fontRenderer.drawString("\u00A7f<No trusted players to edit>", 10, 124, 0);
        } else {
            fontRenderer.drawString(base.getTrustedPlayers().get(base.trustedPlayerIndex).getName() + "'s Permissions:",
                                    10, 124, 0);
        }

        textFieldAddTrustedPlayer.drawTextBox();

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        // x + 114, y + 98, 51, 20
        if (mouseX > k + 114 && mouseX < k + 114 + 51) {
            if (mouseY > l + 98 && mouseY < l + 98 + 20) {
                ArrayList list = new ArrayList();
                list.add("Adds the given player to the turret's trusted list.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        if (mouseX > k + 35 && mouseX < k + 35 + 30) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("Removes the viewed trusted player from the turret's trusted player list.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        if (mouseX > k + 10 && mouseX < k + 10 + 20) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("View previous trusted player.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        if (mouseX > k + 145 && mouseX < k + 145 + 20) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("View next trusted player.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        // x + 70,y + 135,23,20,
        if (mouseX > k + 70 && mouseX < k + 70 + 23) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("If the viewed trusted player can open this turret base's gui.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        // x + 93,y + 135,23,20,
        if (mouseX > k + 93 && mouseX < k + 93 + 23) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("If the viewed trusted player can change the base's targeting parameters.");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }

        // x + 116,y + 135,23,20,
        if (mouseX > k + 116 && mouseX < k + 116 + 23) {
            if (mouseY > l + 135 && mouseY < l + 135 + 20) {
                ArrayList list = new ArrayList();
                list.add("If the viewed trusted player can administer this turret (trusted players and dropping it).");
                this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        ResourceLocation texture = (new ResourceLocation(ModInfo.ID + ":textures/gui/configure.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.mouseX = par1;
        this.mouseY = par2;
        super.drawScreen(par1, par2, par3);
    }

    public void sendChangeToServerMobs(boolean setTo) {
        ToggleAttackMobsMessage message = new ToggleAttackMobsMessage(base.xCoord, base.yCoord, base.zCoord, setTo);
        ModularTurrets.networking.sendToServer(message);
    }

    public void sendChangeToServerNeutrals(boolean setTo) {
        ToggleAttackNeutralMobsMessage message = new ToggleAttackNeutralMobsMessage(base.xCoord, base.yCoord,
                                                                                    base.zCoord, setTo);
        ModularTurrets.networking.sendToServer(message);
    }

    public void sendChangeToServerPlayers(boolean setTo) {
        ToggleAttackPlayersMessage message = new ToggleAttackPlayersMessage(base.xCoord, base.yCoord, base.zCoord,
                                                                            setTo);

        ModularTurrets.networking.sendToServer(message);
    }

    public void sendChangeToServerAddTrusted() {
        AddTrustedPlayerMessage message = new AddTrustedPlayerMessage(base.xCoord, base.yCoord, base.zCoord,
                                                                      textFieldAddTrustedPlayer.getText());

        ModularTurrets.networking.sendToServer(message);
    }

    public void sendChangeToServerRemoveTrusted() {
        RemoveTrustedPlayerMessage message = new RemoveTrustedPlayerMessage(base.xCoord, base.yCoord, base.zCoord,
                                                                            base.getTrustedPlayers().get(
                                                                                    base.trustedPlayerIndex).getName());

        ModularTurrets.networking.sendToServer(message);
    }

    public void sendChangeToServerModifyPermissions(String player, String perm, boolean canDo) {
        ModifyPermissionsMessage message = new ModifyPermissionsMessage(base.xCoord, base.yCoord, base.zCoord, player,
                                                                        perm, canDo);

        ModularTurrets.networking.sendToServer(message);
    }
}
