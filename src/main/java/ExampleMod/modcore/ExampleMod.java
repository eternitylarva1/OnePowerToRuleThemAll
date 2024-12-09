package ExampleMod.modcore;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

@SpireInitializer
public class ExampleMod implements EditCardsSubscriber { // 实现接口
    public ExampleMod() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }

    public static void initialize() {
        new ExampleMod();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        // TODO 这里写添加你卡牌的代码
    }
}