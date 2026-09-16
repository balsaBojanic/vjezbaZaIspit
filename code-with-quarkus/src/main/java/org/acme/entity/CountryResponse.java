package org.acme.entity;

public class CountryResponse {
	public Nearest nearest;
	public static class Nearest{
		public String latt;
		public String longt;
	}
}
