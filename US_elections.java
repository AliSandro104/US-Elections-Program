import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {

	public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){
		// Knapsack problem where the values are the number of people still required to win the state
		// and the weights are the number of delegates per state
		// In the DP array, we will be looking for the minimum value achieved having at least a weight w 
		
		int [] values = new int[num_states];
		
		for (int i = 0; i < num_states; i++) {
			
			values[i] = ((votes_Biden[i] + votes_Trump[i] + votes_Undecided[i])/2 + 1) - votes_Biden[i];
			// compute the number of people still required to win the state
			
			if (values[i] > votes_Undecided[i]) {
				// if Trump has already won the state i, store INF to help us in the calculations
				values[i] = Integer.MAX_VALUE;
			
			} else if (values[i] < 0) {
				// if Biden has already won the state i, store 0
				values[i] = 0;
			}
		}
		
		int sum_delegates = 0;
		
		for (int state : delegates) {
			
			sum_delegates = sum_delegates + state;
		}
		// sum all delegates
		
		if (sum_delegates == 0) {
			
			return -1;
		}
		
		int required_to_win = sum_delegates/2 + 1;
		// compute the required number of delegates to win the election
		
		int [][] min_value = new int[num_states + 1][required_to_win+1];
		// Initialize dynamic programming array
		
		for (int i = 0; i <= required_to_win; i++) {
			
			min_value[0][i] = Integer.MAX_VALUE;
		}
		// store INF in the first row of the table (when row = 0)
		
		int last_sum = 0;
		// temp variable to help us in the comparisons
		
		for (int i = 1; i <= num_states; i++) {
			
			for (int w = required_to_win; w >= 0; w--) {
				
				if (delegates[i-1] + last_sum < w) {
					
					min_value[i][w] = Integer.MAX_VALUE;
				// if we cannot achieve the required weight with all available states, store INF	
				
				} else {
					
					if (w <= delegates[i-1] || values[i-1] == Integer.MAX_VALUE) {
					
						min_value[i][w] = Math.min(min_value[i-1][w], values[i-1]);
						
						// if the current state has weight bigger than w, we take min{min_value[i-1][w], value of the current state}
					
					} else {
						
						min_value[i][w] = Math.min(min_value[i-1][w], values[i-1] + min_value[i-1][w-delegates[i-1]]);
						// else, we use the same recursion as the one in the slides for the knapsack problem (with min instead of max)
					}
				}
			}
			if (values[i-1] != Integer.MAX_VALUE) {
				
				last_sum += delegates[i-1];
				// update the last_sum
			}
		}
		
		int minimum_people = min_value[num_states][required_to_win];
		// get the answer from the DP array in the last row at index "required_to_win"
		
		if (minimum_people != Integer.MAX_VALUE) {
			
			return minimum_people;
		}
		// if the answer is infinity, return -1
		
		return -1;
	}
	
	
	public static void main(String[] args) {
		
	 try {
			String path = args[0];
      File myFile = new File(path);
      Scanner sc = new Scanner(myFile);
      int num_states = sc.nextInt();
      int[] delegates = new int[num_states];
      int[] votes_Biden = new int[num_states];
      int[] votes_Trump = new int[num_states];
 			int[] votes_Undecided = new int[num_states];	
      for (int state = 0; state<num_states; state++){
			  delegates[state] =sc.nextInt();
				votes_Biden[state] = sc.nextInt();
				votes_Trump[state] = sc.nextInt();
				votes_Undecided[state] = sc.nextInt();
      }
      sc.close();
      int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
      	System.out.println(answer);
    	} catch (FileNotFoundException e) {
      	System.out.println("An error occurred.");
      	e.printStackTrace();
    	}
  	}
	
}