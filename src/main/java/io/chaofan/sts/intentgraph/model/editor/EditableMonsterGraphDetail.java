package io.chaofan.sts.intentgraph.model.editor;

import io.chaofan.sts.intentgraph.model.*;

import java.util.ArrayList;

public class EditableMonsterGraphDetail {
    public int ascensionLevel;
    public float width;
    public float height;
    public ArrayList<Damage> damages = new ArrayList<>();
    public ArrayList<Icon> icons = new ArrayList<>();
    public ArrayList<IconGroup> iconGroups = new ArrayList<>();
    public ArrayList<Arrow> arrows = new ArrayList<>();
    public ArrayList<Label> labels = new ArrayList<>();
}
