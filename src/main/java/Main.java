import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		try {
//	    	singletest("coor-xy.txt", 81161);
//	    	singletest("coor-xy48.txt", 1292668);
//	    	singletest("coor-xy175.txt", 4632035);
	    	singletest("coor-xy502.txt", 13146968);
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static void singletest(String file, int expect) throws IOException{
        URL classpathResource = Thread.currentThread().getContextClassLoader().getResource("");
        String resourcePath = classpathResource.getPath() + file;
        FileInputStream inputStream = null;
        Scanner sc = null;
        int count = 0;
        long time = 0;
        QuadTree qt = null;
        try {
        	inputStream = new FileInputStream(resourcePath);
        	sc = new Scanner(inputStream, "UTF-8");
        	long begin = System.currentTimeMillis();
            qt = new QuadTree(10000.000000, 5000.000000, 30000.000000, 15000.000000);
            time += System.currentTimeMillis() - begin;
        	while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] items = line.split(",");
                if (items.length == 3) {
                	double x = Double.parseDouble(items[2]);
                	double y = Double.parseDouble(items[1]);
                	double o = Double.parseDouble(items[0]);
                	begin = System.currentTimeMillis();
                	qt.set(x, y, o);
                	time += System.currentTimeMillis() - begin; 
                	count++;
                	if (count % 1000000 == 0)
                		System.out.println(count);
                }
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        	if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        
        
        assertEquals("Expecting " + expect + " points", expect, count);
        System.out.println("\n***Test with " + file + "***");
        System.out.println("-----Test Build-----");
        System.out.println("Total Building time: " + time + "ms");
        System.out.println("Average Building time: " + time / count + "ms");

        count = 0;
        time = 0;
        try {
        	inputStream = new FileInputStream(resourcePath);
        	sc = new Scanner(inputStream, "UTF-8");
        	while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] items = line.split(",");
                if (items.length == 3) {
                	double x = Double.parseDouble(items[2]);
                	double y = Double.parseDouble(items[1]);
                	long begin = System.nanoTime();
                	qt.get(x, y, null);
                	time += System.nanoTime() - begin;
                	count++;
                	if (count % 1000000 == 0)
                		System.out.println(count);
                }
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        	if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
        assertEquals(expect, count);
        System.out.println("-----Test Query-----");
        System.out.println("Total query time: " + time + "ns");
        System.out.println("Average query time: " + time / count + "ns");
    }
}
