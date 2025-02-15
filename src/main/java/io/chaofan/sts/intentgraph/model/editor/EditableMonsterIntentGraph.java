package io.chaofan.sts.intentgraph.model.editor;

import io.chaofan.sts.intentgraph.GraphLibrary;
import io.chaofan.sts.intentgraph.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class EditableMonsterIntentGraph {
    public final HashMap<Integer, EditableMonsterGraphDetail> graphs = new HashMap<>();

    public EditableMonsterIntentGraph() {
    }

    public EditableMonsterIntentGraph(float renderX, float renderY, MonsterIntentGraph intentGraph, String name) {
        float width = intentGraph.width;
        float height = intentGraph.height;
        MonsterGraphDetail lastGraph = null;
        for (int i = 0; i <= 20; i++) {
            GraphLibrary graphLibrary = intentGraph.getGraphLibrary();
            graphLibrary.setOverwriteAscension(i);
            MonsterGraphDetail graphDetail = graphLibrary.get(null);
            if (graphDetail != lastGraph) {
                lastGraph = graphDetail;
                EditableMonsterGraphDetail editableGraphDetail = new EditableMonsterGraphDetail(renderX, renderY, name, graphDetail);
                editableGraphDetail.ascensionLevel = i;
                if (editableGraphDetail.width <= 0) {
                    editableGraphDetail.width = width;
                }
                if (editableGraphDetail.height <= 0) {
                    editableGraphDetail.height = height;
                }
                graphs.put(i, editableGraphDetail);
            } else {
                graphs.put(i, graphs.get(i - 1));
            }
        }
    }

    public MonsterIntentGraph save() {
        MonsterIntentGraph intentGraph = new MonsterIntentGraph();
        List<EditableMonsterGraphDetail> details = graphs.values().stream()
                .distinct()
                .sorted(Comparator.comparing(g -> g.ascensionLevel))
                .collect(Collectors.toList());

        EditableMonsterGraphDetail firstDetail = details.get(0);
        intentGraph.width = firstDetail.width;
        intentGraph.height = firstDetail.height;
        intentGraph.graphs = null;
        intentGraph.graphList = details.stream().map(this::saveDetail).collect(Collectors.toList());
        return intentGraph;
    }

    public Map<String, String> getLocalizedStrings() {
        Map<String, String> localizedStrings = new HashMap<>();
        for (EditableMonsterGraphDetail detail : graphs.values()) {
            for (EditableLabel label : detail.labels) {
                localizedStrings.put(label.label, label.getLocalizedString(label.label));
            }
        }
        localizedStrings.entrySet().removeIf(entry -> entry.getKey().equals(entry.getValue()));
        return localizedStrings;
    }

    private MonsterGraphDetail saveDetail(EditableMonsterGraphDetail editableDetail) {
        MonsterGraphDetail detail = new MonsterGraphDetail();
        detail.condition = editableDetail.ascensionLevel == 0 ? "true" : "ascension >= " + editableDetail.ascensionLevel;
        detail.ascensionLevel = editableDetail.ascensionLevel;
        detail.width = editableDetail.width;
        detail.height = editableDetail.height;
        detail.damages = editableDetail.damages.stream().map(this::convertDamage).toArray(Damage[]::new);
        detail.icons = editableDetail.icons.stream().map(EditableIcon::toIcon).toArray(Icon[]::new);
        detail.iconGroups = editableDetail.iconGroups.stream().map(EditableIconGroup::toIconGroup).toArray(IconGroup[]::new);
        detail.arrows = editableDetail.arrows.stream().map(EditableArrow::toArrow).toArray(Arrow[]::new);
        detail.labels = editableDetail.labels.stream().map(EditableLabel::toLabel).toArray(Label[]::new);
        return detail;
    }

    private Damage convertDamage(Damage damage) {
        if (damage != null) {
            Damage newDamage = new Damage();
            newDamage.min = damage.min;
            newDamage.max = damage.max == damage.min ? 0 : damage.max;
            newDamage.string = damage.string;
            return newDamage;
        }
        return null;
    }
}
