package com.aymegike.humineclaim.utils.balises;

import com.aypi.utils.xml.MCBalise;
import com.aypi.utils.xml.balises.LocationBalise;

public class SignBalise extends LocationBalise {

	public static final String NAME = "sign";
		
	public SignBalise() {
		super(NAME);
	}		

	@Override
	public MCBalise getInstance() {
		return new SignBalise();
	}
}
