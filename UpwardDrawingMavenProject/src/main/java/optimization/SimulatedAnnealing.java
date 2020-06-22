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
	
	public SimulatedAnnealing (Solution solution, double coolingRate, int equilibriumCoefficient, double stoppingCondition) {
		this.bestSolution = new Solution(solution);
		this.currentSolution = solution;
		this.temperature = solution.getNodes().size();
		this.coolingRate = coolingRate;
		this.equilibriumCondition = equilibriumCoefficient * solution.getNodes().size();
		this.stoppingCondition = stoppingCondition;
	}
	
	public Solution search() {
		Utilities.startTimer();
		while((temperature > stoppingCondition) && !Utilities.isTimeOver()) {
			oneTemperatureLevelSearch();
			// System.out.println(temperature);
			temperature = temperature * coolingRate;	
		}
		return currentSolution;
	}
	
	private void oneTemperatureLevelSearch() {
		equilibriumCycle:
		for(int i = 0; i < equilibriumCondition; i++) {
			int randomNodeId = ThreadLocalRandom.current().nextInt(0, currentSolution.getNodes().size());
			Node randomNode = currentSolution.getNodes().get(randomNodeId);
			int randomX = ThreadLocalRandom.current().nextInt(0, currentSolution.getWidth() + 1);
			
			if(!currentSolution.isMoveFeasible(randomNode, randomX)) {
				continue equilibriumCycle;
			}
			
			/*
			int costChange = currentSolution.computeMoveCost(randomNode, randomX);
			int newCost = currentSolution.getCost() + costChange;
						
			if(newCost < currentSolution.getCost()) {
				currentSolution.performMove(randomNode, randomX, costChange);
			} else {
				double e = java.lang.Math.E;
				double costDifference = Math.abs(costChange);
				double metrolopisCriterion = Math.pow(e, -(costDifference / temperature));
				double probability = (double)ThreadLocalRandom.current().nextInt(0, 1000)/1000;
				
				//if(i == 0) {
				//	System.out.println(metrolopisCriterion);
				//}
				
				if(probability < metrolopisCriterion) {
					currentSolution.performMove(randomNode, randomX, costChange);
				}
			}
			*/
			 
			
			

			
			Solution nextSolution = new Solution(currentSolution);
			randomNode = nextSolution.getNodes().get(randomNodeId);
			nextSolution.performMove(randomNode, randomX, 0);
			nextSolution.calculateCost();
			
			if(nextSolution.getCost() < currentSolution.getCost()) {
				currentSolution = nextSolution;
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
