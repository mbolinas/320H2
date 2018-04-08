/*
 * @Marc Bolinas
 * 3/18/18
 * CISC320 HW2
 */

package pkgMain;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Cmain {
	
	static String input = "K:\\Downloads\\input.txt";
	static String output = "K:\\Downloads\\output.txt";
	static HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
	
	static Comparator<Entry<Integer, Team>> valueComparator = new Comparator<Entry<Integer,Team>>(){
		@Override
		public int compare(Entry<Integer, Team> e1, Entry<Integer, Team> e2) {
			if(e1.getValue().total_riddles == e2.getValue().total_riddles) {
				if(e1.getValue().total_penalty_time == e2.getValue().total_penalty_time) {
					if(e1.getValue().team_number < e2.getValue().team_number) {
						return -1;
					}
					return 1;
				}
				else {
					if(e1.getValue().total_penalty_time < e2.getValue().total_penalty_time) {
						return -1;
					}
					return 1;
				}
			}
			else {
				if(e1.getValue().total_riddles < e2.getValue().total_riddles) {
					return 1;
				}
				return -1;
			}
		}
	};
	
	public static void main(String[] args) throws IOException{
		
		read_file();
		write_file();
		

	}

	public static void read_file() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line = reader.readLine();
		
		while(line != null) {
			String[] values = line.split(" ");
			int tnum = Integer.parseInt(values[0]);
			int rnum = Integer.parseInt(values[1]);
			boolean correct = false;
			if(values[3].equals("C")) 
				correct = true;
			
			if(values[3].equals("I") || correct) {//we only care if it's an I or a C
				if(teams.get(tnum) == null) {//if the team does not exist, create it and the riddle
					
					Team t = new Team(tnum);
					Riddle r = new Riddle(rnum);
					if(correct) {
						r.solved = true;
						t.total_penalty_time = t.total_penalty_time + Integer.parseInt(values[2]) + (r.submission_count * 5);
						t.total_riddles++;
					}
					else {
						r.solved = false;
						r.submission_count++;
					}
					t.riddles.put(r.riddle_number, r);
					teams.put(t.team_number, t);
				}
				else {//if the team exists
					Team t = teams.get(tnum);
					if(t.riddles.containsKey(rnum)) {//update the riddle if it exists
						Riddle r = t.riddles.get(rnum);
						if(correct) {
							r.solved = true;
							t.total_penalty_time = t.total_penalty_time + Integer.parseInt(values[2]) + (r.submission_count * 5);
							t.total_riddles++;
						}
						else {
							r.solved = false;
							r.submission_count++;
						}
					}
					else {//make new riddle if it does not exist
						Riddle r = new Riddle(rnum);
						if(correct) {
							r.solved = true;
							t.total_penalty_time = t.total_penalty_time + Integer.parseInt(values[2]) + (r.submission_count * 5);
							t.total_riddles++;
						}
						else {
							r.solved = false;
							r.submission_count++;
						}
					}
				}
			}
			
			line = reader.readLine();
		}
		
		reader.close();
	}

	public static void write_file() throws IOException{
		
		/*
		 * things get pretty dirty here - I realized that while using a HashMap stored data in O(1) time, it's difficult to 
		 * sort a HashMap because it was not designed to be sorted
		 * 
		 * one way around this was to convert the HashMap into a Set, then into an (Array)List, and sort it using Collections.sort()
		 * via a custom comparator. the custom comparator was needed because the list was sorted differently depending on if the # riddles
		 * were the same, # penalty time, etc
		 */
		List<Entry<Integer, Team>> list_teams = new ArrayList<Entry<Integer, Team>>(teams.entrySet());
		Collections.sort(list_teams, valueComparator);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		
		for(Entry<Integer, Team> entry : list_teams	) {
			String result = entry.getValue().team_number + " " + entry.getValue().total_riddles + " " + entry.getValue().total_penalty_time;
			writer.write(result);
			writer.newLine();
			//System.out.println(entry.getValue().team_number + " -> " + entry.getValue().total_riddles + " -> " + entry.getValue().total_penalty_time);
		}
		
		writer.close();
	}

}
