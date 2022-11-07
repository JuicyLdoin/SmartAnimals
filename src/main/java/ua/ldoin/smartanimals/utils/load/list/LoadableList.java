package ua.ldoin.smartanimals.utils.load.list;

import ua.ldoin.smartanimals.utils.load.ILoadable;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadableList implements ILoadable {

    public final List<ILoadable> loadableList;

    protected LoadableList() {

        this.loadableList = new ArrayList<>();

    }

    public void addAllLoadable(List<ILoadable> loadable) {

        loadableList.addAll(loadable);

    }

    public void load() {

        for (ILoadable loadable : loadableList)
            try {

                loadable.load();

            } catch (Exception ex) {

                ex.printStackTrace();

            }
    }
}