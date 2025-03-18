package BlackLightRelic.powers;

import BlackLightRelic.helpers.ModHelper;
import BlackLightRelic.utils.TextureUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RegenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class zuzhizaisheng extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("zuzhizaisheng");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int amountcounter=0;

    public zuzhizaisheng(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;

        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = Amount;

        // 添加一大一小两张能力图
        String path128 = "MubanResources/images/powers/zuzhizaisheng.png";
        String path48 = "MubanResources/images/powers/zuzhizaisheng.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = TextureUtils.resizeTexture(this.region128, 48, 48);

        // 首次添加能力更新描述
        this.updateDescription();
    }
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]+(int) Math.ceil((double) (this.amount * (this.owner.maxHealth - this.owner.currentHealth)) /100)+DESCRIPTIONS[2];
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flashWithoutSound();
        this.addToTop(new AbstractGameAction() {
                          private AbstractCreature target = zuzhizaisheng.this.owner;
                          private float duration = Settings.ACTION_DUR_FAST;
                          private int amount = zuzhizaisheng.this.amount;

                          @Override
                          public void update() {
                              if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                                  this.isDone = true;
                              } else {
                                  if (this.duration == Settings.ACTION_DUR_FAST) {
                                      AbstractPower p = this.target.getPower(zuzhizaisheng.POWER_ID);
                                      if (this.target.currentHealth > 0) {
                                          this.target.tint.color = Color.CHARTREUSE.cpy();
                                          this.target.tint.changeColor(Color.WHITE.cpy());

                                          this.target.heal((int) Math.ceil((double) (this.amount * (p.owner.maxHealth - p.owner.currentHealth)) / 100), true);
                                      }

                                      if (this.target.isPlayer) {
                                         p = this.target.getPower(zuzhizaisheng.POWER_ID);
                                          if (p != null) {
                                              if (p.amount <= 0) {
                                                  this.addToBot(new RemoveSpecificPowerAction(p.owner, p.owner, p.ID));
                                              } else {
                                                  this.addToBot(new ReducePowerAction(p.owner, p.owner, p.ID, (int) Math.ceil((double) p.owner.currentHealth / 10F)));
                                              }

                                          }
                                      }
                                  }

                                  this.tickDuration();
                              }


                          }

                      });
    // 能力在更新时如何修改描述




}}