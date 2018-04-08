package pkgMain;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {
	final int team_number;
	int total_penalty_time = 0;
	int total_riddles = 0;
	HashMap<Integer, Riddle> riddles = new HashMap<Integer, Riddle>();
	
	public Team(int i) {
		team_number = i;
	}
}
