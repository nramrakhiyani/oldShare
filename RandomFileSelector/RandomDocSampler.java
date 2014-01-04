import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashSet;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

//Code Contributed by: N Ramrakhiyani
//Development Date: 07-July-2012
//Purpose: The code helps in selecting random files from a directory. The source directories and number of files required can be set in the associated configuration file - RandomDocSampler.conf
public class RandomDocSampler
{
	//Method: main
	//Input: Command line arguments in String array - args
	//Return: None
	public static void main(String args[])
	{
		//All variable declarations and intial assignments
		File confFile = new File("RandomDocSampler.conf");
		String line;
		BufferedReader br = null;
		BufferedWriter bw = null;
		String [] inputDirs = null;
		int numRandFiles = 0, totalFiles = 0, k = 0, newNum = 0;
		ArrayList<String> allFiles = new ArrayList<String>();
		HashSet<Integer> randNumsCheck = new HashSet<Integer>();
		System.out.println("Reading configurations from RandomDocSampler.conf");

		//Main Try Catch Block
		try
        	{

			//Reading the configuration file and displaying the configurations------------------------------------------------
			br = new BufferedReader(new FileReader(confFile));

			while((line = br.readLine()) != null)
			{
				if(line.substring(0, line.indexOf("=")).equals("NumberOfFiles"))
                		{
					numRandFiles = Integer.parseInt(line.substring(line.indexOf("=")+1));
                		}
                		else if(line.substring(0, line.indexOf("=")).equals("ReadDirectories"))
                		{
                			inputDirs = line.substring(line.indexOf("=")+1).split(",");
                		}
		    	} 	
			br.close();

			System.out.println("Number of Random files to be selected: " + numRandFiles);
			System.out.println("Directories to be read:");
			for(int j = 0; j < inputDirs.length; j++)
			{
				System.out.println(inputDirs[j]);
			}
			//---------------------------------------------------------------------------------------------------------------

			//Reading each of the directories and storing the full path file names in an arraylist---------------------------
			for (String dir : inputDirs)
			{
				k = 0;
				File inputDir = new File(dir.trim());
				String inputFiles[] = inputDir.list();
				while(k < inputFiles.length)
				{
					allFiles.add(dir.trim()+inputFiles[k]);
					k++;
				}
			}
			totalFiles = allFiles.size();
			System.out.println("Total Files in entire collection for sample selection: " + totalFiles);
			//---------------------------------------------------------------------------------------------------------------

			//Performing random selection and creating .sh file for actual copy----------------------------------------------
			k = 0;
			bw = new BufferedWriter(new FileWriter("RandomDocCopier.sh"));
			Random r = new Random();
			while(k < numRandFiles)
			{
				newNum = r.nextInt(totalFiles);
				if(randNumsCheck.add(newNum))
				{
					bw.write("cp " + allFiles.get(newNum) + " randomSelection/");
					bw.newLine();
					k++;
				}
				else
				{
					continue;
				}
			}
			bw.close();
			System.out.println("Successfully generated file: RandomDocCopier.sh");
			System.out.println("Please run RandomDocCopier.sh manually to get the random files in the directory hi_Random/");
			//---------------------------------------------------------------------------------------------------------------
		}
		catch (Exception ex) 
		{
			System.out.println("Exception Encountered: " + ex.toString());	
		}
	} 
}
