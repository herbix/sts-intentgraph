package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.chaofan.sts.intentgraph.model.Arrow;

public class EditableArrow extends Arrow implements EditableItem {
    private final float renderX;
    private final float renderY;

    public EditableArrow(float renderX, float renderY, Arrow arrow) {
        this.path = arrow.path.clone();
        this.instant = arrow.instant;
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
        this.render(this.renderX, this.renderY, sb);
    }

    @Override
    public void renderHitBoxes(SpriteBatch sb, Color color) {

    }
}
