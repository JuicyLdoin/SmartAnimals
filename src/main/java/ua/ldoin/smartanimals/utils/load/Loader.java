package ua.ldoin.smartanimals.utils.load;

import org.bukkit.entity.Item;
import ua.ldoin.smartanimals.animal.AnimalEntityManager;
import ua.ldoin.smartanimals.animal.food.FoodManager;
import ua.ldoin.smartanimals.animal.task.tasks.WeightTask;
import ua.ldoin.smartanimals.listener.ListenerManager;
import ua.ldoin.smartanimals.utils.load.list.UnLoadableList;
import ua.ldoin.smartanimals.utils.util.items.*;
import ua.ldoin.smartanimals.utils.util.items.recipe.RecipeManager;

import java.util.ArrayList;
import java.util.List;

public class Loader extends UnLoadableList {

    public Loader() {

        super();

        List<ILoadable> loadable = new ArrayList<>();

        loadable.add(ItemManager.itemManager);
        loadable.add(RecipeManager.recipeManager);

        loadable.add(new FoodManager());
        loadable.add(new AnimalEntityManager());

        loadable.add(new ListenerManager());

        addAllLoadable(loadable);

        List<IUnLoadable> unLoadable = new ArrayList<>();

        unLoadable.add(new AnimalEntityManager());

        addAllUnLoadable(unLoadable);

    }

    public void loadAll() {

        for (ILoadable loadable : loadableList)
            try {

                loadable.load();

            } catch (Exception ex) {

                ex.printStackTrace();

            }
    }

    public void unLoadAll() {

        for (IUnLoadable unLoadable : unLoadableList)
            try {

                unLoadable.unload();

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        for (Item item : WeightTask.items)
            item.remove();

    }
}