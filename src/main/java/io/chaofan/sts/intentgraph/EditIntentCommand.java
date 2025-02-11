package io.chaofan.sts.intentgraph;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.chaofan.sts.intentgraph.ui.EditIntentGraphScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class EditIntentCommand extends ConsoleCommand {
    public EditIntentCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 0;
        requiresPlayer = true;
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> options = new ArrayList<>();
        if (tokens.length > depth + 1) {
            tooManyTokensError();
        } else {
            MapRoomNode node = AbstractDungeon.getCurrMapNode();
            if (node != null && node.room != null && node.room.monsters != null) {
                node.room.monsters.monsters.stream()
                        .map(m -> m.id)
                        .distinct()
                        .forEach(options::add);
            }
        }

        return options;
    }

    @Override
    protected void execute(String[] args, int depth) {
        IntentGraphMod.instance.loadIntents();
        if (AbstractDungeon.screen == EditIntentGraphScreen.Enums.EDIT_INTENT_GRAPH_SCREEN) {
            AbstractDungeon.closeCurrentScreen();
        } else if (depth < args.length) {
            String monsterId = String.join(" ", Arrays.copyOfRange(args, depth, args.length));
            if (!monsterId.trim().isEmpty()) {
                IntentGraphMod.editIntentGraphScreen.openScreen(monsterId);
            }
        }
    }
}
