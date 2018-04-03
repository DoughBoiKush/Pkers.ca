package org.osps.model.players;
/**
 * Revamped a simple means of receiving a random item based on chance.
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class Packages{

	// Packages
	//Hween Package//
		static int[] hweenpackage1 = { 1053, 1055, 1057 }; 

		public static int hweenpackage1() {
				return hweenpackage1[(int) (Math.random() * hweenpackage1.length)];
		}
		static int[] hweenpackage2 = { 1053, 1055, 1057,  };

		public static int hweenpackage2() {
				return hweenpackage2[(int) (Math.random() * hweenpackage2.length)];
		}
	//end of Hween package//
		
	//Partyhat package//	
		static int[] partyhatpackage1 = { 1038, 1040, 1042, 1044, 1046, 1048 }; 

		public static int partyhatpackage1() {
				return partyhatpackage1[(int) (Math.random() * partyhatpackage1.length)];
		}
		static int[] partyhatpackage2 = { 1038, 1040, 1042, 1044, 1046, 1048  };

		public static int partyhatpackage2() {
				return partyhatpackage2[(int) (Math.random() * partyhatpackage2.length)];
		}
	//end of Partyhat package//
		
	//Pking package//	
		static int[] pkingpackage1 = { 11802, 11785, 12924, 12929, 11791, 13652, 12954, 10548, 10551, 11770, 11771, 11772, 11773, 12831, 12829, 11283, 6889, 6914, 13858, 13861, 13864, 13896,
			13890, 13884, 6570, 13876, 13870, 13873, 13867, 13899, 13905, 11235, 13237, 13239, 13235, 12791, 10887, 11924, 11926 }; 

		public static int pkingpackage1() {
				return pkingpackage1[(int) (Math.random() * pkingpackage1.length)];
		}
	//end of Pking package//
		
	//Rare package//	
		static int[] rarepackage1 = { 1038, 1040, 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057, 15346, 15347, 15310, 15311, 15312, 15313, 15314, 15353, 15353 }; 

		public static int rarepackage1() {
				return rarepackage1[(int) (Math.random() * rarepackage1.length)];
				}
	//end of Rare package//
		
	//Huge package//	
		static int[] hugepackage1 = { 1053, 1055, 1057, 1038, 1040, 1042, 1044, 1046, 1048,11802, 11785, 12924, 12929, 11791, 13652, 12954, 10548, 10551, 11770, 11771, 11772, 11773, 12831, 12829, 11283, 6889, 6914, 13858, 13861, 13864, 13896,
			13890, 13884, 6570, 13876, 13870, 13873, 13867, 13899, 13905, 11235, 13237, 13239, 13235, 12791, 10887, 11924, 11926, 1038, 1040, 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057, 15346, 15347, 15310, 15311, 15312, 15313, 15314, 13887, 13893,
			15348, 15349, 15352, 15353, 2996 }; 

		public static int hugepackage1() {
				return hugepackage1[(int) (Math.random() * hugepackage1.length)];
						}
	//end of Huge package//
}