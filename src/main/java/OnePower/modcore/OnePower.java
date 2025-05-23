package OnePower.modcore;

import OnePower.ui.BreedsearchButton;
import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import loadout.LoadoutMod;
import loadout.relics.AllInOneBag;
import loadout.relics.PowerGiver;
import loadout.uiElements.ModLabeledDropdown;
import loadout.util.ModConfig;
import loadout.util.SkinManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static OnePower.ui.BreedsearchButton.placeholderCard;
import static OnePower.ui.BreedsearchButton.savedPowers;
import static basemod.BaseMod.registerModBadge;
import static com.megacrit.cardcrawl.core.Settings.language;
import static loadout.relics.PowerGiver.getPower;
import static loadout.screens.PowerSelectScreen.dummyPlayer;


@SpireInitializer
public class OnePower implements PostInitializeSubscriber,EditKeywordsSubscriber,OnStartBattleSubscriber,CustomSavable,PostPowerApplySubscriber, StartActSubscriber , EditStringsSubscriber, EditRelicsSubscriber { // 实现接口
    public OnePower() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }
    public static final String MyModID = "Muban";
    ModPanel settingsPanel = new ModPanel();
    public static SpireConfig config;
    public static void initialize() throws IOException {

        new OnePower();
        config=new SpireConfig("OnePower","OnePower");
        config.load();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数

    @Override
    public void receiveStartAct() {

    }

    @Override
    public void receiveEditRelics() {


    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }


    }
    public static float getYPos(float y) {
        return Settings.HEIGHT/(2160/y);
    }
    public static float getXPos(float x) {
        return Settings.WIDTH/(3840/x);
    }
    @Override
    public void receivePostInitialize() {
        float startingXPos = Settings.WIDTH/2.84f;
        float xSpacing = 250.0F;
        float settingYPos = Settings.HEIGHT/2.880f;
        float lineSpacing = 50.0F;
        Consumer<ModToggleButton> clickAction = (button) -> {
            config.setBool("bool",button.enabled);
            System.out.println("反转1！"+button.enabled);
            try {
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Consumer<ModToggleButton> clickAction1 = (button) -> {
            config.setBool("bool1",button.enabled);
            System.out.println("反转2！"+button.enabled);
            try {
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        ModLabeledToggleButton bool = new   ModLabeledToggleButton("负数？", startingXPos-getXPos(1000)+getXPos(400), settingYPos-getYPos(64.0f)*2, Color.WHITE, FontHelper.buttonLabelFont, config.getBool("bool"),settingsPanel, (label) -> {
        },clickAction);
        ModLabeledToggleButton bool1 = new   ModLabeledToggleButton("负数？", startingXPos+getXPos(200), settingYPos-getYPos(64.0f)*2, Color.WHITE, FontHelper.buttonLabelFont, config.getBool("bool1"),settingsPanel, (label) -> {
        },clickAction1);
        ModPanel settingsPanel = new ModPanel();
        ArrayList<String> tempNames = new ArrayList<>();
        ArrayList<String> tempids = new ArrayList<>();
        int index = 1; // 用于跟踪当前插入的位置
        AbstractMonster Cultist=new Cultist(0,0);
        tempNames.add("无");
        savedPowers.put(0,"无");
        for (String pID : LoadoutMod.powersToDisplay.keySet()) {
            tempids.add(pID);
            AbstractPower p = getPower(pID, 1, Cultist, placeholderCard);
            if (p != null&&p.name!=null&&!p.name.contains("Missing")) {
                tempNames.add(p.name); // 将当前 power 的 names 添加进去
                savedPowers.put( index++,pID);
            }
        }
        System.out.println(tempNames);
        BreedsearchButton.charOptions = tempNames.toArray(new String[0]);
        ModLabel label1 = new ModLabel("玩家身上debuff->？", startingXPos-getXPos(1000), settingYPos, Color.WHITE, FontHelper.buttonLabelFont, settingsPanel, (label) -> {
        });
        ModLabel label2 = new ModLabel("玩家身上buff->？", startingXPos-getXPos(200), settingYPos, Color.WHITE, FontHelper.buttonLabelFont, settingsPanel, (label) -> {
        });
        ModLabel label3 = new ModLabel("怪物身上debuff->？", startingXPos-getXPos(1000), settingYPos-getYPos(64.0f), Color.WHITE, FontHelper.buttonLabelFont, settingsPanel, (label) -> {
        });
        ModLabel label4 = new ModLabel("怪物身上buff->？", startingXPos-getXPos(200), settingYPos-getYPos(64.0f), Color.WHITE, FontHelper.buttonLabelFont, settingsPanel, (label) -> {
        });
        ModLabeledDropdown BuffChange = new ModLabeledDropdown("", (String)null, startingXPos+Settings.WIDTH/6.4f, settingYPos+Settings.HEIGHT/4.32f, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, tempNames, (label) -> {
        }, (dropdownMenu) -> {
            if (dropdownMenu.getHitbox().justHovered) {
                AllInOneBag.INSTANCE.showRelics();
            }

            if (!dropdownMenu.getHitbox().hovered && AllInOneBag.isSelectionScreenUp) {
                AllInOneBag.INSTANCE.hideAllRelics();
            }

        }, (i, skinName) -> {
            try {

                config.setInt("setted_Index", i);
                config.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });
        ModLabeledDropdown BuffChangeM = new ModLabeledDropdown("", (String)null, startingXPos+Settings.WIDTH/6.4f, settingYPos+Settings.HEIGHT/4.32f-getYPos(84.0f), Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, tempNames, (label) -> {
        }, (dropdownMenu) -> {
            if (dropdownMenu.getHitbox().justHovered) {
                AllInOneBag.INSTANCE.showRelics();
            }

            if (!dropdownMenu.getHitbox().hovered && AllInOneBag.isSelectionScreenUp) {
                AllInOneBag.INSTANCE.hideAllRelics();
            }

        }, (i, skinName) -> {
            try {

                config.setInt("setted_Index2", i);
                config.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });

        ModLabeledDropdown deBuffChange = new ModLabeledDropdown("", (String)null, startingXPos-Settings.WIDTH/6.4f, settingYPos+Settings.HEIGHT/4.32f, Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, tempNames, (label) -> {
        }, (dropdownMenu) -> {
            if (dropdownMenu.getHitbox().justHovered) {
                AllInOneBag.INSTANCE.showRelics();
            }

            if (!dropdownMenu.getHitbox().hovered && AllInOneBag.isSelectionScreenUp) {
                AllInOneBag.INSTANCE.hideAllRelics();
            }

        }, (i, skinName) -> {
            try {

                config.setInt("setted_Index1", i);
                config.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });
        ModLabeledDropdown deBuffChangeM = new ModLabeledDropdown("", (String)null, startingXPos-Settings.WIDTH/6.4f, settingYPos+Settings.HEIGHT/4.32f-getYPos(84.0f), Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, tempNames, (label) -> {
        }, (dropdownMenu) -> {
            if (dropdownMenu.getHitbox().justHovered) {
                AllInOneBag.INSTANCE.showRelics();
            }

            if (!dropdownMenu.getHitbox().hovered && AllInOneBag.isSelectionScreenUp) {
                AllInOneBag.INSTANCE.hideAllRelics();
            }

        }, (i, skinName) -> {
            try {

                config.setInt("setted_Index3", i);
                config.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });
        if (!config.has("setted_Index")){
            config.setInt("setted_Index", 0);
        }
        if (!config.has("setted_Index1")){
            config.setInt("setted_Index1", 0);
        }
        if (!config.has("setted_Index2")){
            config.setInt("setted_Index2", 0);
        }
        if (!config.has("setted_Index3")){
            config.setInt("setted_Index3", 0);
        }
        BuffChange.dropdownMenu.setSelectedIndex(Integer.valueOf(config.getInt("setted_Index")));
        deBuffChange.dropdownMenu.setSelectedIndex(Integer.valueOf(config.getInt("setted_Index1")));
        settingsPanel.addUIElement(label1);
        settingsPanel.addUIElement(label2);
        settingsPanel.addUIElement(label3);
        settingsPanel.addUIElement(label4);
        settingsPanel.addUIElement(bool);
        settingsPanel.addUIElement(bool1);
        settingsPanel.addUIElement(BuffChangeM);
        settingsPanel.addUIElement(BuffChange);
        settingsPanel.addUIElement(deBuffChangeM);
        settingsPanel.addUIElement(deBuffChange);
        Texture badgeTexture = new Texture(Gdx.files.internal("OnePowerResources/images/relics/MyRelic.png"));
        registerModBadge(badgeTexture, "OnePower", "Butterfly Norm", "这是一段描述", settingsPanel);
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        /*
        if(abstractPower instanceof heiguang&&abstractCreature.hasPower(heiguang.POWER_ID)){
            AbstractPower power = abstractCreature.getPower("heiguang");
            if(power!=null) {
                power.onSpecificTrigger();
            }
        };*/
    }


    @Override
    public Object onSave() {
        return null;
    }

    @Override
    public void onLoad(Object o) {

    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }

        String json = Gdx.files.internal("OnePowerResources/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        /*
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("muban", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }*/
    }
}