package problem1;

public class problem1 {

    public static void main(String[] args) {

        double radius = 0.0;
        double circleArea = 100.0;
        int feet = 0;
        int inch = 0;
        // calculate radius from area
        radius = Math.sqrt(circleArea / Math.PI);
        // convert radius to fit and inches
        feet = (int) radius;
        inch = (int) ((radius - feet) * 12.0);

        System.out.println("radius = " + radius + " feet ");
        System.out.println("radius = " + feet + " feet " + inch + " inches ");

        // Volume calculations
        double earthDiameter = 7600.0;
        double sunDiameter = 865000.0;

        double earthRadius = earthDiameter / 2.0;
        double sunRadius = sunDiameter / 2.0;

        double earthVolume = (4.0 / 3.0) * Math.PI * Math.pow(earthRadius, 3);
        double sunVolume = (4.0 / 3.0) * Math.PI * Math.pow(sunRadius, 3);

        long roundedEarthVolume = Math.round(earthVolume);
        long roundedSunVolume = Math.round(sunVolume);
        double ratio = (double) roundedSunVolume / roundedEarthVolume;

        System.out.println("Earth Volume: " + roundedEarthVolume + " cubic miles");
        System.out.println("Sun Volume: " + roundedSunVolume + " cubic miles");
        System.out.println("Sun/Earth Volume Ratio: " + ratio);

    }

}
