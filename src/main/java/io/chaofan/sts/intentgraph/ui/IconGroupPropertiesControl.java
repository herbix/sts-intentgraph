package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import io.chaofan.sts.intentgraph.model.editor.EditableIconGroup;
import io.chaofan.sts.intentgraph.model.editor.UndoRedoHelper;

public class IconGroupPropertiesControl extends PropertiesControl {
    private final TextField groupX;
    private final TextField groupY;
    private final TextField groupWidth;
    private final TextField groupHeight;
    private EditableIconGroup group;

    public IconGroupPropertiesControl(float x, float top, UndoRedoHelper undoRedoHelper) {
        super(undoRedoHelper);
        this.groupX = new TextField("X", x, top - TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.groupY = new TextField("Y", x, top - 2 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.groupWidth = new TextField(TEXT[10], x, top - 3 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.groupHeight = new TextField(TEXT[11], x, top - 4 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);

        this.groupX.setOnChange(changeFloatListener(() -> group, (g) -> g.x, (g, value) -> g.x = value));
        this.groupY.setOnChange(changeFloatListener(() -> group, (g) -> g.y, (g, value) -> g.y = value));
        this.groupWidth.setOnChange(changeFloatListener(() -> group, (g) -> g.w, (g, value) -> g.w = value));
        this.groupHeight.setOnChange(changeFloatListener(() -> group, (g) -> g.h, (g, value) -> g.h = value));
    }

    public void setGroup(EditableIconGroup group) {
        if (this.group != group) {
            this.group = group;
            refresh();
        }
    }

    public void refresh() {
        this.groupX.setText(String.valueOf(group.x));
        this.groupY.setText(String.valueOf(group.y));
        this.groupWidth.setText(String.valueOf(group.w));
        this.groupHeight.setText(String.valueOf(group.h));
    }

    public void update() {
        groupX.update();
        groupY.update();
        groupWidth.update();
        groupHeight.update();
    }

    public void render(SpriteBatch sb) {
        groupX.render(sb);
        groupY.render(sb);
        groupWidth.render(sb);
        groupHeight.render(sb);
    }
}
