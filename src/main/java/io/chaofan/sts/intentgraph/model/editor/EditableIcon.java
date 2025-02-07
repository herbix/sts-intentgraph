package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import io.chaofan.sts.intentgraph.IntentGraphMod;
import io.chaofan.sts.intentgraph.model.Icon;

import java.util.Collection;
import java.util.Collections;

public class EditableIcon extends Icon implements EditableItem {
    private final float renderX;
    private final float renderY;

    private final Hitbox hitbox = new Hitbox(IntentGraphMod.GRID_SIZE * Settings.scale, IntentGraphMod.GRID_SIZE * Settings.scale);

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
        updateHitBoxLocation();
    }

    @Override
    public void update() {
        updateHitBoxes();
    }

    @Override
    public void updateSelected() {

    }

    @Override
    public void render(SpriteBatch sb, EditableMonsterGraphDetail graphDetail) {
        this.render(graphDetail, renderX, renderY, sb);

    }

    @Override
    public Collection<Hitbox> getHitBoxes() {
        return Collections.singleton(hitbox);
    }

    private void updateHitBoxLocation() {
        this.hitbox.move(EditableItem.getScreenX(x + 0.5f, renderX), EditableItem.getScreenY(y + 0.5f, renderY));
    }
}
