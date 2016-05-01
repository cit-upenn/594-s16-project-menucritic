import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MenuDataObject {
		public String id; 
		private Double itemAdjStat;
		private Double itemAdvStat;
		private Double itemWordLenTotal;
		private Double itemWordCount;
		private Double descAdjStat;
		private Double descAdvStat;
		private Double descWordLenTotal;
		private Double descWordCount;
		private Double totalItemCount;
		public StringBuilder allDescString;
		
		
		public Double wl_item;
		public Double adj_item;
		public Double adv_item;
		public Double nw_item;
		public Double wl_desc;
		public Double adj_desc;
		public Double adv_desc;
		public Double nw_desc;
		public Double rating;
		public Double chain;
		
		
		public MenuDataObject(String id, Double rating, Double chain){
			 this.id = id; 
			 itemAdjStat = 0.;
			 itemAdvStat = 0.;
			 itemWordLenTotal = 0.;
			 itemWordCount = 0.;
			 descAdjStat = 0.;
			 descAdvStat = 0.;
			 descWordLenTotal = 0.;
			 descWordCount = 0.;
			 totalItemCount = 0.;
			 this.rating = rating;
			 this.chain = chain;
			 allDescString = new StringBuilder();
		}
		
		public void addItemMap(HashMap<String, String> map){
			totalItemCount++;
			for(String word: map.keySet()){
				word = word.replaceAll("[^A-za-z]", "");
				itemWordCount ++;
				itemWordLenTotal += word.length();
				if(map.containsKey(word) && map.get(word).length()>1){
					itemAdjStat += map.get(word).substring(0,2).equals("JJ") ? 1 : 0; 
					itemAdvStat += map.get(word).substring(0,2).equals("RB") ? 1 : 0; 
				}
			}
			
		}
		
		public void addDescriptionMap(HashMap<String, String> map){
			for(String word: map.keySet()){
				descWordCount ++;
				descWordLenTotal += word.length();
				if(map.containsKey(word) && map.get(word).length()>1){
					descAdjStat += map.get(word).substring(0,2).equals("JJ") ? 1 : 0; 
					descAdvStat += map.get(word).substring(0,2).equals("RB") ? 1 : 0; 
				}
				
				
			}
			
		}
		public void addDescription(String desc){
			allDescString.append(desc + "\n");
		}
		
		public void calculateTotals(){
			wl_item = itemWordLenTotal/itemWordCount;
//			System.out.println("wl_item: "+ wl_item);
			adj_item = itemAdjStat/ itemWordCount;
//			System.out.println("adj_item: "+adj_item);
			adv_item = itemAdvStat/ itemWordCount;
//			System.out.println("adv_item: "+adv_item);
			nw_item = itemWordCount/ totalItemCount;
//			System.out.println("nw_item: "+nw_item);
			
			wl_desc = descWordLenTotal/descWordCount;
//			System.out.println("wl_desc: "+ wl_desc);
			adj_desc = descAdjStat/ descWordCount;
//			System.out.println("adj_desc: "+ adj_desc);
			adv_desc = descAdvStat/ descWordCount;
//			System.out.println("adv_desc: "+ adv_desc);
			nw_desc = descWordCount/ totalItemCount;
//			System.out.println("nw_desc: "+ nw_desc);
			
		}

}
