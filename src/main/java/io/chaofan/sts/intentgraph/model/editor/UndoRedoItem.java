package io.chaofan.sts.intentgraph.model.editor;

public class UndoRedoItem {

    private final Runnable undo;
    private final Runnable redo;

    public UndoRedoItem(Runnable redo, Runnable undo) {
        this.undo = undo;
        this.redo = redo;
    }

    public void undo() {
        undo.run();
    }

    public void redo() {
        redo.run();
    }
}
