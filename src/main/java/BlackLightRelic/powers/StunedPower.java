package BlackLightRelic.powers;



import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;


public class StunedPower extends AbstractPower {
    public static final String POWER_ID = "Guardian:StunnedPower";

    public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.DEBUFF;

    public static String[] DESCRIPTIONS;

    public int storedHandSize;

    private AbstractCreature source;

    public StunedPower(AbstractCreature owner) {
        this.ID = "Muban:StunnedPower";
        this.owner = owner;
        this.type = POWER_TYPE;
        DESCRIPTIONS = (CardCrawlGame.languagePack.getPowerStrings(this.ID)).DESCRIPTIONS;
        this.name = (CardCrawlGame.languagePack.getPowerStrings(this.ID)).NAME;
        updateDescription();
    }

    public void atEndOfRound() {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, "Guardian:StunnedPower"));
        } else {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, "Guardian:StunnedPower", 1));
        }
    }

    public void atStartOfTurn() {
        super.atStartOfTurn();
        this.storedHandSize = AbstractDungeon.player.gameHandSize;
        AbstractDungeon.player.gameHandSize = 0;
        AbstractDungeon.actionManager.cardQueue.clear();
        for (AbstractCard c : AbstractDungeon.player.limbo.group)
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
    }

    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        AbstractDungeon.player.gameHandSize = this.storedHandSize;
    }

    public void updateDescription() {
        if (this.amount <= 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}
