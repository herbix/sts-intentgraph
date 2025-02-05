package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.chaofan.sts.intentgraph.model.*;

import java.util.ArrayList;

public class EditableMonsterGraphDetail implements DamageProvider {
    public final float renderX;
    public final float renderY;
    public int ascensionLevel;
    public float width;
    public float height;
    public ArrayList<Damage> damages = new ArrayList<>();
    public ArrayList<EditableIcon> icons = new ArrayList<>();
    public ArrayList<EditableIconGroup> iconGroups = new ArrayList<>();
    public ArrayList<EditableArrow> arrows = new ArrayList<>();
    public ArrayList<EditableLabel> labels = new ArrayList<>();

    public EditableMonsterGraphDetail(float renderX, float renderY, MonsterGraphDetail detail) {
        this.ascensionLevel = detail.ascensionLevel;
        this.width = detail.width;
        this.height = detail.height;
        if (detail.damages != null) {
            for (Damage damage : detail.damages) {
                Damage damageCopy = new Damage();
                damageCopy.max = damage.max;
                damageCopy.min = damage.min;
                damageCopy.string = damage.string;
                damages.add(damageCopy);
            }
        }
        if (detail.icons != null) {
            for (Icon icon : detail.icons) {
                icons.add(new EditableIcon(renderX, renderY, icon));
            }
        }
        if (detail.iconGroups != null) {
            for (IconGroup iconGroup : detail.iconGroups) {
                iconGroups.add(new EditableIconGroup(renderX, renderY, iconGroup));
            }
        }
        if (detail.arrows != null) {
            for (Arrow arrow : detail.arrows) {
                arrows.add(new EditableArrow(renderX, renderY, arrow));
            }
        }
        if (detail.labels != null) {
            for (Label label : detail.labels) {
                labels.add(new EditableLabel(renderX, renderY, label));
            }
        }
        this.renderX = renderX;
        this.renderY = renderY;
    }

    public void render(SpriteBatch sb) {
        for (EditableIcon icon : icons) {
            icon.render(sb, this);
        }
        for (EditableIconGroup iconGroup : iconGroups) {
            iconGroup.render(sb, this);
        }
        for (EditableArrow arrow : arrows) {
            arrow.render(sb, this);
        }
        for (EditableLabel label : labels) {
            label.render(sb, this);
        }
    }

    @Override
    public Damage getDamage(int index) {
        return damages.get(index);
    }
}
