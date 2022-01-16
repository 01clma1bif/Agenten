import java.util.ArrayList;
import java.util.List;

public abstract class Agent {

	public abstract boolean[] vote(ArrayList<int[]> proposalList);
	public abstract void    printUtility(int[] contract);
	public abstract int     getContractSize();
}
