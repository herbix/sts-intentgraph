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
            Button buttons = new Button(EditIntentGraphScreen.getButtonImage(tool.iconIndex),
                    x + 70 * (i % 4) * Settings.scale, top - ((i / 4) * 70 + 64) * Settings.scale,
                    64 * Settings.scale, 64 * Settings.scale);
            buttonMap.put(tool, buttons);
            buttons.setOnClick(this::onClickTool);
            if (tool == selectedTool) {
                buttons.setInactiveColor(Color.GOLD);
            } else {
                buttons.setInactiveColor(new Color(0.6f, 0.6f, 0.6f, 1F));
            }
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
                entry.getValue().setInactiveColor(Color.GOLDENROD);
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
