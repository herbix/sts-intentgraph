package io.chaofan.sts.intentgraph.ui;

import basemod.BaseMod;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
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
import io.chaofan.sts.intentgraph.model.editor.EditableMonsterGraphDetail;
import io.chaofan.sts.intentgraph.model.editor.EditableMonsterIntentGraph;
import io.chaofan.sts.intentgraph.patches.DisableInputActionPatch;

import java.util.HashMap;

import static io.chaofan.sts.intentgraph.IntentGraphMod.getImagePath;

public class EditIntentGraphScreen extends CustomScreen {
    private static final Texture buttonTexture = ImageMaster.loadImage(getImagePath("ui/button.png"));

    private final EditorControl editorControl = new EditorControl(50 * Settings.scale, Settings.HEIGHT - 390 * Settings.scale);
    private final Toolbox toolbox = new Toolbox(50 * Settings.scale, Settings.HEIGHT - 390 * Settings.scale);
    private final EditorCanvas editorCanvas = new EditorCanvas(350 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale, Settings.WIDTH - 870 * Settings.scale, toolbox);
    private final PropertiesControl propertiesControl = new PropertiesControl(Settings.WIDTH - 500 * Settings.scale, Settings.HEIGHT - 250 * Settings.scale);

    private final HashMap<String, EditableMonsterIntentGraph> intents = new HashMap<>();
    private EditableMonsterIntentGraph monsterIntentGraph;

    public EditIntentGraphScreen() {
        editorControl.setOnExit(this::exit);
        editorControl.setOnAscensionChange(this::onAscensionChange);
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
        DisableInputActionPatch.disabled = true;
    }

    @Override
    public void close() {
        genericScreenOverlayReset();
        InputHelper.regainInputFocus();
        TextField.hoverField = null;
        DisableInputActionPatch.disabled = false;
    }

    @Override
    public void update() {
        editorControl.update();
        toolbox.update();
        editorCanvas.update();
        propertiesControl.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        editorControl.render(sb);
        toolbox.render(sb);
        editorCanvas.render(sb);
        propertiesControl.render(sb);
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }

    public static TextureRegion getButtonImage(int index) {
        return new TextureRegion(buttonTexture, 64 * (index % 4), 64 * (index / 4), 64, 64);
    }

    private void onAscensionChange(int ascension) {
        editorCanvas.setGraphDetail(monsterIntentGraph.graphs.get(ascension));
    }

    private void exit() {
        AbstractDungeon.closeCurrentScreen();
    }

    public static class Enums {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen EDIT_INTENT_GRAPH_SCREEN;
    }
}
