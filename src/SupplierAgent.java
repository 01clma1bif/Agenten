import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SupplierAgent extends Agent{

	private int[][] costMatrix;
	
	public SupplierAgent(File file) throws FileNotFoundException{
//		try{
			Scanner scanner = new Scanner(file);
			int dim         = scanner.nextInt();
			costMatrix      = new int[dim][dim];
			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					int x = scanner.nextInt();
					costMatrix[i][j] = x;
				}
			}			
			scanner.close();
//		}
//		catch(FileNotFoundException e){
//			System.out.println(e.getMessage());
//        }
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
	
	public int getContractSize(){
		return costMatrix.length;
	}
	

	public void printUtility(int[] contract){
		System.out.print(evaluate(contract));
	}
	
	
	
	/*
	 * Ab hier private Methoden 
	 */
	
	private int evaluate(int[] contract){
		//WICHTIG: Diese Methode muss private sein!
		//Nur zu Analyse-Zwecken darf diese auf public gesetzt werden
		
		int result = 0;
		for(int i=0;i<contract.length-1;i++) {
			int zeile  = contract[i];
			int spalte = contract[i+1];
			result    += costMatrix[zeile][spalte];
		}
		
		return result;
	}
	

	
	
}