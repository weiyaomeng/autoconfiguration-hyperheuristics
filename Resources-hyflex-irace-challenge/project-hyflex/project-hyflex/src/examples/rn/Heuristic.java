package examples.rn;


public class Heuristic {

	private final HeuristicConfiguration configuration;
	
//	private final HeuristicData data;
	
	private final int heuristicId;
	
	public Heuristic(HeuristicConfiguration configuration, int heuristicId) {
		
		this.configuration = configuration;
		this.heuristicId = heuristicId;
	}

	public HeuristicConfiguration getConfiguration() {
		return configuration;
	}

//	public HeuristicData getData() {
//		return data;
//	}

	public int getHeuristicId() {
		return heuristicId;
	}
}
