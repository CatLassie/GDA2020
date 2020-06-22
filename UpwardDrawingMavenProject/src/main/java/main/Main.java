package main;

import static java.lang.System.out;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import optimization.SimulatedAnnealing;
import model.GraphInstance;
import model.Solution;
import util.InputParser;
import util.SolutionWriter;

public class Main {
	public static void main(String[] args) {
		String fileName = args[0];
		boolean verbose = args.length > 1 ? Boolean.parseBoolean(args[1]) : true;
		int timeLimit = args.length > 2 ? Integer.parseInt(args[2]) : 180 ;
		double coolingRate = args.length > 3 ? Double.parseDouble(args[3]) : 0.95;
		double stoppingCondition = args.length > 4 ? Double.parseDouble(args[4]) : 0.01;
		int equilibriumCoefficient = 0;
		
		String localDir = System.getProperty("user.dir").split("UpwardDrawingMavenProject")[0];
		String readPath = localDir + "\\instances\\" + fileName + ".json";
		String writePath = localDir + "\\solutions\\" + fileName + "_solution.json";
		String writePathOpt = localDir + "\\solutions\\" + fileName + "_solution_opt.json";
		
		GraphInstance inst = null;

		try {
			inst = InputParser.readInstance(readPath);
			
			int defaultCoefficient = (int) Math.sqrt(inst.getNodes().size());
			equilibriumCoefficient = args.length > 5 ? Integer.parseInt(args[5]) : defaultCoefficient;
			
			System.out.println("\nMain.main()");
			System.out.println("command line args:");
			System.out.println("file name: "+fileName+"");
			System.out.println("verbose: "+verbose+"");
			System.out.println("timeLimit: "+timeLimit+"sec.");
			System.out.println("cooling rate: "+coolingRate+"");
			System.out.println("stopping condition: "+stoppingCondition+"");
			System.out.println("equilibrium coefficient: "+equilibriumCoefficient+"\n");
		} catch (IOException e) {
			out.println("Sorry, couldnt open file!");
			System.exit(-1);
		}
		
		Solution initialSolution = new Solution(inst, verbose);
		initialSolution.computeInitialFeasibleSolution();
		
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(new Solution(initialSolution),
				   coolingRate, equilibriumCoefficient, stoppingCondition, timeLimit, verbose);
		if(verbose) {
			System.out.println("SIMULATED ANNEALING:");
			System.out.println(simulatedAnnealing+"");	
		}
		double annealingStartCPU = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
		Solution bestSolution = simulatedAnnealing.search();
		double annealingEndCPU = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
		double annealingDiffCPU = (annealingEndCPU - annealingStartCPU)/1000000000;
		if(verbose) {
			System.out.println("CPU time: "+annealingDiffCPU+"\n");
		}
		
		out.println("final cost is: " + bestSolution.getCost());
		
		// Write initial solution to file
		try {
			SolutionWriter.writeSolution(writePath, initialSolution.getGraphInstanceFromSolution());
		} catch (IOException e) {
			out.println("Sorry, couldnt write to file!");
			System.exit(-1);
		}
		
		// Write final solution to file
		try {
			SolutionWriter.writeSolution(writePathOpt, bestSolution.getGraphInstanceFromSolution());
		} catch (IOException e) {
			out.println("Sorry, couldnt write to file!");
			System.exit(-1);
		}
	}
}
