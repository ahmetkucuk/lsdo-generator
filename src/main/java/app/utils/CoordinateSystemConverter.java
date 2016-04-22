/**
 * File: CoordinateSystemConverter.java is used for conversion between 
 * the various coordinate systems used in solar physics datasets based
 * on the SDO mission with its 4096 X 4096 pixel images
 * and a pixel based coordinate system used in displaying images
 * in computer science.   
 * implemented by the Data Mining Lab at Georgia State University
 * 
 * Some code was taken from SunPy a python library and can be found at
 * http://sunpy.org/
 * 
 * @author Dustin Kempton
 * @version 05/12/2015 
 * @Owner Data Mining Lab, Georgia State University
 *
 */
package app.utils;

import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.math3.complex.Complex;

public class CoordinateSystemConverter {

	static final double CDELT = 0.599733;
	static final double HPCCENTER = 4096.0 / 2.0;

	static final double rsun_meters = 696000;
	static final double dsun_meters = 149600000;

	/**
	 * convertToPixXY :converts a point in HPC coordinates to Pixel XY
	 * coordinates
	 * 
	 * @param pointIn
	 *            :point to convert
	 * @return :a new point in Pixel XY coordinates
	 */
	public static Coordinate convertHPCToPixXY(Coordinate pointIn) {
		double PixX = (CoordinateSystemConverter.HPCCENTER + (pointIn.x / CoordinateSystemConverter.CDELT));
		double PixY = (CoordinateSystemConverter.HPCCENTER - (pointIn.y / CoordinateSystemConverter.CDELT));
		return new Coordinate(PixX, PixY);
	}

	/**
	 * convertToPixXY :converts a point in HGS coordinates to Pixel XY
	 * coordinates
	 * 
	 * @param pointIn
	 *            :point to convert
	 * @return :a new point in Pixel XY coordinates
	 */
	public static Coordinate convertHGSToPixXY(Coordinate pointIn) {
		Coordinate pointHCC = CoordinateSystemConverter.convertHG_HCC(pointIn.x,
				pointIn.y);
		Coordinate pointHPC = CoordinateSystemConverter.convertHCC_HPC(pointHCC.x,
				pointHCC.y);

		double PixX = (CoordinateSystemConverter.HPCCENTER + (pointHPC.x / CoordinateSystemConverter.CDELT));
		double PixY = (CoordinateSystemConverter.HPCCENTER - (pointHPC.y / CoordinateSystemConverter.CDELT));
		return new Coordinate(PixX, PixY);
	}

	/**
	 * convertToHGS :converts a point in Pixel XY coordinates to HGS coordinates
	 * 
	 * @param pointIn
	 *            :the point to convert
	 * @return :a new point in HGS
	 */
	public static Coordinate convertPixXYToHGS(Coordinate pointIn) {
		double HPCx = (pointIn.x - CoordinateSystemConverter.HPCCENTER)
				* CoordinateSystemConverter.CDELT;
		double HPCy = (CoordinateSystemConverter.HPCCENTER - pointIn.y)
				* CoordinateSystemConverter.CDELT;

		Coordinate hccPoint = CoordinateSystemConverter.convertHPC_HCC(HPCx, HPCy);
        Coordinate hgsPoint = CoordinateSystemConverter.convertHCC_HG(hccPoint.x,
				hccPoint.x);

		return hgsPoint;
	}

	/**
	 * convertToHGS :converts a point in Pixel XY coordinates to HPC coordinates
	 * 
	 * @param pointIn
	 *            :the point to convert
	 * @return :a new point in HPC
	 */
	public static Coordinate convertPixXYToHPC(Coordinate pointIn) {
		double HPCx = (pointIn.x - CoordinateSystemConverter.HPCCENTER)
				* CoordinateSystemConverter.CDELT;
		double HPCy = (CoordinateSystemConverter.HPCCENTER - pointIn.y)
				* CoordinateSystemConverter.CDELT;

		Coordinate ptd = new Coordinate(HPCx, HPCy);
		return ptd;
	}

	// ************************************************************************/
	// """Convert from Heliographic coordinates (given in degrees) to
	// Heliocentric - Cartesian coordinates(given in meters)
	//
	// Parameters
	// ----------
	// hglon_deg, hglat_deg : float (degrees)
	// Heliographic longitude and latitude in degrees.
	// b0_deg : float (degrees)
	// Tilt of the solar North rotational axis toward the observer
	// (heliographic latitude of the observer). Usually given as SOLAR_B0,
	// HGLT_OBS, or CRLT_OBS. Default is 0.
	// l0_deg : float (degrees)
	// Carrington longitude of central meridian as seen from Earth. Default is
	// 0.
	// occultation : Bool
	// If true set all points behind the Sun(e.g. not visible) to Nan.
	// z : Bool
	// If true return the z coordinate as well.
	// r : float (meters)
	// Heliographic radius
	//
	// Returns
	// -------
	// out : ndarray(meters)
	// The data coordinates in Heliocentric - Cartesian coordinates.
	//
	// Notes
	// -----
	// Implements Eq.(11) of Thompson(2006), A&A, 449, 791, with the default
	// assumption that the value 'r' in Eq. (11) is identical to the radius of
	// the
	// Sun.
	//
	// Examples
	// --------
	// >> > sunpy.wcs.convert_hg_hcc(0.01873188196651189, 3.6599471896203317,
	// r = 704945784.41465974, z = True)
	// (230000.0, 45000000.0, 703508000.0)
	// """
	// ************************************************************************/
	static Coordinate convertHG_HCC(double hglon_deg, double hglat_deg) {

		double b0_deg = 0;
		double l0_deg = 0;
		double lon = Math.toRadians(hglon_deg);
		double lat = Math.toRadians(hglat_deg);

		double cosb = Math.cos(Math.toRadians(b0_deg));
		double sinb = Math.sin(Math.toRadians(b0_deg));

		lon = lon - Math.toRadians(l0_deg);

		double cosx = Math.cos(lon);
		double sinx = Math.sin(lon);
		double cosy = Math.cos(lat);
		double siny = Math.sin(lat);

		// #Perform the conversion.
		double x = CoordinateSystemConverter.rsun_meters * cosy * sinx;
		double y = CoordinateSystemConverter.rsun_meters
				* (siny * cosb - cosy * cosx * sinb);
		// double zz = r * (siny * sinb + cosy * cosx * cosb);

		return new Coordinate(x, y);
	}

	// ************************************************************************/
	// """Converts from Helioprojective-Cartesian (HPC) coordinates into
	// Heliocentric-Cartesian (HCC) coordinates. Returns all three dimensions,
	// x, y, z in
	// meters.
	//
	// Parameters
	// ----------
	// x, y : float
	// Data coordinate in angle units (default is arcsec)
	// dsun_meters : float
	// Distance from the observer to the Sun in meters. Default is 1 AU.
	// angle_units : str
	// Units of the data coordinates (e.g. arcsec, arcmin, degrees). Default is
	// arcsec.
	// z : Bool
	// If true return the z coordinate as well.
	//
	// Returns
	// -------
	// out : ndarray
	// The data coordinates (x,y,z) in heliocentric cartesian coordinates in
	// meters.
	// ************************************************************************/
	static Coordinate convertHPC_HCC(double x, double y) {

		double arcSec = Math.toRadians(1) / (60 * 60);
		double cosx = Math.cos(x * arcSec);
		double sinx = Math.sin(x * arcSec);
		double cosy = Math.cos(y * arcSec);
		double siny = Math.sin(y * arcSec);

		double q = CoordinateSystemConverter.dsun_meters * cosy * cosx;
		double distance = Math.pow(q, 2.0)
				- Math.pow(CoordinateSystemConverter.dsun_meters, 2.0)
				+ Math.pow(CoordinateSystemConverter.rsun_meters, 2.0);

		if (distance < 0) {
			Complex c = new Complex(distance, 0.0);
			c = c.sqrt();
			distance = q - c.getReal();
		} else {
			distance = q - Math.sqrt(distance);
		}

		double rx = distance * cosy * sinx;
		double ry = distance * siny;

		return new Coordinate(rx, ry);
	}

	// ************************************************************************/
	// """Convert Heliocentric-Cartesian (HCC) to angular
	// Helioprojective-Cartesian (HPC) coordinates (in degrees).
	//
	// Parameters
	// ----------
	// x, y : float (meters)
	// Data coordinate in meters.
	// dsun_meters : float
	// Distance from the observer to the Sun in meters. Default is 1 AU.
	// angle_units : str
	// Units of the data coordinates (e.g. arcsec, arcmin, degrees). Default is
	// arcsec.
	//
	// Returns
	// -------
	// out : ndarray
	// The data coordinates (x,y) in helioprojective cartesian coordinates in
	// arcsec.
	//
	// Notes
	// -----
	//
	// Examples
	// --------
	//
	// """
	// # Calculate the z coordinate by assuming that it is on the surface of the
	// Sun */
	// ************************************************************************/
	static Coordinate convertHCC_HPC(double x, double y) {
		double z = Math.sqrt(Math.pow(CoordinateSystemConverter.rsun_meters, 2)
				- Math.pow(x, 2) - Math.pow(y, 2));

		double zeta = CoordinateSystemConverter.dsun_meters - z;
		double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
				+ Math.pow(zeta, 2));
		double hpcx = Math.toDegrees(Math.atan2(x, zeta));
		double hpcy = Math.toDegrees(Math.asin(y / distance));

		// if angle_units == 'arcsec' :
		hpcx = 60 * 60 * hpcx;
		hpcy = 60 * 60 * hpcy;

		return new Coordinate(hpcx, hpcy);
	}

	// ************************************************************************/
	// """Convert from Heliocentric-Cartesian (HCC) (given in meters) to
	// Heliographic coordinates (HG) given in degrees, with radial output in
	// meters.
	//
	// Parameters
	// ----------
	// x, y : float (meters)
	// Data coordinate in meters.
	// z : float (meters)
	// Data coordinate in meters. If None, then the z-coordinate is assumed
	// to be on the Sun.
	// b0_deg : float (degrees)
	// Tilt of the solar North rotational axis toward the observer
	// (heliographic latitude of the observer). Usually given as SOLAR_B0,
	// HGLT_OBS, or CRLT_OBS. Default is 0.
	// l0_deg : float (degrees)
	// Carrington longitude of central meridian as seen from Earth. Default is
	// 0.
	// radius : Bool
	// If true, forces the output to return a triple of (lon, lat, r). If
	// false, return (lon, lat) only.
	//
	// Returns
	// -------
	// out : ndarray (degrees, meters)
	// if radius is false, return the data coordinates (lon, lat). If
	// radius=True, return the data cordinates (lon, lat, r). The quantities
	// (lon, lat) are the heliographic coordinates in degrees. The quantity
	// 'r' is the heliographic radius in meters.
	// ************************************************************************/
	static Coordinate convertHCC_HG(double x, double y) {

		float b0_deg = 0;
		float l0_deg = 0;

		double d = Math.pow(CoordinateSystemConverter.rsun_meters, 2)
				- Math.pow(x, 2) - Math.pow(y, 2);
		double z;
		if (d < 0) {
			z = 0;
		} else {
			z = Math.sqrt(d);
		}

		double cosb = Math.cos(Math.toRadians(b0_deg));
		double sinb = Math.sin(Math.toRadians(b0_deg));

		double hecr = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)
				+ Math.pow(z, 2));
		double hgln = Math.atan2(x, z * cosb - y * sinb)
				+ Math.toRadians(l0_deg);
		double hglt = Math.asin((y * cosb + z * sinb) / hecr);

		return new Coordinate(Math.toDegrees(hgln), Math.toDegrees(hglt));
	}
}
