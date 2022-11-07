package ua.ldoin.smartanimals.utils.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityUtil {

    public static boolean isAnimal(Entity entity) {

        return FileUtil.CONFIG.getStringList("animals").contains(entity.getType().name());

    }

    public static List<Block> findNearbyBlocks(Location location, int radius, List<Material> blocks) {

        List<Block> list = new ArrayList<>();

        for (int x = location.getBlockX() + radius; x > location.getBlockX() - radius; x--)
            for (int y = location.getBlockY() + radius; y > location.getBlockY() - radius; y--)
                for (int z = location.getBlockZ() + radius; z > location.getBlockZ() - radius; z--) {

                    Location blockLocation = new Location(location.getWorld(), x, y, z);
                    Material material = blockLocation.getBlock().getType();

                    if (material != null && blocks.contains(material))
                        list.add(blockLocation.getBlock());

                }

        return list;

    }

    public static List<Block> findNearbyBlocks(Location location, int radius, Material... blocks) {

        return findNearbyBlocks(location, radius, Arrays.asList(blocks));

    }

    public static List<Location> blocksToLocations(List<Block> blocks) {

        List<Location> locations = new ArrayList<>();

        for (Block block : blocks)
            locations.add(block.getLocation());

        return locations;

    }

    public static Location findNearbyBlockLocation(Location location, int radius, List<Material> blocks) {

        Location current = null;
        double distance = radius + 1;

        for (Location locations : blocksToLocations(findNearbyBlocks(location, radius, blocks)))
            if (location.distance(locations) < distance) {

                current = locations;
                distance = location.distance(locations);

            }

        return current;

    }

    public static Location findNearbyBlockLocation(Location location, int radius, Material... blocks) {

        return findNearbyBlockLocation(location, radius, Arrays.asList(blocks));

    }

    public static Location findNearbyWaterLocation(Location currentLocation, int radius) {

        List<Location> locations = blocksToLocations(findNearbyBlocks(currentLocation, radius, Material.WATER, Material.STATIONARY_WATER));

        Location location = null;
        double distance = radius + 1;

        for (Location loc : locations)
            if (loc.getBlock().getLightLevel() > 0)
                if (currentLocation.distance(loc) < distance) {

                    location = loc;
                    distance = currentLocation.distance(loc);

                }

        return location;

    }
}