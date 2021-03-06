package com.tooflya.bouncekid.ui.parallax;


/**
 * @author Tooflya.com
 * @since
 */
public class AutoParallaxBackground extends ParallaxBackground {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AutoParallaxBackground(final float parallaxChangePerSecond) {
		this.parallaxChangePerSecond = parallaxChangePerSecond;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void accelerate(final int accelerateFactor) {
		this.parallaxChangePerSecond += accelerateFactor;
	}

	public void restoreChangePerSecond(final int parallaxChangePerSecond) {
		this.parallaxChangePerSecond = parallaxChangePerSecond;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	public void onManagedUpdate(final float secondsElapsed) {
		super.onManagedUpdate(secondsElapsed);

		this.parallaxValue += this.parallaxChangePerSecond * secondsElapsed;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
