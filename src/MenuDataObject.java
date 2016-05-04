import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class MenuDataObject implements Callable<MyTuple<Document, Document>>{
   
	public Integer id;
    //various statistics to be kept track of by this object
	private Double itemAdjStat;
    private Double itemAdvStat;
    private Double itemWordLenTotal;
    private Double itemWordCount;
    private Double descAdjStat;
    private Double descAdvStat;
    private Double descWordLenTotal;
    private Double descWordCount;
    private Double totalItemCount;
   
    private GenericObjectPool<MenuAnalyzer> objectPool;
    private HashMap<String, String> menuContents;
    private MenuVectorMap mvp;
    public StringBuilder allDescString;
    
    //attributes to be used to create this MDO's MenuAttributeVector
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

    
    /**
     * Constructor for a Callable MenuDataObject
     * @param id the id of the associated menu
     * @param rating the yelp rating of the menu
     * @param menuContents a <String, String> HashMap of <item, description>
     * @param chain 1.0 if yes, 0.0 if no
     * @param objectPool the pool of MenuAnalyzerObjects
     * @param mvp the ConcurrentHashMap to be updated by this Callable
     */
    public MenuDataObject(Integer id, Double rating, HashMap<String, String> menuContents, Double
            chain, GenericObjectPool<MenuAnalyzer> objectPool, MenuVectorMap mvp) {
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
        this.objectPool = objectPool;
        this.menuContents = menuContents;
        this.mvp = mvp;
    }

    /**Uses a MenuAnalyzer to analyze each item and description in MenuContents,
     * updates the concurrentHashMap passed to this object with the values. 
     * Returns a Tuple of Documents, one with the words from this menu, one from its bigrams    
    **/
    @Override
    public MyTuple<Document, Document> call() throws Exception {
        MenuAnalyzer ma = objectPool.borrowObject();
        for(String s: menuContents.keySet()){
            MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
            this.addItemMap(tupItem.second);
            MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(menuContents.get(s));
            this.addDescriptionMap(tupDesc.second);
            this.addDescription(tupDesc.first);
        }
        mvp.add(this);
        BigramGenerator bg = new BigramGenerator();
        objectPool.returnObject(ma);
        System.out.println("MA returned, Finished with restaurant" + id);
        return new MyTuple<>(new Document(this.allDescString.toString(), id), new Document(bg.generateBigrams(this.allDescString.toString()), id));
    }
    
    /**
     * Update this MDO's item counts based on the part of speech tag map 
     * @param tagMap
     */
    public void addItemMap(HashMap<String, String> tagMap){
        totalItemCount++;
        for(String word: tagMap.keySet()){
            word = word.replaceAll("[^A-za-z]", "");
            itemWordCount ++;
            itemWordLenTotal += word.length();
            if(tagMap.containsKey(word) && tagMap.get(word).length()>1){
                itemAdjStat += tagMap.get(word).substring(0,2).equals("JJ") ? 1 : 0;
                itemAdvStat += tagMap.get(word).substring(0,2).equals("RB") ? 1 : 0;
            }
        }
    }

    /**
     * Update this MDO's description counts based on the part of speech tag map 
     * @param tagMap
     */
    public void addDescriptionMap(HashMap<String, String> tagMap){
        for(String word: tagMap.keySet()){
            descWordCount ++;
            descWordLenTotal += word.length();
            if(tagMap.containsKey(word) && tagMap.get(word).length()>1){
                descAdjStat += tagMap.get(word).substring(0,2).equals("JJ") ? 1 : 0;
                descAdvStat += tagMap.get(word).substring(0,2).equals("RB") ? 1 : 0;
            }
        }
    }
    /**
     * Add desc this menu's full menu string
     * @param desc the description to add to the full menu string
     */
    public void addDescription(String desc){
        allDescString.append(desc).append("\n");
    }

    /**
     * Called once the whole menu has been read. Calculates the totals for each statistic
     */
    public void calculateTotals(){
    	if(totalItemCount <= 0){
    		throw new IllegalStateException("Empty Menu!, ID: "+ id);
    	}
        wl_item = itemWordLenTotal/itemWordCount;
        adj_item = itemAdjStat/ itemWordCount;
        adv_item = itemAdvStat/ itemWordCount;
        nw_item = itemWordCount/ totalItemCount;

        wl_desc = descWordLenTotal/descWordCount;
        adj_desc = descAdjStat/ descWordCount;
        adv_desc = descAdvStat/ descWordCount;
        nw_desc = descWordCount/ totalItemCount;

    }
}
