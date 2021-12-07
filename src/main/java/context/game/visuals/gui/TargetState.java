package context.game.visuals.gui;

import common.math.Vector2f;
import math.UnitQuaternion;

public class TargetState {

	private Vector2f targetPos;
	private UnitQuaternion targetRotation;
	private int targetTint;

	public TargetState(Vector2f targetPos, UnitQuaternion targetRotation, int targetTint) {
		this.targetPos = targetPos;
		this.targetRotation = targetRotation;
		this.targetTint = targetTint;
	}

	public Vector2f pos() {
		return targetPos;
	}

	public UnitQuaternion rotation() {
		return targetRotation;
	}

	public int tint() {
		return targetTint;
	}

}
