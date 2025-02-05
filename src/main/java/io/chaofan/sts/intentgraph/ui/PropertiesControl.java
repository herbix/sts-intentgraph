package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class PropertiesControl {
    private final float x;
    private final float top;
    private final TextField propertyX;
    private final TextField propertyY;

    public PropertiesControl(float x, float top) {
        this.x = x;
        this.top = top;
        this.propertyX = new TextField("X", x, top - 64 * Settings.scale, 200 * Settings.scale, 250 * Settings.scale);
        this.propertyY = new TextField("Y", x, top - 128 * Settings.scale, 200 * Settings.scale, 250 * Settings.scale);
    }

    public void update() {
        propertyX.update();
        propertyY.update();
    }

    public void render(SpriteBatch sb) {
        propertyX.render(sb);
        propertyY.render(sb);
    }
}
