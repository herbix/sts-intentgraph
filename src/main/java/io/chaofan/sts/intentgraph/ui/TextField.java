package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class TextField {
    private static final float TEXT_Y_OFFSET = 27 * Settings.scale;
    public static TextField hoverField;

    private final String label;
    private String text = "";
    private final float x;
    private final float y;
    private final float labelWidth;
    private final float textWidth;
    private final float height;
    private final Hitbox hb;

    private int pressingKey = Input.Keys.ANY_KEY;
    private float pressTimer;

    private int cursor = 0;
    private float cursorTimer;
    private float cursorRenderX = -1;
    private float textOffsetX = 0;

    private Consumer<TextField> onChange;

    public TextField(String label, float x, float y, float labelWidth, float textWidth) {
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
                if (TextField.hoverField != null) {
                    TextField.hoverField.triggerOnChange();
                }
                TextField.hoverField = this;
                this.cursorTimer = 1.5f;
            } else if (TextField.hoverField == this) {
                triggerOnChange();
                TextField.hoverField = null;
            }
        }

        if (TextField.hoverField != this) {
            pressingKey = Input.Keys.ANY_KEY;
        } else if (pressingKey != Input.Keys.ANY_KEY) {
            pressTimer -= Gdx.graphics.getDeltaTime();
            if (pressTimer <= 0) {
                this.keyPressed(pressingKey);
                pressTimer = 0.1f;
            }
        }

        cursorTimer -= Gdx.graphics.getDeltaTime();
        if (cursorTimer <= 0) {
            cursorTimer = 1.5f;
        }
    }

    public void render(SpriteBatch sb) {
        Color color;
        if (TextField.hoverField == this || this.hb.hovered) {
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

        if (TextField.hoverField == this) {
            if (cursorRenderX < 0) {
                cursorRenderX = FontHelper.getWidth(textFont, text.substring(0, cursor), 1);
                if (cursorRenderX + textOffsetX > textWidth) {
                    textOffsetX = textWidth - cursorRenderX;
                } else if (cursorRenderX + textOffsetX < 0) {
                    textOffsetX = -cursorRenderX;
                }
                if (textOffsetX > 0) {
                    textOffsetX = 0;
                }
            }
            if (cursorTimer >= 0.75f) {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.WHITE_SQUARE_IMG, x + labelWidth + cursorRenderX + textOffsetX, y, 2, height);
            }
        }

        FontHelper.renderFontLeftTopAligned(sb, textFont, text, x + labelWidth + textOffsetX, y + TEXT_Y_OFFSET, color);

        this.hb.render(sb);
    }

    public boolean keyDown(int keycode) {
        if (pressingKey != keycode) {
            pressingKey = keycode;
            pressTimer = 0.8f;
            this.keyPressed(keycode);
            return true;
        }

        return false;
    }

    public boolean keyUp(int keycode) {
        if (pressingKey == keycode) {
            pressingKey = Input.Keys.ANY_KEY;
            return true;
        }

        return false;
    }

    public boolean keyTyped(char character) {
        if (character >= 32) {
            insertText(String.valueOf(character));
            return true;
        }

        return false;
    }

    private void keyPressed(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSPACE:
                backspaceText();
                break;
            case Input.Keys.FORWARD_DEL:
                deleteText();
                break;
            case Input.Keys.LEFT:
                if (cursor > 0) {
                    cursor--;
                    cursorRenderX = -1;
                }
                break;
            case Input.Keys.RIGHT:
                if (cursor < text.length()) {
                    cursor++;
                    cursorRenderX = -1;
                }
                break;
            case Input.Keys.HOME:
            case Input.Keys.UP:
                cursor = 0;
                cursorRenderX = -1;
                break;
            case Input.Keys.END:
            case Input.Keys.DOWN:
                cursor = text.length();
                cursorRenderX = -1;
                break;
            case Input.Keys.ESCAPE:
                triggerOnChange();
                TextField.hoverField = null;
                break;
            case Input.Keys.ENTER:
                triggerOnChange();
                break;
        }
    }

    private void triggerOnChange() {
        if (this.onChange != null) {
            this.onChange.accept(this);
        }
    }

    private void insertText(String text) {
        this.text = this.text.substring(0, cursor) + text + this.text.substring(cursor);
        cursor += text.length();
        cursorRenderX = -1;
    }

    private void backspaceText() {
        if (cursor > 0) {
            this.text = this.text.substring(0, cursor - 1) + this.text.substring(cursor);
            cursor--;
            cursorRenderX = -1;
        }
    }

    private void deleteText() {
        if (cursor < this.text.length()) {
            this.text = this.text.substring(0, cursor) + this.text.substring(cursor + 1);
            cursorRenderX = -1;
        }
    }

    public void setOnChange(Consumer<TextField> onChange) {
        this.onChange = onChange;
    }

    public void setText(String text) {
        this.text = text;
        this.cursor = Math.min(cursor, text.length());
        cursorRenderX = -1;
    }

    public String getText() {
        return text;
    }
}
