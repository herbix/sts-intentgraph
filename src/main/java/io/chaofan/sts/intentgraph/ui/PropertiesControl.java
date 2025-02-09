package io.chaofan.sts.intentgraph.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import io.chaofan.sts.intentgraph.model.editor.EditableItem;
import io.chaofan.sts.intentgraph.model.editor.UndoRedoHelper;

import java.util.function.*;

public abstract class PropertiesControl {
    protected static final float TEXT_FIELD_HEIGHT = 42 * Settings.scale;
    protected final UndoRedoHelper undoRedoHelper;

    public abstract void update();
    public abstract void render(SpriteBatch sb);
    public abstract void refresh();

    public PropertiesControl(UndoRedoHelper undoRedoHelper) {
        this.undoRedoHelper = undoRedoHelper;
    }

    protected <T> Consumer<TextField> changeFloatListener(Supplier<T> targetProvider, FloatGetter<T> getter, FloatSetter<T> setter) {
        return textField -> {
            T target = targetProvider.get();
            if (target == null) {
                return;
            }
            float oldValue = getter.get(target);
            float newValue = textField.getFloat();
            undoRedoHelper.runAndPush(() -> {
                setter.set(target, newValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(String.valueOf(newValue));
                }
            }, () -> {
                setter.set(target, oldValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(String.valueOf(oldValue));
                }
            });
        };
    }

    protected <T> Consumer<TextField> changeIntListener(Supplier<T> targetProvider, IntGetter<T> getter, IntSetter<T> setter) {
        return textField -> {
            T target = targetProvider.get();
            if (target == null) {
                return;
            }
            int oldValue = getter.get(target);
            int newValue = textField.getInt();
            undoRedoHelper.runAndPush(() -> {
                setter.set(target, newValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(String.valueOf(newValue));
                }
            }, () -> {
                setter.set(target, oldValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(String.valueOf(oldValue));
                }
            });
        };
    }

    protected <T> Consumer<TextField> changeStringListener(Supplier<T> targetProvider, Getter<T, String> getter, Setter<T, String> setter) {
        return textField -> {
            T target = targetProvider.get();
            if (target == null) {
                return;
            }
            String oldValue = getter.get(target) == null ? "" : getter.get(target);
            String newValue = textField.getText();
            undoRedoHelper.runAndPush(() -> {
                setter.set(target, newValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(newValue);
                }
            }, () -> {
                setter.set(target, oldValue);
                if (target instanceof EditableItem) {
                    ((EditableItem) target).updateHitBoxesLocation();
                }
                if (target == targetProvider.get()) {
                    textField.setText(oldValue);
                }
            });
        };
    }

    public interface FloatGetter<T> {
        float get(T target);
    }

    public interface FloatSetter<T> {
        void set(T target, float value);
    }

    public interface IntGetter<T> {
        int get(T target);
    }

    public interface IntSetter<T> {
        void set(T target, int value);
    }

    public interface Getter<T, R> {
        R get(T target);
    }

    public interface Setter<T, R> {
        void set(T target, R value);
    }
}
