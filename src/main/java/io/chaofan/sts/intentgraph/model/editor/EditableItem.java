package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface EditableItem {
    void update();
    void updateSelected();
    void render(SpriteBatch sb);
    void renderHitBoxes(SpriteBatch sb, Color color);
}
