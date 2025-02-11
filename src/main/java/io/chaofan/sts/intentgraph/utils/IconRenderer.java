package io.chaofan.sts.intentgraph.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import io.chaofan.sts.intentgraph.model.DamageProvider;
import io.chaofan.sts.intentgraph.model.Icon;

public interface IconRenderer {
    public static boolean[] isAttack = new boolean[1];
    boolean renderIconImage(DamageProvider damageProvider, SpriteBatch sb, Icon icon, float iconX, float iconY, @ByRef boolean[] isAttack);
}
