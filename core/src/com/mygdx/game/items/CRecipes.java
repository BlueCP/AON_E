package com.mygdx.game.items;

import java.util.LinkedList;

public enum CRecipes {

	IRON_INGOT("Iron ingot", "Iron ore", Station.FURNACE),
	GOLD_INGOT("Gold ingot", "Gold ore", Station.FURNACE),
	SILVER_INGOT("Silver ingot", "Silver ore", Station.FURNACE),
	STEEL_INGOT("Steel ingot", "Iron ingot+Coal", Station.FURNACE),
	
	WAXED_HORSEHAIR("Waxed horsehair", "Horsehair+Beeswax", Station.WORKBENCH),
	LIFE_POTION("Life potion", "Water-filled flask+Vertroot", Station.WORKBENCH),
	ENERGY_POTION("Energy potion", "Water-filled flask+Voltroot", Station.WORKBENCH),
	
	POLISHED_DIAMOND("Polished diamond", "Rough diamond", Station.POLISHING_STONE),
	POLISHED_RUBY("Polished ruby", "Rough ruby", Station.POLISHING_STONE),
	POLISHED_EMERALD("Polished emerald", "Rough emerald", Station.POLISHING_STONE),
	POLISHED_SAPPHIRE("Polished sapphire", "Rough sapphire", Station.POLISHING_STONE);
	
	private String id;
	public String id() { return id; }
	
	private String components;
	public String components() { return components; }
	
	private Station station;
	public Station station() { return station; }
	
	public enum Station {
		NONE,
		WORKBENCH,
		FURNACE,
		ANVIL,
		POLISHING_STONE
	}
	
	CRecipes(String id, String components, Station station) {
		this.id = id;
		this.components = components;
		this.station = station;
	}
	
	/*
	 * Returns a string array of components for a given recipe.
	 */
	public String[] stringComponents() {
		LinkedList<String> list = new LinkedList<>(); // Initialise a list of the components
		StringBuilder currentComponent = new StringBuilder(); // A string to hold the current component
		for (char character: components.toCharArray()) {
			if (character != '+') { // If the character isn't a '+' (indicating the end of the component hasn't been reached)
				currentComponent.append(character); // Add the character to the current string
			} else {
				list.add(currentComponent.toString()); // Add the finished string to the list of strings
				currentComponent.setLength(0); // Reset the string
			}
		}
		list.add(currentComponent.toString()); // Because the last component is not added; there is no + sign after it to trigger it being added to the list
		Object[] objectArray = list.toArray(); // Converts the list to an array
		String[] stringArray = new String[objectArray.length]; // Creates a new string array the same length as the object array
		for (int i = 0; i < objectArray.length; i ++) {
			stringArray[i] = String.valueOf(objectArray[i]); // Sets each element of the string array to the corresponding string element of the object array
		}
		return stringArray;
	}
	
	public LinkedList<Item> itemComponents() {
		LinkedList<Item> listOfItems = new LinkedList<>(); // Initialise a list of type Item
		for (String component: stringComponents()) { // Iterate through the list of components of type String
			listOfItems.add(ItemFactory.createOtherItem(component)); // Add each item to the listOfItems in type Item
		}
		return listOfItems;
	}
	
	/*
	 * Returns whether or not the player has the required items in their inventory to craft an item.
	 */
	private boolean componentsPresent(Inventory inventory) {
		LinkedList<Item> listOfItems = itemComponents();
		int noOfComponentsNeeded = listOfItems.size(); // Keeps track of how many more items are needed to craft the recipe
		for (Item playerItem: inventory.otherItems) { // Iterates through player's items
			for (Item neededItem: listOfItems) { // Iterates through needed items
				if (playerItem.getOrigName().equals(neededItem.getOrigName())) // If the player has an item that is needed for the recipe
					noOfComponentsNeeded --; // Take one away from the no. of items still needed
			}
		}
		return noOfComponentsNeeded <= 0; // If all components are present, return true. If any are missing, return false.
	}
	
	public boolean craft(Inventory inventory) {
		if (!componentsPresent(inventory)) // First check if the player has all the necessary components
			return false; // If not, don't craft and return false
		for (OtherItem playerItem: inventory.otherItems) {
			for (Item neededItem: itemComponents()) {
				if (playerItem.getOrigName().equals(neededItem.getOrigName())) { // If a component if found in the player's inventory
					inventory.otherItems.removeValue(playerItem, false); // Remove that component
				}
			}
		}
		inventory.addItem(id); // Add the crafted item to the player's inventory
		return true;
	}
	
}