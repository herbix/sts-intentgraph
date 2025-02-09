package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import io.chaofan.sts.intentgraph.model.editor.EditableLabel;
import io.chaofan.sts.intentgraph.model.editor.UndoRedoHelper;

import java.util.Arrays;

public class LabelPropertiesControl extends PropertiesControl {
    private final float x;
    private final float top;
    private final TextField labelX;
    private final TextField labelY;
    private final ComboBox align;
    private final TextField text;
    private final TextField localizedText;
    private EditableLabel label;

    public LabelPropertiesControl(float x, float top, UndoRedoHelper undoRedoHelper) {
        super(undoRedoHelper);
        this.x = x;
        this.top = top;
        this.labelX = new TextField("X", x, top - TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.labelY = new TextField("Y", x, top - 2 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.align = new ComboBox("Align", x, top - 3 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.text = new TextField("Txt", x, top - 4 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.localizedText = new TextField("Localized Txt", x, top - 5 * TEXT_FIELD_HEIGHT, 200 * Settings.scale, 250 * Settings.scale);
        this.align.setOptions(Arrays.asList("left", "middle", "right"));

        this.labelX.setOnChange(changeFloatListener(() -> label, (l) -> l.x, (l, value) -> l.x = value));
        this.labelY.setOnChange(changeFloatListener(() -> label, (l) -> l.y, (l, value) -> l.y = value));
        this.align.setOnChange(this::onAlignChange);
        this.text.setOnChange(changeStringListener(() -> label, (l) -> l.label, (l, value) -> l.label = value));
        this.localizedText.setOnChange(changeStringListener(() -> label, (l) -> l.localizedText, (l, value) -> l.localizedText = value));
    }

    public void setLabel(EditableLabel label) {
        if (this.label != label) {
            this.label = label;
            refresh();
        }
    }

    public void refresh() {
        this.labelX.setText(String.valueOf(label.x));
        this.labelY.setText(String.valueOf(label.y));
        int index = this.align.getOptions().indexOf(label.align);
        this.align.setSelection(index == -1 ? 1 : index);
        this.text.setText(label.label);
        this.localizedText.setText(label.getLocalizedString(label.label));
    }

    public void update() {
        labelX.update();
        labelY.update();
        align.update();
        text.update();
        localizedText.update();
    }

    public void render(SpriteBatch sb) {
        labelX.render(sb);
        labelY.render(sb);
        align.render(sb);
        text.render(sb);
        localizedText.render(sb);
    }

    private void onAlignChange(ComboBox comboBox) {
        int index = comboBox.getSelection() < 0 ? 1 : comboBox.getSelection();
        EditableLabel target = label;
        String oldValue = target.align;
        String newValue = comboBox.getOptions().get(index);
        undoRedoHelper.runAndPush(() -> {
            target.align = newValue;
            target.updateHitBoxesLocation();
            if (target == label) {
                comboBox.setSelection(index);
            }
        }, () -> {
            target.align = oldValue;
            target.updateHitBoxesLocation();
            if (target == label) {
                int i = comboBox.getOptions().indexOf(oldValue);
                comboBox.setSelection(i == -1 ? 1 : i);
            }
        });
    }
}
