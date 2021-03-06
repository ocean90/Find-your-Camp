package de.fhkoeln.gm.findyourcamp.app.matching;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import de.fhkoeln.gm.findyourcamp.app.gson.JsonDatatypes;
import de.fhkoeln.gm.findyourcamp.app.model.RentalProperty;
import de.fhkoeln.gm.findyourcamp.app.utils.Logger;

public class RentalPropertyMatch {

	public final static int MAX_MATCH_RATE = 100;
	public final static int MIN_MATCH_RATE = 50;

	public static int getMatchResult( JsonDatatypes data, Context appContext ) {
		int rentalPropertyLocalId = data.getRentalPropertyLocalId();

		RentalProperty rentalProperty = RentalProperty.getFromId( rentalPropertyLocalId, appContext );
		if ( rentalProperty == null ) {
			return 0;
		}

		int currentMatch = MAX_MATCH_RATE;

		// Get desired values
		int minPriceDesired = data.getMinPrice();
		int maxPriceDesired = data.getMaxPrice();
		int groupSizeDesired = data.getGroupSize();
		Map<String, Boolean> featuresDesired = data.getFeatures();
		int featuresDesiredCount = 0;
		for ( Map.Entry<String, Boolean> featureDesired : featuresDesired.entrySet() ) {
			if ( featureDesired.getValue() ) {
				featuresDesiredCount++;
			}
		}

		// Get present values
		int minPricePresent = rentalProperty.getPriceRange()[0];
		int maxPricePresent = rentalProperty.getPriceRange()[1];
		int groupSizePresent = rentalProperty.getGroupSize();
		Map<String, Boolean> featuresPresent = rentalProperty.getFeatures().toMap();

		// Check price values
		if ( maxPriceDesired < minPricePresent ) {
			Logger.info( "maxPriceDesired < minPricePresent: " + maxPriceDesired + "<" + minPricePresent );
			currentMatch = currentMatch - 10;
		}

		// Check group size values
		if ( groupSizeDesired > groupSizePresent ) {
			Logger.info( "groupSizeDesired > groupSizePresent: " + groupSizeDesired + ">" + groupSizePresent );
			currentMatch = currentMatch - 20;
		}

		// Calculate the scalar product
		ArrayList<Vector<Integer>> featureVectors = createFeatureVectors( featuresDesired, featuresPresent );
		int featureVectorsScalar = createFeatureVectorsScalar( featureVectors.get( 0 ), featureVectors.get( 1 ) );

		// Check feature matching
		if ( featureVectorsScalar < ( featuresDesiredCount / 2 ) ) {
			Logger.info( "featureVectorsScalar < ( featuresDesiredCount / 2 ): " + featureVectorsScalar + "<" +  ( featuresDesiredCount / 2 ) );
			// Only half of the desired features exist
			currentMatch = currentMatch - 30;
		} else if ( ( featureVectorsScalar < featuresDesiredCount ) && ( featureVectorsScalar >= ( featuresDesiredCount / 2 ) ) ) {
			Logger.info( "featureVectorsScalar >= ( featuresDesiredCount / 2 ): " + featureVectorsScalar + ">=" +  ( featuresDesiredCount / 2 ) );
			// Not all desired features exist
			currentMatch = currentMatch - 10;
		}


		return currentMatch;

	}

	private static int createFeatureVectorsScalar( Vector<Integer> vectorFeaturesPresent,
		Vector<Integer> vectorFeaturesDesired ) {
		if ( vectorFeaturesPresent.size() != vectorFeaturesDesired.size() ) {
			return 0;
		}

		int sum = 0;
		for ( int i = 0; i < vectorFeaturesPresent.size(); i++ ) {
			sum = sum + ( vectorFeaturesPresent.get( i ) * vectorFeaturesDesired.get( i ) );
		}

		return sum;
	}

	public static ArrayList<Vector<Integer>> createFeatureVectors( Map<String, Boolean> featuresDesired,
		Map<String, Boolean> featuresPresent ) {
		ArrayList<Vector<Integer>> vectors = new ArrayList<Vector<Integer>>();
		Vector<Integer> vectorFeaturesPresent = new Vector<Integer>();
		Vector<Integer> vectorFeaturesDesired = new Vector<Integer>();

		for ( Map.Entry<String, Boolean> featurePresent : featuresPresent.entrySet() ) {
			Boolean desiredValue = featuresDesired.get( featurePresent.getKey() );
			if ( desiredValue == null ) {
				desiredValue = false;
			}
			vectorFeaturesPresent.add( featurePresent.getValue() ? 1 : 0 );
			vectorFeaturesDesired.add( desiredValue ? 1 : 0 );
		}

		vectors.add( vectorFeaturesPresent );
		vectors.add( vectorFeaturesDesired );

		return vectors;
	}
}
