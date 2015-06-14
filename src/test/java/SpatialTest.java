import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpatialTest {

    static List<Point> _pointList = null;

    private void LoadPointsFromFile(String source) {
        String[] item;
        String[] lines = readAllTextFileLines(source);
        for (String line : lines) {
            item = line.split(",");
            _pointList.add(new Point(Double.parseDouble(item[2]), Double.parseDouble(item[1]), Double.parseDouble(item[0])));
        }
    }

    private static String[] readAllTextFileLines(String fileName) {
        StringBuilder sb = new StringBuilder();

        try {
            String textLine;

			BufferedReader br = new BufferedReader(new FileReader(fileName));

            while ((textLine = br.readLine()) != null) {
                sb.append(textLine);
                sb.append('\n');
            }
            br.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sb.length() == 0)
                sb.append("\n");
        }
        return sb.toString().split("\n");
    }

    public void singletest(String file, int expect){
        _pointList = new ArrayList<Point>();
        URL classpathResource = Thread.currentThread().getContextClassLoader().getResource("");
        String resourcePath = classpathResource.getPath()+"coor-xy.txt";
        long beginload = System.currentTimeMillis();
        LoadPointsFromFile(resourcePath);
        long endload = System.currentTimeMillis();
        assertEquals("Expecting " + expect + " points", expect, _pointList.size());

        long beginbuild = System.currentTimeMillis();
        QuadTree qt = new QuadTree(10000.000000, 5000.000000, 30000.000000, 15000.000000);
        for(Point pt:_pointList) {
            qt.set(pt.getX(), pt.getY(), pt.getValue());
        }
        long endbuild = System.currentTimeMillis();
        System.out.println("***Test with " + file + "***");
        System.out.println("-----Test Build-----");
        System.out.println("Loading time: " + (endload - beginload) + "ms");
        System.out.println("Building time: " + (endbuild - beginbuild) + "ms");
        System.out.println("Total time: " + (endbuild - beginload) + "ms");

        int count = 0;
        long totalquery = 0;
        for(Point pt : _pointList) {
        	long beginquery = System.nanoTime();
        	qt.get(pt.getX(), pt.getY(), null);
        	long endquery = System.nanoTime();
        	totalquery += endquery - beginquery;
        	count++;
        }
        assertEquals(expect, count);
        System.out.println("-----Test Query-----");
        System.out.println("Total query time: " + totalquery + "ns");
        System.out.println("Average query time: " + totalquery / count + "ns");
    }
    
    @Test
    public void testTree(){
    	singletest("coor-xy.txt", 81161);
    }
}
