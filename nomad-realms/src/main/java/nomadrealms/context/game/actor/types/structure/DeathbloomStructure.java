package nomadrealms.context.game.actor.types.structure;

import static engine.common.colour.Colour.rgb;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static nomadrealms.context.game.actor.types.structure.factory.StructureType.DEATHBLOOM;
import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.ArrayList;
import java.util.List;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.effect.DeathEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.RestoreManaEffect;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.card.query.actor.StaticTargetQuery;
import nomadrealms.context.game.event.ProcChain;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.event.game.effect.EffectContext;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

public class DeathbloomStructure extends Structure {

	public DeathbloomStructure() {
		super("Deathbloom", "tree_1", 1, 10);
	}

	@Override
	public List<ProcChain> trigger(World world, Effect effect) {
		if (effect instanceof DeathEffect) {
			DeathEffect deathEffect = (DeathEffect) effect;
			if (deathEffect.target() instanceof CardPlayer && deathEffect.target().tile().coord().distanceTo(this.tile().coord()) <= 5) {
				List<Effect> restorationEffects = new ArrayList<>();
				List<Chunk> chunks = this.tile().chunk().getSurroundingChunks();
				for (Chunk chunk : chunks) {
					if (chunk == null) {
						continue;
					}
					for (Actor actor : chunk.actors()) {
						if (actor instanceof CardPlayer && actor != deathEffect.target() && actor.tile().coord().distanceTo(this.tile().coord()) <= 5) {
							restorationEffects.add(new RestoreManaEffect(this, (CardPlayer) actor, 1));
							restorationEffects.add(new SpawnParticlesEffect(
									this,
									new BasicParticleSpawner(new StaticTargetQuery<>(actor.tile()), "ice_cube")
											.particleCount(1)
											.color((i, s, t) -> rgb(100, 200, 255))
											.size((i, s, t) -> new ConstraintPair(absolute(10), absolute(10)))
											.position((i, s, t) -> {
												ConstraintPair startPos = this.tile().pos();
												ConstraintPair endPos = t.tile().pos();
												return startPos.add(endPos.sub(startPos).scale(time().divide(500f)));
											})
											.lifetime((i, s, t) -> 500L),
									new EffectContext().source(this).target(actor)
							));
						}
					}
				}
				if (!restorationEffects.isEmpty()) {
					return singletonList(new ProcChain(restorationEffects));
				}
			}
		}
		return emptyList();
	}

	@Override
	public StructureType structureType() {
		return DEATHBLOOM;
	}

}
