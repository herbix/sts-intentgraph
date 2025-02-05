package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.chaofan.sts.intentgraph.model.IconGroup;

public class EditableIconGroup extends IconGroup implements EditableItem {
    private final float renderX;
    private final float renderY;

    public EditableIconGroup(float renderX, float renderY, IconGroup iconGroup) {
        this.x = iconGroup.x;
        this.y = iconGroup.y;
        this.w = iconGroup.w;
        this.h = iconGroup.h;
        this.hide = iconGroup.hide;
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
        this.render(renderX, renderY, sb);
    }

    @Override
    public void renderHitBoxes(SpriteBatch sb, Color color) {

    }
}
