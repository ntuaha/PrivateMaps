package com.aha.privatemaps;

import java.util.Locale;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MapFragment_Test extends Fragment {
	public static final String ARG_PLANET_NUMBER = "planet_number";

	public MapFragment_Test() {
		// Empty constructor required for fragment subclasses
		// 留空給繼承用的class
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
		int i = getArguments().getInt(ARG_PLANET_NUMBER);
		String planet = getResources().getStringArray(R.array.Drawer_List)[i];
		int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),"drawable", getActivity().getPackageName());
		((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
		getActivity().setTitle(planet);
		return rootView;
	}
}
