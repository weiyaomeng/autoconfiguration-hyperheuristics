package examples.rn;


import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;



/**
 * This class presents the ExampleHyperHeuristic1 that allows manually specified arguments.
 * Same as the ExampleHyperHeuristic1, it implements a random heuristic selection method and a Naive acceptance criteria.
 * It operates on heuristics of type MUTATION, LOCAL_SEARCH, RUIN_RECREATE.
 * For each of these heuristics, parameters IOM and DOS are applied.
 * 
 * @author Weiyao Meng (weiyao.meng2@nottingham.ac.uk)
 * @date 2024.04.04
 */

public class RN extends HyperHeuristic {
	

//	private Heuristic[] heuristics;
	
	// Default values for DOS and IOM parameters
	double[] dosValues = {0.2, 0.2, 0.2}, iomValues = {0.2, 0.2, 0.2}; 
	
	public RN(long seed) {
		super(seed);
	}
	
	/**
     * Constructs a new RN hyper-heuristic with the given seed and custom DOS/IOM values.
     * 
     * @param seed the seed value for random number generation
     * @param dosValue an array of custom DOS values for heuristics
     * @param iomValue an array of custom IOM values for heuristics
     */
	
	public RN(long seed, double[] dosValue, double[] iomValue){
		super(seed);
		this.dosValues = dosValue;
		this.iomValues = iomValue;
	}
	
	/**
	 * This method defines the strategy of the hyper-heuristic
	 * @param problem the problem domain to be solved
	 */
	public void solve(ProblemDomain problem) {

		// Variable to store the ID of the heuristic to apply
		int heuristic_to_apply = 0; 
		
		// Create heuristics array with customised configurations
		Heuristic[] heuristics = createHeuristics(problem, dosValues, iomValues);
		
		// Determine the set of heuristics to use based on the problem domain
		int[] heuristics_to_use = get_heuristics_to_use(problem);

		// Initialise the current solution and get its objective value
		problem.initialiseSolution(0);
		double current_obj_function_value = problem.getFunctionValue(0);
		
		// Set the first heuristic to apply
		Heuristic h = heuristics[0];

		//the main loop of any hyper-heuristic, which checks if the time limit has been reached
		while (!hasTimeExpired()) {

			//this hyper-heuristic chooses a random low level heuristic to apply
			int randomIndex = rng.nextInt(heuristics_to_use.length);
			heuristic_to_apply = heuristics_to_use[randomIndex];
			h = heuristics[heuristic_to_apply];
			
			// Set the dos and iom based on the selected LLH
			problem.setDepthOfSearch(h.getConfiguration().getDos());
			problem.setIntensityOfMutation(h.getConfiguration().getIom());

			//apply the chosen heuristic to the solution at index 0 in the memory
			//the new solution is then stored at index 1 of the solution memory while we decide whether to accept it
			double new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, 0, 1);
			
//			this.printHeuristicInfo(h);
//			System.out.println("Problem setting IOM: "+problem.getIntensityOfMutation()+" DOS: "+problem.getDepthOfSearch());
								
			//calculate the change in fitness from the current solution to the new solution
			double delta = current_obj_function_value - new_obj_function_value;

			//all of the problem domains are implemented as minimisation problems. A lower fitness means a better solution.
			if (delta > 0) {
				//if there is an improvement then we 'accept' the solution by copying the new solution into memory index 0
				problem.copySolution(1, 0);
				//we also set the current objective function value to the new function value, as the new solution is now the current solution
				current_obj_function_value = new_obj_function_value;
			} else {
				//if there is not an improvement in solution quality then we accept the solution with a 50% probability
				if (rng.nextBoolean()) {
					//the process for 'accepting' a solution is the same as above
					problem.copySolution(1, 0);
					current_obj_function_value = new_obj_function_value;
				}
			}
			//one iteration has been completed, so we return to the start of the main loop and check if the time has expired 
		}
	}
	
	/**
	 * this method must be implemented, to provide a different name for each hyper-heuristic
	 * @return a string representing the name of the hyper-heuristic
	 */
	public String toString() {
		return "Example Hyper Heuristic One for Configuration";
	}
	
	/**
	 * Creates an array of heuristic objects based on the problem domain and custom parameter values.
	 * 
	 * @param problem the problem domain
	 * @param dosValues an array of depth of search (DOS) parameter values
	 * @param iomValues an array of intensity of mutation (IOM) parameter values
	 * @return an array of configured heuristic objects
	 */
	private Heuristic[] createHeuristics(ProblemDomain problem, double[] dosValues, double[] iomValues) {
			
		// Initialise array to store heuristic objects
		int numHeuristics = problem.getNumberOfHeuristics();
		Heuristic[] heuristics = new Heuristic[numHeuristics];
		
		// Create heuristic objects with default configurations
    	for (int i = 0; i < numHeuristics; i++) {
            HeuristicConfiguration configuration = new HeuristicConfiguration(0.2, 0.2);
            heuristics[i] = new Heuristic(configuration, i); 
        }
        
    	// Retrieve heuristics that use DOS and IOM parameters
        int[] dosHeuristics = problem.getHeuristicsThatUseDepthOfSearch();
        int[] iomHeuristics = problem.getHeuristicsThatUseIntensityOfMutation();
 
        // Apply custom DOS values to corresponding heuristic IDs
        for (int i = 0; i < dosHeuristics.length; i++) {
            int id = dosHeuristics[i];
            double value = dosValues[i];
            heuristics[id].getConfiguration().setDos(value);
        }
        
        // Apply custom IOM values to corresponding heuristic IDs
        for (int i = 0; i < iomHeuristics.length; i++) {
            int id = iomHeuristics[i];
            double value = iomValues[i];
            heuristics[id].getConfiguration().setIom(value); 
        }

        return heuristics; //return the array heuristics containing all the configured heuristic objects.
    }
	
	/**
	 * Retrieves an array of heuristic IDs to use based on the problem domain types.
	 * 
	 * @param problem the problem domain
	 * @return an array of heuristic IDs to use
	 */
	private int[] get_heuristics_to_use(ProblemDomain problem) {
		// Retrieve heuristics of different types from the problem domain
		int[] mutations = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		int[] ruin_recreates = problem.getHeuristicsOfType(HeuristicType.RUIN_RECREATE);
		int[] local_searches = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		
		// Calculate total length of all heuristic types
		int totalLength = mutations.length + ruin_recreates.length + local_searches.length;
		
		// Create array to store heuristic IDs to use
		int[] heuristics_to_use = new int[totalLength];
		
		// Copy heuristic IDs of each type to the combined array
		System.arraycopy(mutations, 0, heuristics_to_use, 0, mutations.length);
		System.arraycopy(ruin_recreates, 0, heuristics_to_use, mutations.length, ruin_recreates.length);
		System.arraycopy(local_searches, 0, heuristics_to_use, mutations.length + ruin_recreates.length, local_searches.length);
		
		return heuristics_to_use; // Return the array of heuristic IDs to use
	}
	
	private void printHeuristicInfo(Heuristic h) {
		System.out.println("ID: "+h.getHeuristicId()+" IOM: "+h.getConfiguration().getIom()+" DOS: "+h.getConfiguration().getDos());
	}
	
	private void printHeuristicsInfo(Heuristic[] heuristics) {
		for(Heuristic h: heuristics) {
			this.printHeuristicInfo(h);
		}
	}
	
}
