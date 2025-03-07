package BlackLightRelic.relics;

import BlackLightRelic.helpers.ModHelper;
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
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MyRelic extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath(MyRelic.class.getSimpleName());
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "MubanResources/images/relics/MyRelic.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH = "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    public int status=0;
    private RelicStrings relicStrings;
    private int counterd=33;
    private String name;

    public MyRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        this.counter=0;
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void update() {
        super.update();
        if(this.counter/counterd >= (this.status+1)&&status<2 ){
            this.status++;
            this.counter=0;
            System.out.println("状态："+this.status);
        }else {
        return;
        }

        this.description =this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();

    }

    public void wasHPLost(int damageAmount) {
        this.counter+=damageAmount;
        switch (this.status){
            case 0:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 1), 1));

                break;
                case 1:
                    this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 2), 2));
                    break;
            default:
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, 3), 3));
                break;

        }

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && damageAmount > 0) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }

    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if(this.status==2) {
            if (!m.hasPower(MinionPower.POWER_ID) && !(m instanceof Darkling)) {
                AbstractDungeon.player.increaseMaxHp(1, false);
            }
        }
    }
// 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述

    public String getUpdatedDescription() {
        switch (this.status){
            case 0:
                this.relicStrings = CardCrawlGame.languagePack.getRelicStrings(this.relicId);
                this.setTexture(ImageMaster.loadImage(IMG_PATH));
                break;
            case 1:
                this.relicStrings = CardCrawlGame.languagePack.getRelicStrings("Muban:zaishengzuzhi");
                this.setTexture(ImageMaster.loadImage("MubanResources/images/relics/zaishengzuzhi.png"));

                break;
            default:
                this.setTexture(ImageMaster.loadImage("MubanResources/images/relics/shiyingzuzhi.png"));
                this.relicStrings = CardCrawlGame.languagePack.getRelicStrings("Muban:shiyingxingzuzhi");
        }

        this.name=this.relicStrings.NAME;
        this.description =this.relicStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        return this.description;

    }

    public AbstractRelic makeCopy() {
        return new MyRelic();
    }
}