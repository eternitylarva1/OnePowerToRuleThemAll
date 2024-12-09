package SeenextMonsterMod.modcore;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static SeenextMonsterMod.patchs.RandomNumberPatch.Monsters;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.monsterList;

@SpireInitializer
public class SeenextMonster implements StartActSubscriber { // 实现接口
    public SeenextMonster() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }

    public static void initialize() {
        new SeenextMonster();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数

    @Override
    public void receiveStartAct() {
        for (String s : AbstractDungeon.monsterList) {
            Monsters.add(MonsterHelper.getEncounter(s));
        }

    }
}