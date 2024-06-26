package examples.example;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import travelingSalesmanProblem.TSP;

/**
 * This class shows how to run a selected hyper-heuristic on a selected problem domain.
 * It shows the minimum that must be done to test a hyper heuristic on a problem domain, and it is 
 * intended to be read before other example classes provided in HyFlex website, which provides more complex set-ups
 */
public class ExampleRun1 {

	public static void main(String[] args) {

		//create a ProblemDomain object with a seed for the random number generator
		ProblemDomain problem = new TSP(1234);

		//creates an ExampleHyperHeuristic object with a seed for the random number generator
		HyperHeuristic hyper_heuristic_object = new ExampleHyperHeuristic1(5678);

		//we must load an instance within the problem domain, in this case we choose instance 2
		problem.loadInstance(0);

		//we must set the time limit for the hyper-heuristic in milliseconds, in this example we set the time limit to 1 minute
		hyper_heuristic_object.setTimeLimit(60000);

		//a key step is to assign the ProblemDomain object to the HyperHeuristic object. 
		//However, this should be done after the instance has been loaded, and after the time limit has been set
		hyper_heuristic_object.loadProblemDomain(problem);
		
		// Print information before running the hyper-heuristic
        System.out.println("Algorithm: "+hyper_heuristic_object.toString());
        System.out.println("Problem instance: " + problem.getClass());
        System.out.println("Time limit set to: " + hyper_heuristic_object.getTimeLimit()/1000 + " seconds");
        System.out.println("Search ...");


		//now that all of the parameters have been loaded, the run method can be called.
		//this method starts the timer, and then calls the solve() method of the hyper_heuristic_object.
		hyper_heuristic_object.run();
		
		// Obtain the best solution found within the time limit
        System.out.println("Best solution value found: " + hyper_heuristic_object.getBestSolutionValue());

	}
}