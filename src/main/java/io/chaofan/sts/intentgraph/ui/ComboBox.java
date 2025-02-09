package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ComboBox {
    private static final float TEXT_Y_OFFSET = 27 * Settings.scale;
    public static ComboBox openedComboBox;

    private final String label;
    private final float x;
    private final float y;
    private final float labelWidth;
    private final float textWidth;
    private final float height;
    private final Hitbox hb;

    private final ArrayList<Button> openedButtons = new ArrayList<>();

    private final ArrayList<String> options = new ArrayList<>();
    private int index = -1;
    private Consumer<ComboBox> onChange;

    public ComboBox(String label, float x, float y, float labelWidth, float textWidth) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.labelWidth = labelWidth;
        this.textWidth = textWidth;
        this.height = 38 * Settings.scale;
        this.hb = new Hitbox(x + labelWidth, y, textWidth, height);
    }

    public void update() {
        this.hb.update(this.hb.x, this.hb.y);

        if (InputHelper.justClickedLeft) {
            if (this.hb.hovered) {
                InputHelper.justClickedLeft = false;
                if (ComboBox.openedComboBox != this) {
                    ComboBox.openedComboBox = this;
                    openedButtons.clear();
                    for (int i = 0; i < options.size(); i++) {
                        Button button = getButtonForOpened(i);
                        openedButtons.add(button);
                    }
                } else {
                    ComboBox.openedComboBox = null;
                }
            } else {
                ComboBox.openedComboBox = null;
            }
        }
    }

    private Button getButtonForOpened(int i) {
        Button button = new Button((TextureRegion) null, x + labelWidth, y - height * (i + 1), textWidth, height);
        button.setOnClick((b) -> {
            int oldIndex = index;
            index = i;
            ComboBox.openedComboBox = null;
            if (onChange != null && oldIndex != index) {
                onChange.accept(this);
            }
        });
        button.setOnRender((btn, sb) -> {
            BitmapFont textFont = FontHelper.cardDescFont_L;
            textFont.getData().setScale(1);

            String text = options.get(i);
            FontHelper.renderFontLeftTopAligned(sb, textFont, text, btn.getX(), btn.getY() + TEXT_Y_OFFSET, sb.getColor());
        });
        return button;
    }

    public void render(SpriteBatch sb) {
        Color color;
        if (this.hb.hovered) {
            color = Color.WHITE;
        } else {
            color = Color.LIGHT_GRAY;
        }

        BitmapFont labelFont = FontHelper.cardTitleFont;
        labelFont.getData().setScale(1);
        FontHelper.renderFontLeftTopAligned(sb, labelFont, label, x, y + TEXT_Y_OFFSET, color);

        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, x + labelWidth, y, textWidth, 1);

        BitmapFont textFont = FontHelper.cardDescFont_L;
        textFont.getData().setScale(1);

        String text = index < 0 ? "" : options.get(index);
        FontHelper.renderFontLeftTopAligned(sb, textFont, text, x + labelWidth, y + TEXT_Y_OFFSET, color);

        sb.draw(ImageMaster.FILTER_ARROW,
                x + labelWidth + textWidth - 38 * Settings.scale, y,
                38 * Settings.scale, 38 * Settings.scale);

        this.hb.render(sb);
    }

    public static boolean updateOpened() {
        if (openedComboBox == null) {
            return false;
        }

        for (Button button : openedComboBox.openedButtons) {
            button.update();
        }

        if (InputHelper.justClickedLeft) {
            openedComboBox = null;
        }

        return true;
    }

    public static void renderOpened(SpriteBatch sb) {
        if (openedComboBox == null) {
            return;
        }
        sb.setColor(0.1f, 0.1f, 0.1f, 1.0f);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, openedComboBox.x + openedComboBox.labelWidth, openedComboBox.y - openedComboBox.height * openedComboBox.options.size(), openedComboBox.textWidth, openedComboBox.height * openedComboBox.options.size());
        for (Button button : openedComboBox.openedButtons) {
            button.render(sb);
        }
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getSelection() {
        return index;
    }

    public void setOptions(List<String> options) {
        this.options.clear();
        this.options.addAll(options);
        if (this.index >= options.size()) {
            this.index = options.size() - 1;
        }
    }

    public void setSelection(int selection) {
        this.index = selection;
        if (this.index >= options.size()) {
            this.index = options.size() - 1;
        }
    }

    public void setOnChange(Consumer<ComboBox> onChange) {
        this.onChange = onChange;
    }
}
