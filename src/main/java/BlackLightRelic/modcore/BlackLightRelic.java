package BlackLightRelic.modcore;

import BlackLightRelic.powers.heiguang;
import BlackLightRelic.relics.MyRelic;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import basemod.patches.com.megacrit.cardcrawl.rooms.AbstractRoom.StartBattleHook;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.core.Settings.language;


@SpireInitializer
public class BlackLightRelic implements EditKeywordsSubscriber,OnStartBattleSubscriber,CustomSavable,PostPowerApplySubscriber,PostInitializeSubscriber,StartActSubscriber , EditStringsSubscriber, EditRelicsSubscriber { // 实现接口
    public BlackLightRelic() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }
    public static final String MyModID = "Muban";

    public static void initialize() {
        new BlackLightRelic();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数

    @Override
    public void receiveStartAct() {

    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(MyModID)
                .packageFilter(MyRelic.class)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelic(relic,RelicType.SHARED);

                        UnlockTracker.markRelicAsSeen(relic.relicId);

                });

    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(RelicStrings.class, "MubanResources/localization/" + lang + "/relics.json"); // 加载相应语言的卡牌本地化内容。
        // 如果是中文，加载的就是"ExampleResources/localization/ZHS/cards.json"
        BaseMod.loadCustomStringsFile(PowerStrings.class, "MubanResources/localization/" + lang + "/powers.json"); // 加载相应语言的卡牌本地化内容。

    }

    @Override
    public void receivePostInitialize() {

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
        if(AbstractDungeon.floorNum==1){
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new MyRelic()));
        }
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }

        String json = Gdx.files.internal("MubanResources/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("muban", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}