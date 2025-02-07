package io.chaofan.sts.intentgraph.model.editor;

import io.chaofan.sts.intentgraph.GraphLibrary;
import io.chaofan.sts.intentgraph.model.MonsterGraphDetail;
import io.chaofan.sts.intentgraph.model.MonsterIntentGraph;

import java.util.HashMap;

public class EditableMonsterIntentGraph {
    public float width;
    public float height;
    public final HashMap<Integer, EditableMonsterGraphDetail> graphs = new HashMap<>();

    public EditableMonsterIntentGraph() {
        this.width = 4;
        this.height = 3;
    }

    public EditableMonsterIntentGraph(float renderX, float renderY, MonsterIntentGraph intentGraph, String name) {
        this.width = intentGraph.width;
        this.height = intentGraph.height;
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
                    editableGraphDetail.width = this.width;
                }
                if (editableGraphDetail.height <= 0) {
                    editableGraphDetail.height = this.height;
                }
                graphs.put(i, editableGraphDetail);
            } else {
                graphs.put(i, graphs.get(i - 1));
            }
        }
    }
}
