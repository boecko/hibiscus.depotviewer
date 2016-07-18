package de.open4me.depot.abruf.impl;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.open4me.depot.abruf.utils.Utils;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

public class CortalConsorsMitHBCIJSONWrapper {
	

	private Map<String, Object> orderinfo;
	private Map<String, Object> detailInfo;

	public CortalConsorsMitHBCIJSONWrapper(Map<String, Object> orderinfo, Map<String, Object> detailInfo) {
		this.orderinfo = orderinfo;
		this.detailInfo = detailInfo;
	}

	public boolean check() {
		System.out.println(orderinfo.entrySet());
			return  orderinfo.get("30").equals("ACTIVE")
					&& orderinfo.get("39").equals("piece")
					&& orderinfo.get("0").equals("")
					&& (orderinfo.get("2").equals("") || orderinfo.get("2").equals("LIMIT_BASED"))
					&& orderinfo.get("12").equals("E")
					&& orderinfo.get("17").equals("N")
					&& orderinfo.get("7").equals(orderinfo.get("10"))
					&& orderinfo.get("7").equals(orderinfo.get("28"))
					&& orderinfo.get("7").equals(orderinfo.get("29"))
					&& !orderinfo.get("3").equals("")
					&& (orderinfo.get("6").equals("B") || orderinfo.get("6").equals("S"))
					;
	}

	public boolean addUmsatz(String kontoID)  {
		if (!check()) {
			return false;
		}

		try {
			Utils.addUmsatz(
					kontoID,  // kontoid 
					Utils.getORcreateWKN(getWkn(), getIsis(), getWertpapierName()), // wpid 
					getOrderArt(),  // aktion
					orderinfo.entrySet().toString() + " -- " + detailInfo.entrySet().toString(), // info
					getStueckzahl(), // anzahl
					getKurs(), // kurs 
					getKursW(), // kursW
					getKosten(), // kosten
					getKostenW(), // kostenW
					getDatum(), // Datum
					getOrderID(), // orderid
					"", // Kommentar
					0.0d, // Gebuehren
					"EUR", /// 
					0.0d, // Steuern
					"EUR" /// 
			);
		} catch (RemoteException | ApplicationException | ParseException e) {
			Logger.error("Fehler beim Anlegen eines Umsatzes", e);
			return false;
		}
		return true;
		
	}

	private String getIsis() {
		return orderinfo.get("8").toString();
	}

	private String getOrderID() {
		return orderinfo.get("4").toString();
	}

	private Date getDatum() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse((String) orderinfo.get("3"));
	}

	private String getKostenW() {
		return (String) detailInfo.get("2");
	}

	private Double getKosten() {
		return (getOrderArt().equals("B") ? -1 : 1) *
		Math.rint(getKurs() * getStueckzahl() * 100) / 100;
	}

	private String getKursW() {
		return (String) detailInfo.get("2");
	}

	private Double getKurs() {
		return Double.parseDouble(detailInfo.get("1").toString());
	}

	private Double getStueckzahl() {
		return Double.parseDouble(detailInfo.get("12").toString());
	}

	private String getOrderArt() {
		return ((String) orderinfo.get("6")).toUpperCase();
	}

	private String getWertpapierName() {
		return (String) orderinfo.get("9");
	}

	private String getWkn() {
		return orderinfo.get("11").toString();
	}

	public String getAnnoymisierterBuchungstext() {
		annoIfExists(orderinfo, "3");
		annoIfExists(orderinfo, "4");
		annoIfExists(orderinfo, "5");
		annoIfExists(orderinfo, "8");
		annoIfExists(orderinfo, "9");
		annoIfExists(orderinfo, "11");
		annoIfExists(orderinfo, "15");
		annoIfExists(orderinfo, "27");
		
		annoIfExists(detailInfo, "1");
		annoIfExists(detailInfo, "3");
		annoIfExists(detailInfo, "4");
		annoIfExists(detailInfo, "11");
		annoIfExists(detailInfo, "14");
		return orderinfo.entrySet() + System.lineSeparator() + detailInfo.entrySet();
	}
	
	/**
	 * Anonymisiert einzelne Elemente der Map
	 * @param m Map
	 * @param key Key für das Element, welches anonymisiert werden soll.
	 */
	public void annoIfExists(Map<String, Object> m, String key) {
		if (m.containsKey(key)) {
			String value = m.get(key).toString();
			String out = "";
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (Character.isUpperCase(c)) {
					out = out + "^";
				} else if (Character.isLowerCase(c)) {
					out = out + "_";
				} else if (Character.isDigit(c)) {
					out = out + "#";
				} else {
					out = out + c;
				}
			}
			m.put(key, out);
		}
		
	}

}
