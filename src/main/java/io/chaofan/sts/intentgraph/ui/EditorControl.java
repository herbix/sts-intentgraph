package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;

import java.util.function.IntConsumer;

public class EditorControl {
    private final Button save;
    private final Button exit;
    private final Button add;
    private final Button remove;
    private final Button ascensionDown;
    private final Button ascensionUp;
    private final float x;
    private final float y;
    private int ascension = 0;
    private IntConsumer onAscensionChange;
    private boolean showAdd = true;

    public EditorControl(float x, float y) {
        this.save = new Button(EditIntentGraphScreen.getButtonImage(1), x, y + 76 * Settings.scale, 64 * Settings.scale, 64 * Settings.scale);
        this.exit = new Button(EditIntentGraphScreen.getButtonImage(2), x + 210 * Settings.scale, y + 76 * Settings.scale, 64 * Settings.scale, 64 * Settings.scale);
        this.add = new Button(EditIntentGraphScreen.getButtonImage(9), x + 70 * Settings.scale, y + 76 * Settings.scale, 64 * Settings.scale, 64 * Settings.scale);
        this.remove = new Button(EditIntentGraphScreen.getButtonImage(10), x + 70 * Settings.scale, y + 76 * Settings.scale, 64 * Settings.scale, 64 * Settings.scale);
        this.ascensionDown = new Button(ImageMaster.CF_LEFT_ARROW, x + 8 * Settings.scale, y + 14 * Settings.scale, 48 * Settings.scale, 48 * Settings.scale);
        this.ascensionUp = new Button(ImageMaster.CF_RIGHT_ARROW, x + 218 * Settings.scale, y + 14 * Settings.scale, 48 * Settings.scale, 48 * Settings.scale);
        this.x = x;
        this.y = y;

        this.ascensionUp.setOnClick(this::onClickAscension);
        this.ascensionDown.setOnClick(this::onClickAscension);
    }

    private void onClickAscension(Button button) {
        if (button == this.ascensionUp) {
            ascension++;
        } else {
            ascension--;
        }
        if (ascension < 0) {
            ascension = 0;
        }
        if (ascension > 20) {
            ascension = 20;
        }
        if (onAscensionChange != null) {
            onAscensionChange.accept(ascension);
        }
    }

    public void update() {
        save.update();
        exit.update();
        if (showAdd) {
            add.update();
        } else {
            remove.update();
        }
        ascensionDown.update();
        ascensionUp.update();
    }

    public void render(SpriteBatch sb) {
        save.render(sb);
        exit.render(sb);
        if (showAdd) {
            add.render(sb);
        } else {
            remove.render(sb);
        }
        ascensionDown.render(sb);
        ascensionUp.render(sb);

        FontHelper.cardTitleFont.getData().setScale(1);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, CharacterSelectScreen.TEXT[6] + " " + ascension, x + 140 * Settings.scale, y + 38 * Settings.scale, Settings.CREAM_COLOR);
    }

    public void setOnSave(Runnable onSave) {
        save.setOnClick(button -> onSave.run());
    }

    public void setOnExit(Runnable onExit) {
        exit.setOnClick(button -> onExit.run());
    }

    public void setOnAdd(Runnable onAdd) {
        add.setOnClick(button -> onAdd.run());
    }

    public void setOnRemove(Runnable onRemove) {
        remove.setOnClick(button -> onRemove.run());
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
    }

    public void setOnAscensionChange(IntConsumer onAscensionChange) {
        this.onAscensionChange = onAscensionChange;
    }

    public int getAscension() {
        return ascension;
    }
}
