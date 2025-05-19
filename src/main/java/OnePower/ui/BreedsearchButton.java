package OnePower.ui;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;
import loadout.LoadoutMod;
import loadout.relics.PowerGiver;
import loadout.screens.PowerSelectScreen;
import loadout.util.ModConfig;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static loadout.relics.AbstractCustomScreenRelic.isScreenUpMap;
import static loadout.relics.PowerGiver.getPower;
import static loadout.screens.PowerSelectScreen.dummyPlayer;

public class BreedsearchButton extends TopPanelItem {
    private static final Texture IMG = new Texture("OnePowerResources/images/relics/MyRelic.png");
    public static final String ID = "OnePower:Button";
    public static final PowerGiver pg=new PowerGiver();
    public static PowerSelectScreen powerSelectScreen=new PowerSelectScreen(pg);
    public static AbstractCard placeholderCard=new Madness();
    public static HashMap<Integer, String> savedPowers = new HashMap<>();
    public static String[] charOptions = {};
    public static String pid="";
    DropdownMenuListener listener = new DropdownMenuListener() {
        @Override
        public void changedSelectionTo(DropdownMenu menu, int selectedIndex, String selectedText) {
            if((savedPowers.containsKey(selectedIndex))) {
                pid = savedPowers.get(selectedIndex);
            }
            // 在这里处理选中变化的逻辑（例如打印或更新UI）
            System.out.println("选中索引：" + selectedIndex + "，文本：" + selectedText);
        }

    };

 DropdownMenu dropdownMenu = new DropdownMenu(listener,charOptions, FontHelper.cardDescFont_N, Settings.CREAM_COLOR);
    public BreedsearchButton() {
        super(IMG, ID);


    }

    @Override
    protected void onClick() {

        // your onclick code
    }

    @Override
    public void update() {
        super.update();
        if (!Settings.hideTopBar) {
            if (this.hitbox.hovered) {
                TipHelper.renderGenericTip((float) InputHelper.mX - 140.0F * Settings.scale, Settings.HEIGHT - 120.0F * Settings.scale, "选择要变的buff", "左键选择");
            } else{

            }
        }

    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        dropdownMenu.render(sb,  InputHelper.mX - 140.0F * Settings.scale, Settings.HEIGHT - 120.0F * Settings.scale);
    }
}