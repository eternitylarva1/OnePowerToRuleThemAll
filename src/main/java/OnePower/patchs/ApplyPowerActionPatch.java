package OnePower.patchs;

import OnePower.ui.PowerButton;
import OnePower.utils.Invoker;
import basemod.BaseMod;
import basemod.EasyConfigPanel;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Hyperbeam;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import loadout.LoadoutMod;
import loadout.relics.PowerGiver;
import loadout.screens.PowerSelectScreen;

import java.util.Iterator;

import static OnePower.modcore.OnePower.config;
import static OnePower.ui.BreedsearchButton.pid;
import static OnePower.ui.BreedsearchButton.savedPowers;
import static loadout.relics.PowerGiver.getPower;
import static loadout.screens.PowerSelectScreen.dummyPlayer;


@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR
        ,paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
    public class ApplyPowerActionPatch {

        @SpirePostfixPatch
        public static void Postfix(ApplyPowerAction  __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
           if(target==AbstractDungeon.player) {
               if (powerToApply.type == AbstractPower.PowerType.BUFF) {
                   int Index=1;
                   if(config.getInt("setted_Index1")!=0) {
                       if(config.getBool("bool1")){
                           Index*=-1;
                       }
                   AbstractPower power = getPower(savedPowers.get(config.getInt("setted_Index")), powerToApply.amount*Index, AbstractDungeon.player, new Madness());
                   if (power != null) {
                       Invoker.setField(__instance, "powerToApply", power);
                   }}
               }
               if (powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                    int Index=1;
                  if(config.getInt("setted_Index1")!=0) {
                      if(config.getBool("bool")){
                          if(powerToApply.amount>=0) {
                              Index *= -1;
                          }
                      }

                      //powerToApply.amount*=Index;
                      AbstractPower power = getPower(savedPowers.get(config.getInt("setted_Index1")), powerToApply.amount*Index, AbstractDungeon.player, new Madness());
                      if (power != null) {
                          Invoker.setField(__instance, "powerToApply", power);
                          Invoker.setField(__instance, "amount", power.amount);
                      }
                  }
               }
           }
       }



        }




