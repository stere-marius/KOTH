package ro.marius.koth.match;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import ro.marius.koth.utils.CuboidSelection;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KothAreaAnimation {

    // daca nu e nicio echipa si daca exista block-uri de facut restore, le dau restore unul cate unul
    // daca exista echipa dau update la block-uri

    public void restoreKothArea(CuboidSelection cuboidSelection) {

    }

    public void update(KothTeam team, CuboidSelection cuboidSelection) {

        List<Block> woolBlocks = cuboidSelection
                .getBlocks()
                .stream()
                .filter(b -> b.getType() != team.getKothAreaMaterial() && b.getType().name().endsWith("_WOOL"))
                .collect(Collectors.toList());


        if (woolBlocks.isEmpty()) return;

        int blocksPerIteration = Math.min(10, woolBlocks.size());

        for (int i = 0; i < blocksPerIteration; i++) {
            woolBlocks.get(i).setType(team.getKothAreaMaterial());
            woolBlocks.get(i).getLocation().getWorld().playEffect(woolBlocks.get(i).getLocation(), Effect.SMOKE, 10);
        }

    }

}
