package optimization;

import static java.lang.System.out;
import java.util.concurrent.ThreadLocalRandom;

import model.Node;
import model.Solution;
import util.Utilities;

public class SimulatedAnnealing {
	
	private Solution bestSolution;
	private Solution currentSolution;
	private double temperature; // // f-max - f-min (which is the vertex number)
	private double coolingRate; // temperature cooling rate (e.g. 0.95)
	private int equilibriumCondition; // number of moves before temperature adjustment (multiple of vertex number, e.g. n*(sqrt(n)))
	private double stoppingCondition; // minimum temperature (e.g. 0.01)
	private int timeLimit;
	private int timeCounter = 0;
	private boolean verbose;
	
	public SimulatedAnnealing (Solution solution, double coolingRate, int equilibriumCoefficient, double stoppingCondition, int timeLimit, boolean verbose) {
		this.bestSolution = new Solution(solution);
		this.currentSolution = solution;
		this.temperature = solution.getNodes().size();
		this.coolingRate = coolingRate;
		this.equilibriumCondition = equilibriumCoefficient * solution.getNodes().size();
		this.stoppingCondition = stoppingCondition;
		this.timeLimit = timeLimit;
		this.verbose = verbose;
	}
	
	public Solution search() {
		if(verbose) {
			out.print("\ntime elapsed (sec): ");	
		}
		Utilities.startTimer();
		while((temperature > stoppingCondition) && !Utilities.isTimeOver(timeLimit)) {
			oneTemperatureLevelSearch();
			// System.out.println(temperature);
			temperature = temperature * coolingRate;	
		}
		if(verbose) {
			out.println("\n");
		}
		return bestSolution;
	}
	
	private void oneTemperatureLevelSearch() {
		equilibriumCycle:
		for(int i = 0; i < equilibriumCondition && !Utilities.isTimeOver(timeLimit); i++) {
			
			if(verbose) {
				if(Utilities.elapsedTime() > timeCounter * 5) {
					if((timeCounter+1) % 15 == 0) {
						out.println(Math.round(Utilities.elapsedTime()) +", ");	
					} else {
						out.print(Math.round(Utilities.elapsedTime()) +", ");	
					}
					timeCounter++;
				}
			}
			
			// System.out.println(i);
			int randomNodeId = ThreadLocalRandom.current().nextInt(0, currentSolution.getNodes().size());
			Node randomNode = currentSolution.getNodes().get(randomNodeId);
			int randomX = ThreadLocalRandom.current().nextInt(0, currentSolution.getWidth() + 1);
			
			if(!currentSolution.isMoveFeasible(randomNode, randomX)) {
				continue equilibriumCycle;
			}
			
			Solution nextSolution = new Solution(currentSolution);
			randomNode = nextSolution.getNodes().get(randomNodeId);
			nextSolution.performMove(randomNode, randomX, 0);
			nextSolution.calculateCost();
			
			if(nextSolution.getCost() < currentSolution.getCost()) {
				currentSolution = nextSolution;
				if(currentSolution.getCost() < bestSolution.getCost()) {
					bestSolution = new Solution(currentSolution);
				}
			} else {
				double e = java.lang.Math.E;
				double costDifference = Math.abs(nextSolution.getCost() - currentSolution.getCost());
				double metrolopisCriterion = Math.pow(e, -(costDifference / temperature));
				double probability = (double)ThreadLocalRandom.current().nextInt(0, 1000)/1000;
				// if(i == 0) {
				//	System.out.println(metrolopisCriterion);
				// }
				if(probability < metrolopisCriterion) {
					currentSolution = nextSolution;
				}
			}
			
		}
	}
	
	public String toString() {
		String annealing = "initial temperature: " + temperature + "\n";
		annealing += "cooling rate: " + coolingRate + "\n";
		annealing += "equilibrium condition: " + equilibriumCondition + " iterations\n";
		annealing += "stopping condition: " + stoppingCondition + " minimum temperature";
		return annealing;
	}
}
