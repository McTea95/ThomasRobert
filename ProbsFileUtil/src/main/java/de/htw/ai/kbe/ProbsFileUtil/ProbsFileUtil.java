package de.htw.ai.kbe.ProbsFileUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class ProbsFileUtil 
{
	public static Properties loadProperties(String filepath){
		Properties props = new Properties();
		
		BufferedInputStream stream = null;

        try {
            stream = new BufferedInputStream(new FileInputStream(filepath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }


        try 
        {
            props.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
		return props;
	}
}
