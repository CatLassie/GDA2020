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
		/*
		String instanceN = args[0];
		String constrType = args[1];
		String neighbourhoodType = args[2] ;
		double coolingRate = args.length > 3 ? Double.parseDouble(args[3]) : 0.95;
		double stoppingCondition = args.length > 4 ? Double.parseDouble(args[4]) : 0.01;
		int equilibriumCoefficient = 0;
		*/
		double coolingRate = 0.95;
		double stoppingCondition = 0.01;
		int equilibriumCoefficient = 0;
		
		

		String fileName = "auto9";
		boolean verbose = true;

		String localDir = System.getProperty("user.dir").split("UpwardDrawingMavenProject")[0];
		// String readPath = localDir + "\\instances\\vc-exact_"+instanceN+".gr";
		// String writePath = localDir + "\\solutions\\vc-exact_"+instanceN+".gr";
		String readPath = localDir + "instances\\" + fileName + ".json";
		String writePath = localDir + "solutions\\" + fileName + "_solution.json";
		// out.println(readPath);
		
		GraphInstance inst = null;

		try {
			inst = InputParser.readInstance(readPath);
			
			int defaultCoefficient = (int) Math.sqrt(inst.getNodes().size());
			// equilibriumCoefficient = args.length > 5 ? Integer.parseInt(args[5]) : defaultCoefficient;
			equilibriumCoefficient = defaultCoefficient;

		} catch (IOException e) {
			out.println("Sorry, couldnt open file!");
			System.exit(-1);
		}
		
		Solution initialSolution = new Solution(inst, verbose);
		initialSolution.computeInitialFeasibleSolution();
		

		/*
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(new Solution(initialSolution),
				   coolingRate, equilibriumCoefficient, stoppingCondition);

		System.out.println("SIMULATED ANNEALING:");
		System.out.println(simulatedAnnealing+"");
		double annealingStartCPU = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
		Solution bestSolution = simulatedAnnealing.search();
		double annealingEndCPU = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
		double annealingDiffCPU = (annealingEndCPU - annealingStartCPU)/1000000000;
		System.out.println("CPU time: "+annealingDiffCPU+"\n");
		*/
		
		
		// Write initial solution to file
		try {
			SolutionWriter.writeSolution(writePath, initialSolution.getGraphInstanceFromSolution());
		} catch (IOException e) {
			out.println("Sorry, couldnt write to file!");
			System.exit(-1);
		}
		
		// Write solution to file
		/*
		try {
			SolutionWriter.writeSolution(writePath, bestSolution.getGraphInstanceFromSolution());
		} catch (IOException e) {
			out.println("Sorry, couldnt write to file!");
			System.exit(-1);
		}
		*/
		
		
		// out.println(inst);
		// out.println("\n"+ solution.getGraphInstanceFromSolution());
		// out.println("\n" + solution);
	}

}
