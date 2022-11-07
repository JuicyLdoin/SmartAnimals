package ua.ldoin.smartanimals.animal;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import ua.ldoin.smartanimals.utils.load.IUnLoadable;
import ua.ldoin.smartanimals.utils.util.EntityUtil;
import ua.ldoin.smartanimals.utils.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnimalEntityManager implements IUnLoadable {

    public void load() throws Exception {

        List<World> worlds = new ArrayList<>();

        FileConfiguration config = FileUtil.CONFIG;

        if (config.contains("worlds"))
            for (String worldName : config.getStringList("worlds")) {

                World world = Bukkit.getWorld(worldName);

                if (world != null)
                    worlds.add(world);

            }

        for (World world : worlds)
            for (Chunk chunk : world.getLoadedChunks())
                for (Entity entity : chunk.getEntities())
                    if (!entity.isDead())
                        if (EntityUtil.isAnimal(entity))
                            new AnimalEntity((LivingEntity) entity, false);

    }

    public void unload() {

        for (File file : Objects.requireNonNull(FileUtil.ANIMALS_DATA_PARENT.listFiles()))
            file.delete();

        AnimalEntity.saveAll();

    }
}