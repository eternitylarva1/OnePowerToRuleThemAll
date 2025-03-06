package MubanMod.modcore;

import MubanMod.relics.MyRelic;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;


@SpireInitializer
public class SeenextMonster implements PostInitializeSubscriber,StartActSubscriber , EditStringsSubscriber, EditRelicsSubscriber { // 实现接口
    public SeenextMonster() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }
    public static final String MyModID = "Muban";

    public static void initialize() {
        new SeenextMonster();
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
        if (Settings.language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(RelicStrings.class, "MubanResources/localization/" + lang + "/relics.json"); // 加载相应语言的卡牌本地化内容。
        // 如果是中文，加载的就是"ExampleResources/localization/ZHS/cards.json"
    }

    @Override
    public void receivePostInitialize() {

    }
}