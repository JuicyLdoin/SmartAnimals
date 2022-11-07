package ua.ldoin.smartanimals.listener;

import ua.ldoin.smartanimals.listener.listeners.*;
import ua.ldoin.smartanimals.listener.listeners.animal.AnimalBreedListener;
import ua.ldoin.smartanimals.listener.listeners.animal.AnimalDeathListener;
import ua.ldoin.smartanimals.listener.listeners.animal.AnimalStatsListener;
import ua.ldoin.smartanimals.listener.listeners.animal.gender.*;
import ua.ldoin.smartanimals.utils.load.ILoadable;
import ua.ldoin.smartanimals.utils.load.list.LoadableList;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager extends LoadableList {

    public ListenerManager() {

        List<ILoadable> listeners = new ArrayList<>();

        listeners.add(new EntitySpawnListener());
        listeners.add(new EntityLoadListener());
        listeners.add(new EntityUnloadListener());

        listeners.add(new AnimalBreedListener());
        listeners.add(new AnimalDeathListener());
        listeners.add(new AnimalStatsListener());

        listeners.add(new HenLaidEggListener());
        listeners.add(new MilkCowListener());

        addAllLoadable(listeners);

    }
}