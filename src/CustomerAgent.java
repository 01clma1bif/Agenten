import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class CustomerAgent extends Agent{

	private int[][] timeMatrix;
	private int[][] delayMatrix;//wird anhand der timeMatrix berechnet
	int best = 0;
	
	
	public CustomerAgent(File file) throws FileNotFoundException{
	//	try {
			Scanner scanner = new Scanner(file);
			int jobs = scanner.nextInt();
			int machines = scanner.nextInt();
			timeMatrix = new int[jobs][machines];
			for (int i = 0; i < timeMatrix.length; i++) {
				for (int j = 0; j < timeMatrix[i].length; j++) {
					int x = scanner.nextInt();
					timeMatrix[i][j] = x;
				}
			}
			calculateDelay(timeMatrix.length);
			
			scanner.close();
//		} catch (FileNotFoundException e) {
//			System.out.println(e.getMessage());
//		}
	}
	public boolean[] vote(ArrayList<int[]> proposalList) {
		boolean[] votes = new boolean[proposalList.size()];
		Arrays.fill(votes, Boolean.FALSE);
		int[] costProposal = new int[proposalList.size()];
		for(int i = 0; i < proposalList.size(); i++) {
			costProposal[i] = evaluate(proposalList.get(i));
		}
		for(int k = 0; k < proposalList.size()/2; k++) {
			int temp = 1000000;
			int temp2 = 0;
			for(int j = 0; j < costProposal.length; j++) {
				if(costProposal[j] < temp && costProposal[j] != -1) {
					temp = costProposal[j];
					temp2 = j;
				}
			}
			costProposal[temp2] = -1;
			votes[temp2] = true;
		}
		
		return votes;
	}
	

	public int getContractSize() {
		return timeMatrix.length;
	}

	

	public void printUtility(int[] contract){
		System.out.print(evaluate(contract));
	}
	
	
	/*
	 * Ab hier private Methoden
	 */

	private void calculateDelay(int jobNr){
		delayMatrix = new int[jobNr][jobNr];
		for (int h = 0; h < jobNr; h++){
			for (int j = 0; j < jobNr; j++){
				delayMatrix[h][j] = 0;
				if(h!=j){
					int maxWait = 0;
					for(int machine = 0; machine < timeMatrix[0].length; machine++){
						int wait_h_j_machine;						
						
						int time1 = 0;
						for(int k=0; k<=machine; k++){
							time1 += timeMatrix[h][k];
						}
						int time2 = 0;
						for(int k=1; k<=machine; k++){
							time2 += timeMatrix[j][k-1];
						}
						wait_h_j_machine = Math.max(time1-time2, 0);
						if(wait_h_j_machine > maxWait)maxWait = wait_h_j_machine;
					}
					delayMatrix[h][j] = maxWait;
				}
			}
		}		
	}
	
	
	private int evaluate(int[] contract) {
		// WICHTIG: Diese Methode muss private sein!
		// Nur zu Analyse-Zwecken darf diese auf public gesetzt werden

		int result = 0;

		for (int i = 1; i < contract.length; i++) {//starte bei zweitem Job (also Index 1)
			int jobVor  = contract[i-1];
			int job     = contract[i];
			result     += delayMatrix[jobVor][job];
		}
		
		int lastjob = contract[contract.length-1];
		for (int machine = 0; machine < timeMatrix[0].length; machine++) {
			result  +=  timeMatrix[lastjob][machine];
		}
		

		return result;
	}

}
