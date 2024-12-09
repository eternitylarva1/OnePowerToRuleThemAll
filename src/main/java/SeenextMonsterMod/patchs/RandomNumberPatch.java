package SeenextMonsterMod.patchs;

import SeenextMonsterMod.utils.Invoker;
import SeenextMonsterMod.utils.ScreenPartition;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static SeenextMonsterMod.utils.ScreenPartition.assignSequentialPosition;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.floorNum;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.monsterList;


@SpirePatch(
        clz = TopPanel.class,
        method = "renderMapIcon"
)
public class RandomNumberPatch {
    private static final float HB_W;
    private static final float HB_H;
    private static Field frameShadowColorField;
    private static Random newmiscRng;
    private static int floor=-100;
    private  static MonsterGroup signedMonsterGroup;
    public static ArrayList<MonsterGroup> Monsters=new ArrayList<>();
    public RandomNumberPatch() {
    }

    @SpireInsertPatch(
         rloc=10
    )
    public static void Insert(TopPanel __instance, SpriteBatch sb) {
        if (!AbstractDungeon.isScreenUp) {
            int total = 1;
            if (total != 0) {
                AbstractCard hovered = null;
                int hoveredIndex = 0;
                int xOffset = 0;
                ArrayList NextRandomCards;
                int i;
                AbstractMonster ret;
                ScreenPartition.currentRow = 3;
                ScreenPartition.currentCol = 2;


                if(__instance.mapHb.hovered) {
                    ScreenPartition.currentRow = 3;
                    ScreenPartition.currentCol = 4;
                    for (AbstractMonster monster : renderMonsters().monsters) {
                        ret=monster;

                        if(ret!=null){
                            ScreenPartition.assignSequentialPosition(null,ret);
                        }

                    }
                    if(AbstractDungeon.getCurrRoom().monsters!=null) {
                        getMonsterForNextRoomCreation().render(sb);
                    }else{
                        AbstractMonster m=new ApologySlime();
                        m.isDead = true;
                        AbstractDungeon.getCurrRoom().monsters=new MonsterGroup(m);
                        getMonsterForNextRoomCreation().render(sb);
                    }
                }
            }
        }
    }

    private static MonsterGroup renderMonsters(){
        ArrayList<String> foreseemonster=new ArrayList<>(monsterList);
        newmiscRng=new Random(Settings.seed, CardCrawlGame.saveFile.monster_seed_count+1);

        return getMonsterForNextRoomCreation();
    }
    public static MonsterGroup getMonsterForNextRoomCreation() {
        AbstractDungeon.lastCombatMetricKey = monsterList.get(0);
        if (floorNum == floor){
            return signedMonsterGroup;
        }else{
            floor=floorNum;
            signedMonsterGroup=MonsterHelper.getEncounter(monsterList.get(1));
            return signedMonsterGroup;
        }
    }



    static {
        HB_W = 300.0F * Settings.scale;
        HB_H = 420.0F * Settings.scale;
        frameShadowColorField = null;
    }

    public static class Locator extends SpireInsertLocator {
        public Locator() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "render");
            return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
        }
    }
    @SpirePatch(
            clz = MonsterRoom.class,
            method = "onPlayerEntry"
    )
    public static class MonsterRoomPatch
    {
        public MonsterRoomPatch(){}
        @SpireInsertPatch(
                rloc=3
        )
        public static void Insert(MonsterRoom __instance) {
        __instance.monsters=getMonsterForNextRoomCreation();
        }
    }
}
