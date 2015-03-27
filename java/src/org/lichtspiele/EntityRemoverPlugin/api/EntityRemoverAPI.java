package org.lichtspiele.EntityRemoverPlugin.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.lichtspiele.EntityRemoverPlugin.EntityRemover;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EntityRemoverAPI {

	public static void removeEntity(World world, ProtectedRegion region, String entity_name) {
		List<String> entity_names = new ArrayList<String>();
		entity_names.add(entity_name.toUpperCase());
		removeEntities(world, region, entity_names);
	}	

	public static void removeEntities(World world, ProtectedRegion region) throws IllegalArgumentException {
		removeEntities(world, region, null);
	}
	
	public static void removeEntities(World world, ProtectedRegion region, List<String> entity_names) throws IllegalArgumentException {
		new EntityRemover().remove(world, region, entity_names);
	}

}