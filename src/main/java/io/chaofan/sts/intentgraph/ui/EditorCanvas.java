package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import io.chaofan.sts.intentgraph.IntentGraphMod;
import io.chaofan.sts.intentgraph.model.*;

public class EditorCanvas {
    private final float x;
    private final float top;
    private final float width;
    private final Toolbox toolbox;
    private String monsterName;
    private MonsterIntentGraph intentGraph;
    private MonsterGraphDetail graphDetail;
    private final Color boxColor = new Color(1, 1, 1, 0.5f);

    private Object hoveredItem;
    private Object selectedItem;
    private final Color hoverItemColor = new Color(1, 1, 1, 0.2f);
    private final Color selectedItemColor = new Color(1, 1, 0.3f, 0.2f);

    public EditorCanvas(float x, float top, float width, Toolbox toolbox) {
        this.x = x;
        this.top = top;
        this.width = width;
        this.toolbox = toolbox;
    }

    public void setIntentGraph(MonsterIntentGraph intentGraph, String monsterName) {
        this.intentGraph = intentGraph;
        this.monsterName = monsterName;
        this.hoveredItem = null;
        this.selectedItem = null;
    }

    public void setGraphDetail(MonsterGraphDetail graphDetail) {
        if (this.graphDetail != graphDetail) {
            this.graphDetail = graphDetail;
            this.hoveredItem = null;
            this.selectedItem = null;
        }
    }

    public void update() {
        this.hoveredItem = null;
        if (this.graphDetail != null) {
            float x = getGridX(InputHelper.mX);
            float y = getGridY(InputHelper.mY);
            if (this.graphDetail.icons != null) {
                for (Icon icon : this.graphDetail.icons) {
                    if (icon.x <= x && icon.x + 1 >= x && icon.y <= y && icon.y + 1 >= y) {
                        this.hoveredItem = icon;
                    }
                }
            }
            if (this.graphDetail.iconGroups != null) {
                for (IconGroup iconGroup : this.graphDetail.iconGroups) {

                }
            }
            if (this.graphDetail.arrows != null) {
                for (Arrow arrow : this.graphDetail.arrows) {

                }
            }
            if (this.graphDetail.labels != null) {
                for (Label label : this.graphDetail.labels) {

                }
            }
        }

    }

    public void render(SpriteBatch sb) {
        float scale = Settings.scale;

        int hLine = -64;
        float hLineFloat;
        int hLineNum = 0;
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        do {
            hLineFloat = hLine * scale + this.top;
            sb.setColor(1, 1, 1, hLineNum % 2 == 0 ? 0.3f : 0.1f);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.x + 32 * scale, hLineFloat - 1 * scale, this.width - 32 * scale, 2 * scale);
            hLine -= IntentGraphMod.GRID_SIZE / 2;
            hLineNum++;
        } while (hLineFloat > 0);

        int vLine = 32;
        float vLineFloat;
        int vLineNum = 0;
        float vLineEnd = this.x + this.width - IntentGraphMod.GRID_SIZE / 2f * scale;
        do {
            vLineFloat = vLine * scale + this.x;
            sb.setColor(1, 1, 1, vLineNum % 2 == 0 ? 0.3f : 0.1f);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, vLineFloat - 1 * scale, 0, 2 * scale, this.top - 64 * scale);
            vLine += IntentGraphMod.GRID_SIZE / 2;
            vLineNum++;
        } while (vLineFloat < vLineEnd);

        if (this.intentGraph != null && this.graphDetail != null) {
            this.intentGraph.render(sb, this.boxColor, this.graphDetail, this.x, this.top, this.monsterName);
        }

        FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, "X: " + getGridX(InputHelper.mX), this.x, this.top + 64 * scale, Color.WHITE);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, "Y: " + getGridY(InputHelper.mY), this.x, this.top + 32 * scale, Color.WHITE);

        if (this.hoveredItem != null) {
            this.renderItem(sb, this.hoveredItem, this.hoverItemColor);
        }
        if (this.selectedItem != null) {
            this.renderItem(sb, this.selectedItem, this.selectedItemColor);
        }
    }

    private void renderItem(SpriteBatch sb, Object item, Color color) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        if (item instanceof Icon) {
            this.renderIconBox(sb, (Icon) item, color);
        }

        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void renderIconBox(SpriteBatch sb, Icon icon, Color color) {
        float x = getScreenX(icon.x);
        float y = getScreenY(icon.y);
        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y - IntentGraphMod.GRID_SIZE * Settings.scale, IntentGraphMod.GRID_SIZE * Settings.scale, IntentGraphMod.GRID_SIZE * Settings.scale);
    }

    private float getGridX(float x) {
        return (x - this.x - 32 * Settings.scale) / (IntentGraphMod.GRID_SIZE * Settings.scale);
    }

    private float getGridY(float y) {
        return (this.top - y - 64 * Settings.scale) / (IntentGraphMod.GRID_SIZE * Settings.scale);
    }

    private float getScreenX(float x) {
        return this.x + 32 * Settings.scale + x * IntentGraphMod.GRID_SIZE * Settings.scale;
    }

    private float getScreenY(float y) {
        return this.top - 64 * Settings.scale - y * IntentGraphMod.GRID_SIZE * Settings.scale;
    }
}
