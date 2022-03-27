package model.world;

import model.actor.Actor;

@FunctionalInterface
public interface GenerateActorFunction {

	Actor[] generate(double moisture, double elevation, int radius);

}
