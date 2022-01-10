package ro.marius.koth.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuboidSelection {

    private List<Block> blocks = new ArrayList<>();
    private int xMin, yMin, zMin;
    private int xMax, yMax, zMax;
    private Location positionOne;
    private Location positionTwo;
    private List<BlockState> blocksState = new ArrayList<>();

    private Map<Location, Material> locationMaterial = new HashMap<>();

    public CuboidSelection(Location positionOne, Location positionTwo) {
        this.positionOne = positionOne;
        this.positionTwo = positionTwo;
    }

    public CuboidSelection select() {

        for (int y = this.yMin; y <= this.yMax; y++) {
            for (int x = this.xMin; x <= this.xMax; x++) {
                for (int z = this.zMin; z <= this.zMax; z++) {
                    Block b = positionOne.getWorld().getBlockAt(x, y, z);
                    this.blocks.add(b);
                    this.blocksState.add(b.getState());
                    locationMaterial.put(b.getLocation(), b.getType());
                }
            }

        }
        return this;

    }

    public Map<Location, Material> getLocationMaterial() {
        return locationMaterial;
    }

    @Override
    public CuboidSelection clone() {

        return new CuboidSelection(this.positionOne, this.positionTwo);
    }

    public void assignValues() {
        this.xMin = Math.min(this.positionOne.getBlockX(), this.positionTwo.getBlockX());
        this.yMin = Math.min(this.positionOne.getBlockY(), this.positionTwo.getBlockY());
        this.zMin = Math.min(this.positionOne.getBlockZ(), this.positionTwo.getBlockZ());
        this.xMax = Math.max(this.positionOne.getBlockX(), this.positionTwo.getBlockX());
        this.yMax = Math.max(this.positionOne.getBlockY(), this.positionTwo.getBlockY());
        this.zMax = Math.max(this.positionOne.getBlockZ(), this.positionTwo.getBlockZ());
    }

    public boolean isInsideCuboidSelection(Location loc) {

        return (loc.getBlockX() >= this.xMin) && (loc.getBlockX() <= this.xMax) && (loc.getBlockZ() >= this.zMin) && (loc.getBlockZ() <= this.zMax)
                && (loc.getBlockY() >= this.yMin) && (loc.getBlockY() <= this.yMax);
    }

    public Location getMid() {

        return new Location(this.positionOne.getWorld(), (this.xMin + this.xMax) / 2, (this.yMin + this.yMax) / 2, (this.zMin + this.zMax) / 2);
    }

    public Location getPositionOne() {
        return this.positionOne;
    }

    public void setPositionOne(Location location) {
        this.positionOne = location;
    }

    public Location getPositionTwo() {
        return this.positionTwo;
    }

    public void setPositionTwo(Location pos2) {
        this.positionTwo = pos2;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
