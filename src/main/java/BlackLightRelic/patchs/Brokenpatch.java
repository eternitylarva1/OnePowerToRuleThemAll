package BlackLightRelic.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

@SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
public class Brokenpatch {

    @SpireInsertPatch(
            rloc = 0
    )
    public static void Insert(AbstractCreature abstractCreature) {
if(abstractCreature instanceof AbstractPlayer){
    Iterator var1 = AbstractDungeon.player.relics.iterator();

    while(var1.hasNext()) {
        AbstractRelic r = (AbstractRelic)var1.next();
        r.onBlockBroken(abstractCreature);
    }
}
    }

}