package io.chaofan.sts.intentgraph.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.input.InputAction;

@SpirePatch(clz = InputAction.class, method = "isJustPressed")
@SpirePatch(clz = InputAction.class, method = "isPressed")
public class DisableInputActionPatch {
    public static boolean disabled = false;

    @SpirePrefixPatch
    public static SpireReturn<Boolean> Prefix(InputAction instance) {
        if (disabled) {
            return SpireReturn.Return(false);
        }

        return SpireReturn.Continue();
    }
}
