package BlackLightRelic.relics;

import BlackLightRelic.helpers.ModHelper;
import BlackLightRelic.powers.shiyingxing;
import BlackLightRelic.powers.zuzhizaisheng;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Pear;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class shiyingxingzuzhi extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(shiyingxingzuzhi.class.getSimpleName());
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "MubanResources/images/relics/shiyingzuzhi.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private RelicStrings relicStrings;
    private int counterd=33;
    private String name;

    public shiyingxingzuzhi() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter=-1;

        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void update() {
        super.update();
    }

    public void wasHPLost(int damageAmount) {


        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && damageAmount > 0) {
            this.flash();

           this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new zuzhizaisheng(AbstractDungeon.player, (int)Math.ceil((double)damageAmount/6)), (int)Math.ceil((double)damageAmount/6)));
           this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new shiyingxing(AbstractDungeon.player, (int)Math.ceil((double)damageAmount/6)), (int)Math.ceil((double)damageAmount/6)));
           this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            }

        }




    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);

            if (!m.hasPower(MinionPower.POWER_ID) && !(m instanceof Darkling)) {
                AbstractDungeon.player.increaseMaxHp((int) Math.ceil((double) m.maxHealth*6/100), false);
            }
    }

    @Override
    public void obtain() {
        super.obtain();
    }
    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];

    }

    public AbstractRelic makeCopy() {
        return new shiyingxingzuzhi();
    }
}