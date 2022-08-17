package context.synctime;

import context.data.GameData;
import context.game.data.DebugTools;
import debugui.RollingAverageStat;

public class SyncTimeData extends GameData {

	private final DebugTools tools;

	private volatile long t0 = -1;
	private volatile long t3 = -1;

	private RollingAverageStat rttStat;
	private RollingAverageStat timeOffsetStat;

	public SyncTimeData(DebugTools tools) {
		this.tools = tools;
	}

	@Override
	protected void init() {
		rttStat = new RollingAverageStat(10, resourcePack());
		timeOffsetStat = new RollingAverageStat(10, resourcePack());
	}

	public DebugTools tools() {
		return tools;
	}

	public long t0() {
		return t0;
	}

	public void setT0(long t0) {
		this.t0 = t0;
	}

	public long t3() {
		return t3;
	}

	public void setT3(long t3) {
		this.t3 = t3;
	}

	public RollingAverageStat rttStat() {
		return rttStat;
	}

	public RollingAverageStat timeOffsetStat() {
		return timeOffsetStat;
	}

}
