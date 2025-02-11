package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.function.Consumer;

public class CheckBox extends Button {
    private static final float TEXT_Y_OFFSET = 27 * Settings.scale;

    private final String label;
    private boolean active;
    private Consumer<CheckBox> onChange;

    public CheckBox(String label, float x, float y, float width, float height) {
        super((TextureRegion) null, x, y, width, height);
        this.label = label;
        this.setOnClick((button) -> {
            this.active = !this.active;
            if (this.onChange != null) {
                this.onChange.accept(this);
            }
        });
    }

    @Override
    public void render(SpriteBatch sb) {
        Color color;
        if (this.hb.hovered) {
            color = Color.WHITE;
        } else {
            color = this.inactiveColor;
        }

        sb.setColor(color);
        sb.draw(ImageMaster.OPTION_TOGGLE, this.hb.x, this.hb.y, this.hb.height, this.hb.height);

        if (this.active) {
            sb.draw(ImageMaster.OPTION_TOGGLE_ON, this.hb.x, this.hb.y, this.hb.height, this.hb.height);
        }

        BitmapFont labelFont = FontHelper.cardTitleFont;
        labelFont.getData().setScale(1);
        FontHelper.renderFontLeftTopAligned(sb, labelFont, label, this.hb.x + this.hb.height + 5 * Settings.scale, this.hb.y + TEXT_Y_OFFSET, color);

        this.hb.render(sb);
    }

    public void setOnChange(Consumer<CheckBox> onChange) {
        this.onChange = onChange;
    }

    public void setChecked(boolean active) {
        this.active = active;
    }

    public boolean isChecked() {
        return active;
    }
}
