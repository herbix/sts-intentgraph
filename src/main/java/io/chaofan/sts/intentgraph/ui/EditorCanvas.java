package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import io.chaofan.sts.intentgraph.IntentGraphMod;
import io.chaofan.sts.intentgraph.model.editor.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EditorCanvas {
    private final float x;
    private final float top;
    private final float width;
    private final Toolbox toolbox;
    private final UndoRedoHelper undoHelper;
    private EditableMonsterGraphDetail graphDetail;

    private EditableItem hoveredItem;
    private EditableItem selectedItem;
    private final Color hoverItemColor = new Color(1, 1, 1, 0.2f);
    private final Color selectedItemColor = new Color(1, 1, 0.3f, 0.5f);

    private Toolbox.Tool lastTool;

    private Consumer<EditorCanvas> onSelectedItemChange;

    public EditorCanvas(float x, float top, float width, Toolbox toolbox, UndoRedoHelper undoHelper) {
        this.x = x;
        this.top = top;
        this.width = width;
        this.toolbox = toolbox;
        this.undoHelper = undoHelper;
    }

    public void setGraphDetail(EditableMonsterGraphDetail graphDetail) {
        if (this.graphDetail != graphDetail) {
            this.graphDetail = graphDetail;
            this.hoveredItem = null;
            this.selectedItem = null;
        }
    }

    public void update() {
        EditableItem oldSelectedItem = this.selectedItem;
        this.hoveredItem = null;
        Toolbox.Tool tool = toolbox.getSelectedTool();
        if (tool != lastTool) {
            this.selectedItem = null;
            lastTool = tool;
        }
        if (InputHelper.justClickedRight && mouseInCanvas()) {
            this.selectedItem = null;
        }
        if (this.graphDetail != null) {
            switch (tool) {
                case MOVE: updateMoveTool(); break;
                case ICON: updateIconTool(); break;
                case GROUP: updateGroupTool(); break;
                case ARROW: updateArrowTool(); break;
                case LABEL: updateLabelTool(); break;
                case DELETE: updateDeleteTool(); break;
            }
        }
        if (oldSelectedItem != this.selectedItem && onSelectedItemChange != null) {
            onSelectedItemChange.accept(this);
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

        if (this.graphDetail != null) {
            this.graphDetail.render(sb);
        }

        FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, "X: " + getGridX(InputHelper.mX), this.x, this.top + 64 * scale, Color.WHITE);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.cardDescFont_N, "Y: " + getGridY(InputHelper.mY), this.x, this.top + 32 * scale, Color.WHITE);

        if (this.hoveredItem != null && this.hoveredItem != this.selectedItem) {
            this.renderItem(sb, this.hoveredItem, this.hoverItemColor);
        }
        if (this.selectedItem != null) {
            this.renderItem(sb, this.selectedItem, this.selectedItemColor);
        }
    }

    public float getGraphRenderX() {
        return this.x + 32 * Settings.scale;
    }

    public float getGraphRenderY() {
        return this.top - 64 * Settings.scale;
    }

    public EditableItem getSelectedItem() {
        return selectedItem;
    }

    public EditableMonsterGraphDetail getGraphDetail() {
        return graphDetail;
    }

    public void setOnSelectedItemChange(Consumer<EditorCanvas> onSelectedItemChange) {
        this.onSelectedItemChange = onSelectedItemChange;
    }

    private void renderItem(SpriteBatch sb, EditableItem item, Color color) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        item.renderHitBoxes(sb, color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private float getGridX(float x) {
        return (x - this.x - 32 * Settings.scale) / (IntentGraphMod.GRID_SIZE * Settings.scale);
    }

    private float getGridY(float y) {
        return (this.top - y - 64 * Settings.scale) / (IntentGraphMod.GRID_SIZE * Settings.scale);
    }

    private void updateMoveTool() {
        updateEditableItems(this.graphDetail.icons);
        updateEditableItems(this.graphDetail.iconGroups);
        updateEditableItems(this.graphDetail.arrows);
        updateEditableItems(this.graphDetail.labels);
        if (InputHelper.justClickedLeft && mouseInCanvas()) {
            this.selectedItem = this.hoveredItem;
        }
    }

    private void updateIconTool() {
        updateEditableItems(this.graphDetail.icons);
        if (InputHelper.justClickedLeft && mouseInCanvas()) {
            this.selectedItem = this.hoveredItem;
        }
    }

    private void updateGroupTool() {
        updateEditableItems(this.graphDetail.iconGroups);
        if (InputHelper.justClickedLeft && mouseInCanvas()) {
            this.selectedItem = this.hoveredItem;
        }
    }

    private void updateArrowTool() {
        updateEditableItems(this.graphDetail.arrows);
        if (InputHelper.justClickedLeft && mouseInCanvas()) {
            this.selectedItem = this.hoveredItem;
        }
    }

    private void updateLabelTool() {
        updateEditableItems(this.graphDetail.labels);
        if (InputHelper.justClickedLeft && mouseInCanvas()) {
            this.selectedItem = this.hoveredItem;
        }
    }

    private void updateDeleteTool() {
        updateEditableItems(this.graphDetail.icons);
        updateEditableItems(this.graphDetail.iconGroups);
        updateEditableItems(this.graphDetail.arrows);
        updateEditableItems(this.graphDetail.labels);
        if (InputHelper.justClickedLeft && this.hoveredItem != null) {
            EditableItem item = this.hoveredItem;
            ArrayList<?> list = getContainingList(item);
            if (list != null) {
                int index = list.indexOf(item);
                if (index >= 0) {
                    this.undoHelper.runAndPush(
                            () -> list.remove(index),
                            () -> ((ArrayList<EditableItem>) list).add(index, item));
                }
            }
        }
    }

    private <T extends EditableItem> void updateEditableItems(ArrayList<T> items) {
        for (T item : items) {
            item.update();
            if (item.isHovered()) {
                this.hoveredItem = item;
            }
        }
    }

    private ArrayList<?> getContainingList(EditableItem hoveredItem) {
        if (hoveredItem instanceof EditableIcon) {
            return this.graphDetail.icons;
        } else if (hoveredItem instanceof EditableIconGroup) {
            return this.graphDetail.iconGroups;
        } else if (hoveredItem instanceof EditableArrow) {
            return this.graphDetail.arrows;
        } else if (hoveredItem instanceof EditableLabel) {
            return this.graphDetail.labels;
        }
        return null;
    }

    private boolean mouseInCanvas() {
        return InputHelper.mX > this.x && InputHelper.mX < this.x + this.width && InputHelper.mY > 0 && InputHelper.mY < this.top;
    }
}
