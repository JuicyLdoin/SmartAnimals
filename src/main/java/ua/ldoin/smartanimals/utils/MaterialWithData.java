package ua.ldoin.smartanimals.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class MaterialWithData {

    private final Material material;
    private final byte data;

    public MaterialWithData(Block block) {

        material = block.getType();
        data = 0;

    }

    public MaterialWithData(Material material) {

        this.material = material;
        this.data = 0;

    }

    public MaterialWithData(Material material, byte data) {

        this.material = material;
        this.data = data;

    }

    public MaterialWithData(String self) {

        String[] bits = self.split(":");

        try {

            Material material;

            try {

                material = Material.getMaterial(Integer.parseInt(bits[0]));

            } catch (Exception ignored) {

                material = Material.matchMaterial(bits[0]);

            }

            this.material = material;

            if (bits.length == 2)
                data = Byte.parseByte(bits[1]);
            else
                data = 0;

        } catch (NumberFormatException nfe) {

            throw new IllegalArgumentException("Unable to convert id to integer and data to byte");

        }
    }

    public Material getMaterial() {

        return material;

    }

    public byte getData() {

        return data;

    }

    public void applyToBlock(Block block) {

        block.setType(material);

        try {

            block.setData(data);

        } catch (Exception ignored) {}
    }

    public String toString() {

        return material + ":" + data;

    }
}