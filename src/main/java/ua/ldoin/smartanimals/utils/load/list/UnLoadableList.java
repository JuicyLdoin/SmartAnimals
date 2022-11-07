package ua.ldoin.smartanimals.utils.load.list;

import ua.ldoin.smartanimals.utils.load.IUnLoadable;

import java.util.ArrayList;
import java.util.List;

public abstract class UnLoadableList extends LoadableList implements IUnLoadable {

    public final List<IUnLoadable> unLoadableList;

    protected UnLoadableList() {

        this.unLoadableList = new ArrayList<>();

    }

    public void addAllUnLoadable(List<IUnLoadable> unLoadable) {

        unLoadableList.addAll(unLoadable);

    }

    public void unload() {

        for (IUnLoadable unLoadable : unLoadableList)
            try {

                unLoadable.unload();

            } catch (Exception ex) {

                ex.printStackTrace();

            }
    }
}