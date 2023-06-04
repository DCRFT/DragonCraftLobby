package pl.dcrft.Utils;

import java.util.Random;

public class RandomUtil {
    public static double randomDouble(double min, double max) {
    Random r = new Random();
    return (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
}
}
