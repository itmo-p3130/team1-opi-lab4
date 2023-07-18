package client.cardWindow.Cards;

public enum CardSuit {
    Hearts(0),
    Tiles(1),
    Clovers(2),
    Spades(3);
    public final int id;
    CardSuit(int id){
        this.id = id;
    }
    public static CardSuit fromId(int id){
        for(CardSuit cs: CardSuit.values()){
            if(cs.id==id){
                return cs;
            }
        }
        return  Hearts;
    }
    public static int fromName(CardSuit crdSuit){
        for(CardSuit cn: CardSuit.values()){
            if(cn==crdSuit){
                return cn.id;
            }
        }
        return 0;
    }
}
