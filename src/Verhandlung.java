import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//SIMULATION!

/*
 * Was ist das "Problem" der nachfolgenden Verhandlung?
 * - Frühe Stagnation, da die Agenten frühzeitig neue Contracte ablehnen
 * - Verhandlung ist nur für wenige Agenten geeignet, da mit Anzahl Agenten auch die Stagnationsgefahr wächst
 * 
 * Aufgabe: Entwicklung und Anaylse einer effizienteren Verhandlung. Eine Verhandlung ist effizienter, wenn
 * eine frühe Stagnation vermieden wird und eine sozial-effiziente Gesamtlösung gefunden werden kann.
 * 
 * Ideen:
 * - Agenten müssen auch Verschlechterungen akzeptieren bzw. zustimmen, die einzuhaltende Mindestakzeptanzrate wird vom Mediator vorgegeben
 * - Agenten schlagen alternative Kontrakte vor
 * - Agenten konstruieren gemeinsam einen Kontrakt
 * - In jeder Runde werden mehrere alternative Kontrakte vorgeschlagen
 * - Alternative Konstruktionsmechanismen für Kontrakte
 * - Ausgleichszahlungen der Agenten (nur möglich, wenn beide Agenten eine monetaere Zielfunktion haben
 * 
 */

public class Verhandlung {

	public static void main(String[] args) {
		ArrayList<int[]> contracts = new ArrayList<int[]>(), proposalList = new ArrayList<int[]>();
		int[] contract, proposal;
		Agent agA, agB;
		Mediator med;
		int maxRounds, round;
		boolean[] votesA = null, votesB = null;

		try {
			agA = new SupplierAgent(new File("data/daten3ASupplier_200.txt"));
			agB = new CustomerAgent(new File("data/daten4BCustomer_200_5.txt"));
			med = new Mediator(agA.getContractSize(), agB.getContractSize());

			// Verhandlung initialisieren
			contract = med.initContract(); // Vertrag=Lösung=Jobliste
			maxRounds = 5; // Verhandlungsrunden
			ausgabe(agA, agB, 0, contract);

			boolean firstround = true;

			int index = 1;
			
			while (true) {
				// Verhandlung starten
				proposalList.clear();
				proposalList = contracts;
				contracts.clear();
				for (round = 0; round < maxRounds; round++) {
					if (firstround) {
						for (int j = 0; j < 10; j++) {
							proposalList.add(med.constructProposal(contract));
						}
						firstround = false;
					} else {
						
					}
					votesA = agA.vote(proposalList); // Autonomie + Private Infos
					votesB = agB.vote(proposalList);

					for (int i = 0; i < votesA.length; i++) {
						if (votesA[i] && votesB[i]) {
							System.out.println("Added Contract " + index++);
							contracts.add(proposalList.get(i));
						}
					}
				}
				
				System.out.println("C-Size: " + contracts.size());
				if (contracts.size() <= 1) {
					contract = contracts.get(0);
					break;
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void ausgabe(Agent a1, Agent a2, int i, int[] contract) {
		System.out.print(i + " -> ");
		a1.printUtility(contract);
		System.out.print("  ");
		a2.printUtility(contract);
		System.out.println();
	}

}