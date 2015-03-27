package org.lichtspiele.EntityRemoverPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EntityRemover {

	private static EntityRemoverPlugin plugin		= EntityRemoverPlugin.getInstance();
	
	@SuppressWarnings("unchecked")
	private static List<String> default_entities	= (List<String>) plugin.getConfig().getList("entities");
	
	/*
	 * returns a list of EntityTypes for a set of strings
	 */
	private List<EntityType> getEntityTypeList(List<String> entity_names) {
		List<EntityType> entities	= new ArrayList<EntityType>();
		
		// apply default entities if entities is null
		if (entity_names == null || entity_names.isEmpty())
			entity_names = default_entities;
		 
		if (entity_names == null || entity_names.isEmpty())
			throw new IllegalArgumentException("Entity list is empty");
		
		for (String name : entity_names) {
			EntityType entity_type = EntityType.valueOf(name);
			if (entity_type != null)
				entities.add(entity_type);
		}
		
		return entities;
	}
	
	/*
	 * returns a list of Bukkit chunks in a WorldGuard Region
	 */
	private List<Chunk> getRegionChunks(World world, ProtectedRegion region) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		
		for (BlockVector2D vector : region.getPoints()) {
			Chunk chunk = world.getChunkAt(
				vector.getBlockX() >> 4,
				vector.getBlockZ() >> 4
			);
			
			if (!chunks.contains(chunk))
				chunks.add(chunk);
		}
		
		return chunks;
	}

	/*
	 * removes entities
	 */
	public void remove(World world, ProtectedRegion region, List<String> entity_names) throws IllegalArgumentException {
		List<Chunk> chunks 				= this.getRegionChunks(world, region);
		List<EntityType> entity_types	= this.getEntityTypeList(entity_names);

		for (Chunk chunk : chunks) {
			for (Entity entity : chunk.getEntities()) {
				// check if entity is in region, continue with next if not
				if (!region.contains(
						entity.getLocation().getBlockX(),
						entity.getLocation().getBlockY(),
						entity.getLocation().getBlockZ()
					))
					continue;

				// check if this entity type has to be removed
				if (entity_types.contains(entity.getType())) {
					entity.remove();
				}
			} // for		
		} // for
	} // end
	
}