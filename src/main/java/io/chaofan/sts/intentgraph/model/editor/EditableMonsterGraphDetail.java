package io.chaofan.sts.intentgraph.model.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import io.chaofan.sts.intentgraph.IntentGraphMod;
import io.chaofan.sts.intentgraph.model.*;

import java.util.ArrayList;

public class EditableMonsterGraphDetail implements DamageProvider {
    private final float renderX;
    private final float renderY;
    private final String name;
    private final Color color = new Color(1.0F, 1.0F, 1.0F, 0.5F);

    public int ascensionLevel;
    public float width;
    public float height;
    public ArrayList<Damage> damages = new ArrayList<>();
    public ArrayList<EditableIcon> icons = new ArrayList<>();
    public ArrayList<EditableIconGroup> iconGroups = new ArrayList<>();
    public ArrayList<EditableArrow> arrows = new ArrayList<>();
    public ArrayList<EditableLabel> labels = new ArrayList<>();

    public EditableMonsterGraphDetail(float renderX, float renderY, String name) {
        this.width = 4;
        this.height = 3;
        this.renderX = renderX;
        this.renderY = renderY;
        this.name = name;
    }

    public EditableMonsterGraphDetail(float renderX, float renderY, String name, MonsterGraphDetail detail) {
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
        this.name = name;
    }

    public EditableMonsterGraphDetail(float renderX, float renderY, EditableMonsterGraphDetail detail) {
        this.ascensionLevel = detail.ascensionLevel;
        this.width = detail.width;
        this.height = detail.height;
        for (Damage damage : detail.damages) {
            Damage damageCopy = new Damage();
            damageCopy.max = damage.max;
            damageCopy.min = damage.min;
            damageCopy.string = damage.string;
            damages.add(damageCopy);
        }
        for (EditableIcon icon : detail.icons) {
            icons.add(new EditableIcon(renderX, renderY, icon));
        }
        for (EditableIconGroup iconGroup : detail.iconGroups) {
            iconGroups.add(new EditableIconGroup(renderX, renderY, iconGroup));
        }
        for (EditableArrow arrow : detail.arrows) {
            arrows.add(new EditableArrow(renderX, renderY, arrow));
        }
        for (EditableLabel label : detail.labels) {
            labels.add(new EditableLabel(renderX, renderY, label));
        }
        this.renderX = renderX;
        this.renderY = renderY;
        this.name = detail.name;
    }

    public void render(SpriteBatch sb) {
        float scale = Settings.scale;
        float scale32 = 32 * scale;
        float x = this.renderX - scale32;
        float y = this.renderY + scale32 * 2;

        MonsterIntentGraph.renderBox(color, x, y, width * IntentGraphMod.GRID_SIZE * scale, (height * IntentGraphMod.GRID_SIZE + 32) * scale, sb);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, name, x + 20 * scale, y - 20 * scale, Color.WHITE);

        sb.setColor(Color.WHITE);
        int renderAscensionLevel = this.ascensionLevel;
        if (renderAscensionLevel > 0) {
            MonsterIntentGraph.renderAscensionLevel(renderAscensionLevel, x + scale32 + (width * IntentGraphMod.GRID_SIZE + 12) * scale, y - 20 * scale, sb);
        }

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
