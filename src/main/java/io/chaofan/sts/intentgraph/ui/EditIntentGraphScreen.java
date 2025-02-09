package io.chaofan.sts.intentgraph.ui;

import basemod.BaseMod;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import io.chaofan.sts.intentgraph.IntentGraphMod;
import io.chaofan.sts.intentgraph.model.MonsterIntentGraph;
import io.chaofan.sts.intentgraph.model.editor.*;
import io.chaofan.sts.intentgraph.patches.DisableInputActionPatch;

import java.util.HashMap;

import static io.chaofan.sts.intentgraph.IntentGraphMod.getImagePath;

public class EditIntentGraphScreen extends CustomScreen {
    private static final Texture buttonTexture = ImageMaster.loadImage(getImagePath("ui/button.png"));

    private final UndoRedoHelper undoHelper = new UndoRedoHelper();

    private final EditorControl editorControl = new EditorControl(50 * Settings.scale, Settings.HEIGHT - 390 * Settings.scale);
    private final Toolbox toolbox = new Toolbox(50 * Settings.scale, Settings.HEIGHT - 390 * Settings.scale);
    private final EditorCanvas editorCanvas = new EditorCanvas(350 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, Settings.WIDTH - 870 * Settings.scale, toolbox, undoHelper);
    private final GraphPropertiesControl graphPropertiesControl = new GraphPropertiesControl(Settings.WIDTH - 500 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, undoHelper);
    private final IconPropertiesControl iconPropertiesControl = new IconPropertiesControl(Settings.WIDTH - 500 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, undoHelper);
    private final IconGroupPropertiesControl iconGroupPropertiesControl = new IconGroupPropertiesControl(Settings.WIDTH - 500 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, undoHelper);
    private final LabelPropertiesControl labelPropertiesControl = new LabelPropertiesControl(Settings.WIDTH - 500 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, undoHelper);
    private PropertiesControl propertyControl = null;

    private final HashMap<String, EditableMonsterIntentGraph> intents = new HashMap<>();
    private EditableMonsterIntentGraph monsterIntentGraph;

    public EditIntentGraphScreen() {
        editorControl.setOnExit(this::exit);
        editorControl.setOnAscensionChange(this::onAscensionChange);
        editorControl.setOnAdd(this::onAdd);
        editorControl.setOnRemove(this::onRemove);
        editorCanvas.setOnSelectedItemChange(this::onCanvasSelectedItemChange);
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return Enums.EDIT_INTENT_GRAPH_SCREEN;
    }

    @SuppressWarnings("unused")
    private void open(String monsterId) {
        reopen();

        monsterIntentGraph = intents.get(monsterId);
        AbstractMonster monster = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.id.equals(monsterId)).findFirst().orElse(null);
        String monsterName = monster != null ? monster.name : "";
        if (monsterIntentGraph == null) {
            MonsterIntentGraph intentGraph = IntentGraphMod.instance.getIntentGraph(monsterId);
            if (intentGraph == null) {
                monsterIntentGraph = new EditableMonsterIntentGraph();
                EditableMonsterGraphDetail graphDetail = new EditableMonsterGraphDetail(editorCanvas.getGraphRenderX(), editorCanvas.getGraphRenderY(), monsterName);
                for (int i = 0; i < 20; i++) {
                    monsterIntentGraph.graphs.put(i, graphDetail);
                }
            } else {
                intentGraph.initMonsterGraphDetail(monster != null ? monster.type : AbstractMonster.EnemyType.NORMAL);
                monsterIntentGraph = new EditableMonsterIntentGraph(editorCanvas.getGraphRenderX(), editorCanvas.getGraphRenderY(), intentGraph, monsterName);
            }
            intents.put(monsterId, monsterIntentGraph);
        }
        onAscensionChange(editorControl.getAscension());
        undoHelper.clear();
    }

    public void openScreen(String monsterId) {
        BaseMod.openCustomScreen(Enums.EDIT_INTENT_GRAPH_SCREEN, monsterId);
    }

    @Override
    public void reopen() {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen();
        Gdx.input.setInputProcessor(new EditScreenInputProcessor());
        TextField.hoverField = null;
        ComboBox.openedComboBox = null;
        DisableInputActionPatch.disabled = true;
    }

    @Override
    public void close() {
        genericScreenOverlayReset();
        InputHelper.regainInputFocus();
        TextField.hoverField = null;
        ComboBox.openedComboBox = null;
        DisableInputActionPatch.disabled = false;
    }

    @Override
    public void update() {
        if (ComboBox.updateOpened()) {
            return;
        }

        editorControl.update();
        toolbox.update();
        editorCanvas.update();
        if (propertyControl != null) {
            propertyControl.update();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    undoHelper.redo();
                } else {
                    undoHelper.undo();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
                undoHelper.redo();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        editorControl.render(sb);
        toolbox.render(sb);
        editorCanvas.render(sb);
        if (propertyControl != null) {
            propertyControl.render(sb);
        }

        ComboBox.renderOpened(sb);
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }

    public static TextureRegion getButtonImage(int index) {
        return new TextureRegion(buttonTexture, 64 * (index % 4), 64 * (index / 4), 64, 64);
    }

    private void onAscensionChange(int ascension) {
        EditableMonsterGraphDetail graphDetail = monsterIntentGraph.graphs.get(ascension);
        editorCanvas.setGraphDetail(graphDetail);
        editorControl.setShowAdd(graphDetail.ascensionLevel != ascension);
        onCanvasSelectedItemChange(editorCanvas);
    }

    private void exit() {
        AbstractDungeon.closeCurrentScreen();
    }

    private void onAdd() {
        int ascension = editorControl.getAscension();
        if (ascension == 0) {
            return;
        }
        EditableMonsterGraphDetail oldGraphDetail = monsterIntentGraph.graphs.get(ascension);
        EditableMonsterGraphDetail newGraphDetail = new EditableMonsterGraphDetail(editorCanvas.getGraphRenderX(), editorCanvas.getGraphRenderY(), oldGraphDetail);
        newGraphDetail.ascensionLevel = ascension;
        undoHelper.runAndPush(
                () -> setGraphForAscension(ascension, newGraphDetail),
                () -> setGraphForAscension(ascension, monsterIntentGraph.graphs.get(ascension - 1)));
    }

    private void onRemove() {
        int ascension = editorControl.getAscension();
        if (ascension == 0) {
            return;
        }
        EditableMonsterGraphDetail oldGraphDetail = monsterIntentGraph.graphs.get(ascension);
        undoHelper.runAndPush(
                () -> setGraphForAscension(ascension, monsterIntentGraph.graphs.get(ascension - 1)),
                () -> setGraphForAscension(ascension, oldGraphDetail));
    }

    private void setGraphForAscension(int ascension, EditableMonsterGraphDetail graphDetail) {
        EditableMonsterGraphDetail oldGraphDetail = monsterIntentGraph.graphs.get(ascension);
        for (int i = ascension; i <= 20; i++) {
            if (monsterIntentGraph.graphs.get(i) == oldGraphDetail) {
                monsterIntentGraph.graphs.put(i, graphDetail);
            } else {
                break;
            }
        }
        onAscensionChange(editorControl.getAscension());
    }

    private void onCanvasSelectedItemChange(EditorCanvas canvas) {
        TextField.hoverField = null;
        ComboBox.openedComboBox = null;

        EditableItem selectedItem = editorCanvas.getSelectedItem();
        if (selectedItem == null) {
            graphPropertiesControl.setGraphDetail(editorCanvas.getGraphDetail());
            propertyControl = graphPropertiesControl;
        } else if (selectedItem instanceof EditableIcon) {
            iconPropertiesControl.setIcon(editorCanvas.getGraphDetail(), (EditableIcon) selectedItem);
            propertyControl = iconPropertiesControl;
        } else if (selectedItem instanceof EditableIconGroup) {
            iconGroupPropertiesControl.setGroup((EditableIconGroup) selectedItem);
            propertyControl = iconGroupPropertiesControl;
        } else if (selectedItem instanceof EditableLabel) {
            labelPropertiesControl.setLabel((EditableLabel) selectedItem);
            propertyControl = labelPropertiesControl;
        } else {
            propertyControl = null;
        }
    }

    public static class Enums {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen EDIT_INTENT_GRAPH_SCREEN;
    }
}
