package OnePower.ui;

import basemod.ReflectionHacks;
import basemod.patches.whatmod.WhatMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import loadout.LoadoutMod;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static loadout.screens.PowerSelectScreen.*;

public  class PowerButton {

        public Class<? extends AbstractPower> pClass;
        public AbstractPower instance;
        public String id;
        public PowerStrings powerStrings;
        public String name;
        public AbstractPower.PowerType type;
        public String modID;
        public String desc;
        public int amount;
        public Hitbox hb;
        public float x;
        public float y;
        public ArrayList<PowerTip> tips;
        public TextureAtlas.AtlasRegion region48;
        public TextureAtlas.AtlasRegion region128;

        public PowerButton(String id, Class<? extends AbstractPower> pClass) {
            this.pClass = pClass;
            Constructor<?>[] con = pClass.getDeclaredConstructors();
            this.tips = new ArrayList<>();





            try {
                if (specialCases.contains(id)) {
                    switch (id) {
                        case "TheBomb":
                            this.instance = new TheBombPower(dummyPlayer,0,40);
                            break;
                    }
                } else {
                    int paramCt = con[0].getParameterCount();
                    Class[] params = con[0].getParameterTypes();
                    Object[] paramz = new Object[paramCt];

                    for (int i = 0 ; i< paramCt; i++) {
                        Class param = params[i];
                        if (AbstractPlayer.class.isAssignableFrom(param)) {
                            paramz[i] = dummyPlayer;
                        } else if (AbstractMonster.class.isAssignableFrom(param)) {
                            paramz[i] = dummyMonster;
                        } else if (AbstractCreature.class.isAssignableFrom(param)) {
                            paramz[i] = dummyPlayer;
                        } else if (int.class.isAssignableFrom(param)) {
                            paramz[i] = 0;
                        } else if (AbstractCard.class.isAssignableFrom(param)) {
                            paramz[i] = dummyCard;
                        } else if (boolean.class.isAssignableFrom(param)) {
                            paramz[i] = true;
                        }
                    }
                    //LoadoutMod.logger.info("Class: " + pClass.getName() + " with parameter: " + Arrays.toString(paramz));

                    this.instance = (AbstractPower) con[0].newInstance(paramz);
                }


                this.id = id;
                this.powerStrings = ReflectionHacks.getPrivateStatic(pClass, "powerStrings");
                if (this.powerStrings == null) {
                    this.powerStrings = CardCrawlGame.languagePack.getPowerStrings(id);
                }

                //this.name = instance.name;
                this.name = powerStrings.NAME;
                this.desc = this.instance.description;
                this.modID = WhatMod.findModID(pClass);
                if (this.modID == null) this.modID = "Slay the Spire";

                this.region48 = this.instance.region48;
                this.region128 = this.instance.region128;


            } catch (Exception e) {

                LoadoutMod.logger.info("Failed to create power button for: " + pClass.getName() + " with name = "+ this.name + " for mod: "+ this.modID);
                e.printStackTrace();
            }
            if(this.id == null) this.id = "Unnamed Power";
            if (this.name == null) this.name = this.id;
            if (this.modID == null) this.modID = "Slay the Spire";

            if(desc != null && desc.length() > 0) {
                //String fullD = StringUtils.join(desc," ");
//                for (String d : desc)
//                {
//                    if (d != null)
//                        this.tips.add(new PowerTip(this.name, d));
//                }

                this.tips.add(new PowerTip(this.name, desc, region48));
            }
            this.tips.add(new PowerTip("Mod",this.modID));
            this.hb = new Hitbox(200.0f * Settings.scale,75.0f * Settings.yScale);
            this.amount = 0;
            this.x = 0;
            this.y = 0;
            //this.loadRegion(StringUtils.lowerCase(this.id));
            //this.type = instance.type;
            //LoadoutMod.logger.info("Created power button for: " + pClass.getName() + " with name = "+ this.name + " for mod: "+ this.modID);
        }

        public void update() {
            this.hb.update();
        }
        protected void loadRegion(String fileName) {
            this.region48 = AbstractPower.atlas.findRegion("48/" + fileName);
            this.region128 = AbstractPower.atlas.findRegion("128/" + fileName);
        }

        public void render(SpriteBatch sb) {
            if (this.hb != null) {
                this.hb.render(sb);
                float a = (amount != 0 || this.hb.hovered) ? 1.0f : 0.7f;
                if(this.region128 != null) {

                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, a));
                    sb.draw(this.region128, x - (float)this.region128.packedWidth / 2.0F, y - (float)this.region128.packedHeight / 2.0F, (float)this.region128.packedWidth / 2.0F, (float)this.region128.packedHeight / 2.0F, (float)this.region128.packedWidth, (float)this.region128.packedHeight, Settings.scale, Settings.scale, 0.0F);

                } else {
                    if (this.region48 != null) {
                        sb.setColor(new Color(1.0F, 1.0F, 1.0F, a));
                        sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, 128.0f, 128.0f, Settings.scale, Settings.scale, 0.0F);

                    }
                }

                if (this.hb.hovered) {
                    sb.setBlendFunction(770, 1);
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
                    sb.draw(ImageMaster.CHAR_OPT_HIGHLIGHT, x+40.0F,y-64.0F, 64.0F, 64.0F, 300.0f, 100.0f, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                    FontHelper.renderSmartText(sb,FontHelper.buttonLabelFont,this.name,x+150.0f / 2,y + 20.0f,200.0f,25.0f,Settings.GOLD_COLOR);
                    sb.setBlendFunction(770, 771);

                    TipHelper.queuePowerTips(InputHelper.mX + 60.0F * Settings.scale, InputHelper.mY + 180.0F * Settings.scale, this.tips);
                } else {
                    FontHelper.renderSmartText(sb,FontHelper.buttonLabelFont,this.name,x+150.0f / 2,y + 20.0f,200.0f,25.0f,Settings.CREAM_COLOR);
                }
                if (this.amount > 0) {
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x+30.0f, y-30.0f, 3.0f, Settings.GREEN_TEXT_COLOR);
                } else if (this.amount < 0) {
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x+30.0f, y-30.0f, 3.0f, Settings.RED_TEXT_COLOR);
                }
            }
        }

    }