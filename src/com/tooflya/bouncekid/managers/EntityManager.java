package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.entity.Entity;

/**
 * @author Tooflya.com
 * @since
 */
public class EntityManager extends org.anddev.andengine.entity.Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int lastElementNumber;
	protected int capacity;

	protected Entity[] elements;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityManager(final int capacity, Entity element) {
		this.lastElementNumber = -1;

		this.capacity = capacity;

		elements = new Entity[capacity];

		for (int i = elements.length - 1; i > 0; --i) {
			elements[i] = element.deepCopy();
			elements[i].setManager(this);
			elements[i].setID(i);
		}

		elements[0] = element;
		elements[0].setManager(this);
		elements[0].setID(0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCount() {
		return this.lastElementNumber + 1;
	}

	public int getCapacity() {
		return capacity;
	}

	public Entity getByIndex(final int index) {
		return elements[index];
	}

	public Entity create() throws NullPointerException {
		if (lastElementNumber + 1 < capacity) {
			lastElementNumber++;

			elements[lastElementNumber].create();

			return elements[lastElementNumber];
		}

		return null;
	}

	public void destroy(final int i) {
		Entity temp_element = elements[i];
		elements[i] = elements[lastElementNumber];
		elements[lastElementNumber] = temp_element;

		elements[i].setID(i);
		elements[lastElementNumber].setID(lastElementNumber);

		lastElementNumber--;
	}

	public void clear() {
		for (int i = lastElementNumber; i >= 0; --i) {
			elements[i].destroy();
		}
	}
}
