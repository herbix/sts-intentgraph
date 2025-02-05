package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.chaofan.sts.intentgraph.model.Label;

public class EditableLabel extends Label implements EditableItem {

    private final float renderX;
    private final float renderY;

    public EditableLabel(float renderX, float renderY, Label label) {
        this.x = label.x;
        this.y = label.y;
        this.label = label.label;
        this.align = label.align;
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
