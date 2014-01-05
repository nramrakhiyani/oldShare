import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

//Code Contributed by: N Ramrakhiyani
//Development Date: 10-November-2013
//Purpose: The code helps in generating Velthius coded text from plain Unicode Hindi text.
public class Devgen
{
	public static void main(String args[])
	{
		//Fixed input file
		String inputFile = "01-input.txt";
		String line, parts[];
		int i;
		try
		{
			//Reading the map that connects each Hindi Unicode character to the corresponding Velthius Code
			//The mapping is read into hmDevMap hashmap
			BufferedReader brM = new BufferedReader(new FileReader("map.txt"));
			HashMap<String, String> hmDevMap = new HashMap<String, String>();
			while((line = brM.readLine()) != null)
			{
				parts = line.split(",");
				hmDevMap.put(parts[0],parts[1]);
			}
			brM.close();

			//Reading the input file and creating a new output file
			BufferedReader brI = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bwO = new BufferedWriter(new FileWriter("02-velthius-coded.dn"));

			//Writing some necessary lines into the output file as a initial step
			bwO.write("\\def\\DevnagVersion{2.15}"); bwO.newLine();
			bwO.write("\\documentclass[12pt]{article}"); bwO.newLine();
			bwO.write("\\usepackage{devanagari}"); bwO.newLine();
			bwO.write("\\begin{document}"); bwO.newLine();
			bwO.write("{\\dn"); bwO.newLine();

			//Generating the Velthius code using the mappings in hmDevMap. This is a straight placement of the
			//Velthius code letters corresponding to the Unicode characters encountered.
			String devnagLine = "";
			while((line = brI.readLine()) != null)
			{
				devnagLine = "";
				for(i = 0; i < line.length(); i++)
				{
					if(line.substring(i,i+1).equals(" "))
					{
						devnagLine = devnagLine + " ";
					}
					else
					{
						if(hmDevMap.get(line.substring(i,i+1)) != null)
							devnagLine = devnagLine + hmDevMap.get(line.substring(i,i+1));
						else
							devnagLine = devnagLine + "*";
					}
				}

				//Following lines replace some character combinations which are undesired and enter due to 
				//the above straight placement of the Velthius Code values for each Unicode character
				devnagLine = devnagLine.replaceAll("aA", "A");
				devnagLine = devnagLine.replaceAll("ao", "o");
				devnagLine = devnagLine.replaceAll("aO", "O");
				devnagLine = devnagLine.replaceAll("ae", "e");
				devnagLine = devnagLine.replaceAll("aE", "E");
				devnagLine = devnagLine.replaceAll("ai", "i");
				devnagLine = devnagLine.replaceAll("aI", "I");
				devnagLine = devnagLine.replaceAll("au", "u");
				devnagLine = devnagLine.replaceAll("aU", "U");
				devnagLine = devnagLine.replaceAll(".daZ", "Ra");
				devnagLine = devnagLine.replaceAll(".DaZ", "Rha");
				devnagLine = devnagLine.replaceAll("PaZ", "fa");
				devnagLine = devnagLine.replaceAll("jaZ", "za");
				devnagLine = devnagLine.replaceAll("KaZ", ".Ka");
				devnagLine = devnagLine.replaceAll("gaZ", ".ga");
				devnagLine = devnagLine.replaceAll("kaZ", ".ka");
				devnagLine = devnagLine.replaceAll("U/", ".o");
				devnagLine = devnagLine.replaceAll("a~~", "");

				bwO.write(devnagLine);
				bwO.newLine();
			}
			brI.close();

			//Completing the output file with proper end markers
			bwO.write("}"); bwO.newLine();
			bwO.write("\\end{document}"); bwO.newLine();
			bwO.close();
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
		
	}
}
