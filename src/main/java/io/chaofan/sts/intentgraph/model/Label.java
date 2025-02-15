package io.chaofan.sts.intentgraph.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import io.chaofan.sts.intentgraph.IntentGraphMod;

public class Label {
    public float x;
    public float y;
    public String label;
    public String align;

    public void render(float x, float y, SpriteBatch sb) {
        float scale = Settings.scale;
        float labelX = x + this.x * scale * IntentGraphMod.GRID_SIZE;
        float labelY = y - this.y * scale * IntentGraphMod.GRID_SIZE;

        BitmapFont font = FontHelper.cardDescFont_L;
        font.getData().setScale(0.8f);
        String string = this.label;
        String localizedString = getLocalizedString(string);

        if ("left".equals(align)) {
            FontHelper.renderFontLeftTopAligned(sb, font, localizedString, labelX, labelY, Color.WHITE);
        } else if ("right".equals(align)){
            FontHelper.renderFontRightTopAligned(sb, font, localizedString, labelX, labelY, Color.WHITE);
        } else {
            FontHelper.renderFontCenteredTopAligned(sb, font, localizedString, labelX, labelY, Color.WHITE);
        }
        font.getData().setScale(1);
    }

    protected String getLocalizedString(String string) {
        String localizedString = IntentGraphMod.instance.intentStrings.get(string);
        if (localizedString != null) {
            return localizedString;
        }
        return string;
    }
}
