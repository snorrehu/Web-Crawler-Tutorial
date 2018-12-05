package Categorizers;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class PrimePeopleCategorizer {
    private ArrayList<String> executiveJobs = new ArrayList<String>();
    private ArrayList<String> EngineeringJobs = new ArrayList<String>();
    private ArrayList<String> itJobs = new ArrayList<String>();
    private ArrayList<String> financeJobs = new ArrayList<String>();

    PrimePeopleCategorizer(){
        executiveJobs = makeArraylist("/csvfiles/Executive.csv");
    }

    public ArrayList<String> makeArraylist(String file){
        String csvFile = file;
        ArrayList<String> list = new ArrayList<String>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                list.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
