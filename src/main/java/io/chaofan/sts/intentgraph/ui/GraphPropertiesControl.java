package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import io.chaofan.sts.intentgraph.model.editor.EditableMonsterGraphDetail;
import io.chaofan.sts.intentgraph.model.editor.UndoRedoHelper;

public class GraphPropertiesControl extends PropertiesControl {
    private final float x;
    private final float top;
    private final TextField width;
    private final TextField height;
    private EditableMonsterGraphDetail graphDetail;

    public GraphPropertiesControl(float x, float top, UndoRedoHelper undoRedoHelper) {
        super(undoRedoHelper);
        this.x = x;
        this.top = top;
        this.width = new TextField("Width", x, top - TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.height = new TextField("Height", x, top - 2 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.width.setOnChange(this.changeFloatListener(() -> graphDetail, (g) -> g.width, (g, value) -> g.width = value));
        this.height.setOnChange(this.changeFloatListener(() -> graphDetail, (g) -> g.height, (g, value) -> g.height = value));
    }

    public void setGraphDetail(EditableMonsterGraphDetail graphDetail) {
        if (this.graphDetail != graphDetail) {
            this.graphDetail = graphDetail;
            refresh();
        }
    }

    public void refresh() {
        this.width.setText(String.valueOf(graphDetail.width));
        this.height.setText(String.valueOf(graphDetail.height));
    }

    public void update() {
        width.update();
        height.update();
    }

    public void render(SpriteBatch sb) {
        width.render(sb);
        height.render(sb);
    }
}
