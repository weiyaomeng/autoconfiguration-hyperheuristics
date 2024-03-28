package examples.rn;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import travelingSalesmanProblem.TSP;

/**
 * This class shows how to run the example hyper-heuristic with manual specified arguments.
 * When executing this class, please ensure to set the run configurations. 
 * The "-p" (instance ID) and "-t" (termination time) options are mandatory: e.g., -p 6 -t 10000
 * Full list of arguments:
 * -insseed <instance-seed>   : Seed for the instance generator (default: 1234)
 * -algseed <algorithm-seed>  : Seed for the algorithm (default: 5678)
 * -p <instance-id>           : ID of the problem instance to use
 * -t <termination-time>      : Termination time for the hyper-heuristic in milliseconds
 * -d <DOS-values>            : Depth of Search (DOS) values separated by spaces (default: 0.2 0.2 0.2)
 * -i <IOM-values>            : Intensity of Mutation (IOM) values separated by spaces (default: 0.2 0.2 0.2)
 * Example usage: 
 * -p 6 -t 10000
 * -p 6 -t 10000 -insseed 20240408 -algseed 20240409 -d 0.2 0.4 0.6 -i 0.1 0.3 0.5
 * 
 * @author Weiyao Meng (weiyao.meng2@nottingham.ac.uk)
 * @date 2024.03.26
 */
public class RNRunner {

	public static void main(String[] args) {
		
		// Default values for dos and iom arrays
		double[] dos = {0.2, 0.2, 0.2};
		double[] iom = {0.2, 0.2, 0.2};
		// Default seeds
		long insseed=1234, algseed=5678;
		 
		// Flag to track whether -p is specified
		boolean pSpecified = false, tSpecified = false; 
		int insid=0; long time=0; 
		
		//-------------------------------Parse command line arguments for parameters--------------------------
		// Parse command line arguments
		for (int i = 0; i < args.length; i++) {
		    switch (args[i]) {
			 	// seed
	        	case "-insseed":
	        		insseed = Long.parseLong(args[++i]);
	        		break;
	        	case "-algseed":
	        		algseed = Long.parseLong(args[++i]);
	        		break;
			 	// instance id
	            case "-p":
	            	insid = Integer.parseInt(args[++i]);
	            	pSpecified = true;
	                break;
	             // termination time
                case "-t":
                	time = Long.parseLong(args[++i]);
                	tSpecified = true;
                	break;
            	// Parse dos / iom values
		        case "-d":
		        	parseValues(args, dos, i++);
		            break;
		        case "-i":
		        	parseValues(args, iom, i++);
		            break;
		        default:
		            break; // Handle unknown arguments if needed
		    }
		}
		// Check if -p and -t are specified
        if (!pSpecified || !tSpecified) {
            System.out.println("Error: -p and -t are required.");
            System.exit(1);
        }
        
        //-------------------------------Execute the example HH---------------------------------

		//create a ProblemDomain object with a seed for the random number generator
		ProblemDomain problem = new TSP(insseed);

		//creates an ExampleHyperHeuristic object with a seed for the random number generator
		HyperHeuristic hyper_heuristic_object = new RN(algseed, dos, iom);

		//we must load an instance within the problem domain, in this case we choose instance 2
		problem.loadInstance(insid);

		//we must set the time limit for the hyper-heuristic in milliseconds, in this example we set the time limit to 1 minute
		hyper_heuristic_object.setTimeLimit(time);

		//a key step is to assign the ProblemDomain object to the HyperHeuristic object. 
		//However, this should be done after the instance has been loaded, and after the time limit has been set
		hyper_heuristic_object.loadProblemDomain(problem);
		
		// Print information before running the hyper-heuristic
//		printAlgorithmInfo(problem, hyper_heuristic_object, time, insid, insseed, algseed); // Note: need to comment this out before exporting a jar

		//now that all of the parameters have been loaded, the run method can be called.
		//this method starts the timer, and then calls the solve() method of the hyper_heuristic_object.
		hyper_heuristic_object.run();
		
		// Obtain the best solution found within the time limit
        System.out.println(hyper_heuristic_object.getBestSolutionValue());

	}
	
	// Helper method to parse DOS and IOM values from command line arguments
    private static void parseValues(String[] args, double[] values, int index) {
        for (int j = 0; j < values.length && index + 1 < args.length; j++) {
            values[j] = Double.parseDouble(args[++index]);
        }
    }
    
    // Print algorithm information before running
    private static void printAlgorithmInfo(ProblemDomain problem, HyperHeuristic hyperHeuristic, long time, int insid, long insseed, long algseed) {
        System.out.println("Algorithm: " + hyperHeuristic.toString());
        System.out.println("Problem instance: " + problem.getClass() + " ID: " + insid);
        System.out.println("Time limit set to: " + time / 1000 + " seconds");
        System.out.println("Instance seed: " + insseed + ". Algorithm seed: " + algseed);
        System.out.println("Search ...");
    }
}