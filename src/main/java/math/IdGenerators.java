package math;

public class IdGenerators {

	// TODO: Figure out how to make everyone keep the npcIdGenerator in sync
	private IdGenerator personalIdGenerator;
	private IdGenerator npcIdGenerator;

//	private Map<player uuid, IdGenerator> peerIdToGenerator;

	public IdGenerators(int personalIdRange, long nextNpcId) {
		personalIdGenerator = new IdGenerator(personalIdRange);
		npcIdGenerator = new IdGenerator();
		npcIdGenerator.setNextId(nextNpcId);
	}

	public long genId() {
		return personalIdGenerator.genId();
	}

	public long genNpcId() {
		return npcIdGenerator.genId();
	}

	public IdGenerator personalIdGenerator() {
		return personalIdGenerator;
	}

	public IdGenerator npcIdGenerator() {
		return npcIdGenerator;
	}

}
