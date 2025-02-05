package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.chaofan.sts.intentgraph.model.Icon;

public class EditableIcon extends Icon implements EditableItem {
    private final float renderX;
    private final float renderY;

    public EditableIcon(float renderX, float renderY, Icon icon) {
        this.x = icon.x;
        this.y = icon.y;
        this.type = icon.type;
        this.damageIndex = icon.damageIndex;
        this.percentage = icon.percentage;
        this.limit = icon.limit;
        this.attackCount = icon.attackCount;
        this.attackCountString = icon.attackCountString;
        this.renderX = renderX;
        this.renderY = renderY;
    }

    @Override
    public void update() {

    }

    @Override
    public void updateSelected() {

    }

    @Override
    public void render(SpriteBatch sb, EditableMonsterGraphDetail graphDetail) {
        this.render(graphDetail, renderX, renderY, sb);

    }

    @Override
    public void renderHitBoxes(SpriteBatch sb, Color color) {

    }
}
