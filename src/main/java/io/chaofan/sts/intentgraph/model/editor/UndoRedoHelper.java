package io.chaofan.sts.intentgraph.model.editor;

import java.util.ArrayList;

public class UndoRedoHelper {
    private final ArrayList<UndoRedoItem> undoStack = new ArrayList<>();
    private final ArrayList<UndoRedoItem> redoStack = new ArrayList<>();

    public void runAndPush(Runnable redo, Runnable undo) {
        UndoRedoItem item = new UndoRedoItem(redo, undo);
        item.redo();
        undoStack.add(item);
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        UndoRedoItem item = undoStack.remove(undoStack.size() - 1);
        item.undo();
        redoStack.add(item);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        UndoRedoItem item = redoStack.remove(redoStack.size() - 1);
        item.redo();
        undoStack.add(item);
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
