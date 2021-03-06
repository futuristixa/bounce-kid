package com.tooflya.bouncekid.entity;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.tooflya.bouncekid.Game;
import com.tooflya.bouncekid.GameTimer;
import com.tooflya.bouncekid.Options;
import com.tooflya.bouncekid.helpers.ActionHelper;

/**
 * @author Tooflya.com
 * @since
 */
public class Personage extends Entity {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final BitmapTextureAtlas texture = new BitmapTextureAtlas(1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	private final int maxFlyTime = 100;
	public float runStep = Options.mainStep; // TODO: Make a getter and private. Or move to Options. Or make static.
	public final int flyStep = 2; // TODO: Make a getter and private. Or move to Options. Or make static.
	public final int fallStep = 2; // TODO: Make a getter and private. Or move to Options. Or make static.

	private float startY;
	private float startX;

	// ===========================================================
	// Fields
	// ===========================================================

	private byte currentStates;

	private int flyTime;
	private int fallTime;

	public int rx = Options.babyStep * 150;
	public ArrayList<ActionsList> actions = new ArrayList<ActionsList>(); // TODO: ArrayList? Can we use only one or two variables?

	// ===========================================================
	// Constructors
	// ===========================================================

	public Personage(final TiledTextureRegion pTiledTextureRegion) {
		super(pTiledTextureRegion);

		this.currentStates = ActionHelper.Fall;
		AnimateState.setFall(this);

		this.flyTime = 0;

		Game.loadTextures(texture);
		Game.camera.setBounds(0, Integer.MAX_VALUE, -Integer.MAX_VALUE, Options.cameraHeightOrigin); // TODO: I think this code may add some problems. Very big numbers. *R
		Game.camera.setBoundsEnabled(true);
		Game.camera.setChaseEntity(this);
	}

	public Personage(final float x, final float y, final TiledTextureRegion pTiledTextureRegion) {
		this(pTiledTextureRegion);

		this.setPosition(x, y);
	}

	public Personage(final float x, final float y) {
		this(x, y, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texture, Game.context, "hero.png", 0, 0, 2, 3));
	}

	public Personage() {
		this(0, 0);
	}

	public void init() {
		this.fallTime = 0;
		this.flyTime = 0;
		this.rx = (int) this.mX;
	}

	// ===========================================================
	// Get and set
	// ===========================================================

	public int getFlyPower() {
		return this.flyTime;
	}

	public void setFlyPower(int value) {
		this.flyTime = value;
	}

	public int getMaxFlyTime() {
		return this.maxFlyTime;
	}

	public float getMaxFlyHeight() {
		return this.maxFlyTime * this.flyStep;
	}

	public float getMaxFlyDistance() {
		return this.maxFlyTime * this.runStep;
	}

	public float getMaxFallDistance() {
		return (float) this.getMaxFlyHeight() / this.fallStep * this.runStep;
	}

	public float getFreeX() {
		if (this.getX() > 100) {
			return getX() - 100;
		}
		return 0;
	}

	// ===========================================================
	// Virtual methods
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anddev.andengine.entity.sprite.AnimatedSprite#onManagedUpdate (float)
	 */
	@Override
	public void update() {
		super.update();

		this.rx += Options.mainStep;

		if (this.IsState(ActionHelper.Run) && !AnimateState.isRun) {
			AnimateState.setRun(this);
		}

		if (this.IsState(ActionHelper.Fly) && !AnimateState.isFly) {
			AnimateState.setFly(this);
		}

		if (this.IsState(ActionHelper.Fall) && !AnimateState.isFall) {
			AnimateState.setFall(this);
		}

		if (this.IsState(ActionHelper.Run) && this.IsState(ActionHelper.WantToFly)) {
			this.ChangeStates(ActionHelper.Fly, ActionHelper.Run);
		}

		if (this.IsState(ActionHelper.Fly)) {
			if (this.flyTime == 0) {
				this.startY = this.getY();
				this.startX = this.getX();
				this.fallTime = 0;
			}
			if (this.flyTime < Math.max(this.maxFlyTime, this.flyTime) && this.IsState(ActionHelper.WantToFly)) {
				this.flyTime++;
				// Change first of 3 code to use not linear moving.
				this.setPosition(this.getX() + this.runStep, this.startY - this.flyFunctionY(this.flyTime));// this.getX() + this.runStep
			} else {
				this.flyTime = 0;
				this.ChangeStates(ActionHelper.Fall, ActionHelper.Fly);
			}
		}

		if (this.IsState(ActionHelper.Fall)) {
			if (this.fallTime == 0) {
				this.startY = this.getY();
				this.startX = this.getX();
			}
			// Change second of 3 code to use not linear moving.
			this.setPosition(this.getX() + this.runStep, this.startY + this.fallFunctionY(this.fallTime));// this.getX() + this.runStep
			this.fallTime++;
		}

		if (!this.IsState(ActionHelper.Fly) && !this.IsState(ActionHelper.Fall)) {
			// Change third of 3 code to use not linear moving.
			this.setPosition(this.getX() + this.runStep, this.getY());
		}

		if (this.getY() > Options.cameraHeight) {
			GameTimer.world.init();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tooflya.bouncekid.entity.Entity#deepCopy()
	 */
	@Override
	public Entity deepCopy() {
		return new Personage(getTextureRegion());
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private float flyFunctionY(final float flyTime) {
		return this.flyStep * (float) Math.log(flyTime + 1) / (float) Math.log(this.maxFlyTime + 1) * this.maxFlyTime;
	}

	private float flyFunctionX(final float flyTime) {
		return this.runStep * flyTime;
	}

	private float fallFunctionY(final float fallTime) {
		return this.fallStep * fallTime * fallTime / this.maxFlyTime;
	}

	private float fallFunctionX(final float fallTime) {
		return this.runStep * flyTime;
	}

	public boolean IsState(byte state) {
		return (this.currentStates & state) == state;
	}

	public void ChangeStates(byte settingMaskActions, byte unsettingMaskActions) {
		this.currentStates = (byte) ((this.currentStates | settingMaskActions) & ~unsettingMaskActions); // And what I need to do if I don't want to have operation with int?
	}

	private static class AnimateState {
		public static boolean isRun = false;
		public static boolean isFall = false;
		public static boolean isFly = false;

		public static void setRun(final Personage personage) {
			isFall = false;
			isFly = false;
			isRun = true;
			personage.animate(new long[] { 80, 80 }, 0, 1, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}

		public static void setFall(final Personage personage) {
			isFall = true;
			isFly = false;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 4, 5, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}

		public static void setFly(final Personage personage) {
			isFall = false;
			isFly = true;
			isRun = false;
			personage.animate(new long[] { 80, 80 }, 2, 3, true);
			try {
				personage.actions.add(new ActionsList(personage.currentStates, personage.rx));
			} catch (NullPointerException e) {
			}
		}
	}

	public static class ActionsList {
		public byte currentStates;
		public int apt;

		public ActionsList(final byte currentStates, final int apt) {
			this.currentStates = currentStates;
			this.apt = apt;
		}
	}
}
