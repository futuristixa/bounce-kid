package com.tooflya.bouncekid.screens;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;

/**
 * @author Tooflya.com
 * @since
 */
public abstract class Screen extends Scene {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final int SCREENS_COUNT = 5;

	public static final int SPLASH = 0;
	public static final int LOADING = 1;
	public static final int MENU = 2;
	public static final int LEVEL = 3;

	// ===========================================================
	// Fields
	// ===========================================================

	public static int screen = -1;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.Entity#onAttached()
	 */
	@Override
	public void onAttached() {
		super.onAttached();

		this.setIgnoreUpdate(false);
		this.setChildrenIgnoreUpdate(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.Entity#onDetached()
	 */
	@Override
	public void onDetached() {
		super.onDetached();

		this.setIgnoreUpdate(true);
		this.setChildrenIgnoreUpdate(true);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void setScene(final Engine pEngine) {
		pEngine.setScene(this);
	}

	// ===========================================================
	// Abstract methods
	// ===========================================================

	public abstract boolean onBackPressed();
}
