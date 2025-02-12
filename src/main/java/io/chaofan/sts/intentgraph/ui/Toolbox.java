package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import java.util.HashMap;
import java.util.Map;

public class Toolbox {
    private final Map<Tool, Button> buttonMap = new HashMap<>();
    private Tool selectedTool = Tool.MOVE;

    public Toolbox(float x, float top) {
        for (int i = 0; i < Tool.values().length; i++) {
            Tool tool = Tool.values()[i];
            int gridX = i % 4;
            int gridY = i / 4;
            Button button = new Button(EditIntentGraphScreen.getButtonImage(tool.iconIndex),
                    x + 70 * gridX * Settings.scale, top - (gridY * 70 + 64) * Settings.scale,
                    64 * Settings.scale, 64 * Settings.scale);
            buttonMap.put(tool, button);
            button.setOnClick(this::onClickTool);
            if (tool == selectedTool) {
                button.setInactiveColor(new Color(0xFFD700FF));
            } else {
                button.setInactiveColor(new Color(0.6f, 0.6f, 0.6f, 1F));
            }

            button.setTooltip(EditIntentGraphScreen.TEXT[12 + i * 2], EditIntentGraphScreen.TEXT[12 + i * 2 + 1]);
        }
    }

    public void update() {
        for (Button button : buttonMap.values()) {
            button.update();
        }
    }

    public void render(SpriteBatch sb) {
        for (Button button : buttonMap.values()) {
            button.render(sb);
        }
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    private void onClickTool(Button button) {
        for (Map.Entry<Tool, Button> entry : buttonMap.entrySet()) {
            if (entry.getValue() == button) {
                selectedTool = entry.getKey();
                entry.getValue().setInactiveColor(new Color(0xFFD700FF));
            } else {
                entry.getValue().setInactiveColor(new Color(0.6f, 0.6f, 0.6f, 1F));
            }
        }
    }

    public enum Tool {
        MOVE(3),
        ICON(4),
        GROUP(5),
        ARROW(6),
        LABEL(7),
        DELETE(8);

        public final int iconIndex;

        Tool(int iconIndex) {
            this.iconIndex = iconIndex;
        }
    }
}
