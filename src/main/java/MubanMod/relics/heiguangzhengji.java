package MubanMod.relics;

import MubanMod.helpers.ModHelper;
import MubanMod.powers.heiguang;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class heiguangzhengji extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(heiguangzhengji.class.getSimpleName());
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "MubanResources/images/relics/heiguangzhengji.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public heiguangzhengji() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter=0;
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, AbstractDungeon.player, new PoisonPower(monster,AbstractDungeon.player, 2), 2));
        }
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (m.hasPower(heiguang.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new PoisonPower(m,AbstractDungeon.player, 2), 2));
        }
    }

    public AbstractRelic makeCopy() {
        return new heiguangzhengji();
    }
}