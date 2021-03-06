package de.open4me.depot.sql;

import java.util.ArrayList;
import java.util.List;

public class SQLChange {

	private int version;

	private String[] query;


	public int getVersion() {
		return version;
	}

	public String[] getQuery() {
		return query;
	}

	public SQLChange(int newVersion, String...strings ) {
		this.version = newVersion;
		this.query = strings;
	}

	public static List<SQLChange> getChangesSinceVersion(int currentversion) {
		ArrayList<SQLChange> liste = new ArrayList<SQLChange>();
		if (currentversion < 3) {

			liste.add(new SQLChange(3, 		
					"drop table IF EXISTS depotviewer_bestand;",
					"drop table IF EXISTS depotviewer_umsaetze;",
					"drop table IF EXISTS depotviewer_cfg;",

					"CREATE TABLE depotviewer_umsaetze (\n" + 
							"  id int NOT NULL auto_increment,\n" + 
							" wpid int,\n" +
							"  kontoid 	int(10),\n" + 
							"  anzahl decimal(20,10),\n" + 
							"  \n" + 
							"  kurs decimal(20,2),\n" + 
							"  kursw varchar(3) NOT NULL,\n" + 
							"  \n" + 
							"  kosten decimal(20,2),\n" + 
							"  kostenw varchar(3) NOT NULL,\n" + 
							"  aktion varchar(10) NOT NULL,\n" + 
							"  buchungsdatum date,\n" + 
							"  buchungsinformationen text,\n" + 
							"  orderid varchar(50),\n" + 
							"  PRIMARY KEY (id)\n" + 
							");",
							"create table depotviewer_bestand (\n" + 
									"  id int NOT NULL auto_increment,\n" + 
									"  wpid int,\n" +
									"  kontoid 	int(10),\n" + 
									"  anzahl decimal(20,10),\n" + 
									"  kurs decimal(20,2),\n" + 
									"  kursw varchar(3) NOT NULL,\n" + 
									"  \n" + 
									"  wert decimal(20,2),\n" + 
									"  wertw varchar(3) NOT NULL,\n" + 
									"  \n" + 
									"  datum date NOT NULL,\n" + 
									"  \n" + 
									"  PRIMARY KEY (id)\n" + 
									");",


									"create table depotviewer_cfg (\n" + 
											"  id int NOT NULL auto_increment,\n" + 
											"  `key` varchar(200),\n" + 
											"  value text,\n" + 
											"  \n" + 
											"  PRIMARY KEY (id)\n" + 
											");",

											"insert into depotviewer_cfg (`key`,value) values ('dbversion','3');",
											
											"drop table IF EXISTS depotviewer_wertpapier;",

											"CREATE TABLE depotviewer_wertpapier (\n" + 
													"  id int NOT NULL auto_increment,\n" + 
													"  wertpapiername varchar(255) NOT NULL,\n" + 
													"  wkn varchar(6) NOT NULL,\n" + 
													"  isin varchar(12) NOT NULL,\n" + 
													"  PRIMARY KEY (id)\n" + 
													");"

					));
		}
		if (currentversion < 4) {
			liste.add(new SQLChange(4, 		
					"drop table IF EXISTS depotviewer_kurse;",
					"CREATE TABLE depotviewer_kurse (\n" + 
							"  id int NOT NULL auto_increment,\n" + 
							"  wpid int,\n" +
							"  kurs decimal(20,2),\n" + 
							"  kursw varchar(3) NOT NULL,\n" + 
							"  kursdatum date,\n" + 
							"  PRIMARY KEY (id)\n" + 
							");"
					));

		}
		if (currentversion < 5) {
			liste.add(new SQLChange(5, 	
					"ALTER TABLE depotviewer_umsaetze ADD CONSTRAINT fkdvumsaetze FOREIGN KEY (kontoid) REFERENCES konto (id) on delete cascade;",
					"ALTER TABLE depotviewer_bestand ADD CONSTRAINT fkdvbestand FOREIGN KEY (kontoid) REFERENCES konto (id) on delete cascade;"
					));
		}
		if (currentversion < 6) {
			liste.add(new SQLChange(6, 	
					"ALTER TABLE depotviewer_bestand ADD bewertungszeitpunkt date;"
					));
		}
		if (currentversion < 7) {
			liste.add(new SQLChange(7, 		
					"drop table IF EXISTS depotviewer_kursevent;",
					"CREATE TABLE depotviewer_kursevent (\n" + 
							"  id int NOT NULL auto_increment,\n" + 
							"  wpid int,\n" +
							"  ratio varchar(30) ,\n" +
							"  value decimal(10,5),\n" +
							"  aktion varchar(100) NOT NULL,\n" +
							"  datum date,\n" + 
							"  waehrung varchar(3) ,\n" +
							"  PRIMARY KEY (id)\n" + 
							");"
					));

		}
		if (currentversion < 8) {
			liste.add(new SQLChange(8, 	
					"ALTER TABLE depotviewer_kurse ADD kursperf decimal(20,2);"
					));
		}
		if (currentversion < 9) {
			liste.add(new SQLChange(9, 	
					"ALTER TABLE depotviewer_umsaetze ADD kommentar varchar(2000);"
					));
		}
		if (currentversion < 10) {
			liste.add(new SQLChange(10, 	
					"ALTER TABLE depotviewer_cfg MODIFY  COLUMN  `value` varchar(2000);",
					"ALTER TABLE depotviewer_umsaetze MODIFY COLUMN  buchungsinformationen varchar(2000);"
					));
		}
		if (currentversion < 11) {
			liste.add(new SQLChange(11, 	
					"insert into depotviewer_cfg (`key`,value) values ('status_bestand_order', null);"
					));
		}
		if (currentversion < 12) {
			liste.add(new SQLChange(12, 	
					"create index idxKurseWpid on depotviewer_kurse(wpid);",
					"create index idxKurseDatum on depotviewer_kurse(kursdatum);"
					));
		}
		if (currentversion < 13) {
			liste.add(new SQLChange(13,
					"CREATE TABLE depotviewer_cfgupdatestock (\n" + 
							"  id int NOT NULL auto_increment,\n" + 
							"  wpid int,\n" +
							"  `key` varchar(200),\n" + 
							"  value varchar(200),\n" + 
							"  PRIMARY KEY (id)\n" + 
							");"
					));
		}
		if (currentversion < 14) {
			liste.add(new SQLChange(14, 
					"ALTER TABLE depotviewer_umsaetze ADD transaktionskosten decimal(20,2);",
					"ALTER TABLE depotviewer_umsaetze ADD transaktionskostenw varchar(3);",
					"ALTER TABLE depotviewer_umsaetze ADD steuern decimal(20,2);",
					"ALTER TABLE depotviewer_umsaetze ADD steuernw varchar(3);"
					));
		}
		if (currentversion < 15) {
			liste.add(new SQLChange(15, 
					"update depotviewer_umsaetze set transaktionskosten  =0 where transaktionskosten is null;",
					"update depotviewer_umsaetze set transaktionskostenw ='EUR' where transaktionskostenw is null;",
					"update depotviewer_umsaetze set steuern =0 where steuern is null;",
					"update depotviewer_umsaetze set steuernw='EUR' where steuernw is null;"
					));
		}
		if (currentversion < 16) {
			liste.add(new SQLChange(16, 
					"ALTER TABLE depotviewer_bestand MODIFY  COLUMN  `id` int NOT NULL auto_increment;",
					"ALTER TABLE depotviewer_kurse MODIFY  COLUMN  `id` int NOT NULL auto_increment;",
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `id` int NOT NULL auto_increment;",
					"ALTER TABLE depotviewer_wertpapier MODIFY  COLUMN  `id` int NOT NULL auto_increment;",
					"ALTER TABLE depotviewer_kursevent MODIFY  COLUMN  `id` int NOT NULL auto_increment;",
					
					"ALTER TABLE depotviewer_bestand MODIFY  COLUMN  `wert` decimal(20,6);",
					"ALTER TABLE depotviewer_bestand MODIFY  COLUMN  `kurs` decimal(20,6);",
					
					"ALTER TABLE depotviewer_kurse MODIFY  COLUMN  `kurs` decimal(20,6);",
					"ALTER TABLE depotviewer_kurse MODIFY  COLUMN  `kursperf` decimal(20,6);",
					
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `kurs` decimal(20,6);",
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `kosten` decimal(20,6);",
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `transaktionskosten` decimal(20,6);",
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `steuern` decimal(20,6);"
										
					));
		}
		if (currentversion < 17) {
			liste.add(new SQLChange(17, 
					"ALTER TABLE depotviewer_umsaetze MODIFY  COLUMN  `aktion` varchar(30) NOT NULL;"));
		}
		if (currentversion < 18) {
			liste.add(new SQLChange(18, 	
					"create index idxKurseId on depotviewer_kurse(id);"
					));
		}

//		liste.add(new SQLChange(currentversion,
//		"truncate table depotviewer_umsaetze;",
//		"truncate table depotviewer_bestand;",
//		"truncate table depotviewer_wertpapier;",
//		"truncate table depotviewer_kurse;"
//		));	
		return liste;
	}





}
