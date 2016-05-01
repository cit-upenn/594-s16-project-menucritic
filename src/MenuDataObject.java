import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MenuDataObject implements Callable<MyTuple<Document, Document>>{
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
    private GenericObjectPool<MenuAnalyzer> objectPool;
    private HashMap<String, String> menuContents;
    private MenuVectorMap mvp;
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

    /**
     * Initializes the data associated with a menu
     * @param id identifier for menu
     * @param rating yelp rating score
     * @param chain boolean if it is a chain restaurant
     */
    public MenuDataObject(String id, Double rating, HashMap<String, String> menuContents, Double
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

    public MenuDataObject(String id, Double rating, Double chain) {
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
        allDescString = new StringBuilder();
    }

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
        return new MyTuple<>(new Document(this.allDescString.toString(), id), new Document(bg
                .generateBigrams(this.allDescString.toString()), id));
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
