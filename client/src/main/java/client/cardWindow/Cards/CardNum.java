package client.cardWindow.Cards;

public enum CardNum {
    Ace(0),
    Two(1),
    Three(2),
    Four(3),
    Five(4),
    Six(5),
    Seven(6),
    Eight(7),
    Nine(8),
    Ten(9),
    Jack(10),
    Queen(11),
    King(12);
    public final int id;
    CardNum(int id){
        this.id = id;
    }
    public static CardNum fromId(int id){
        for(CardNum cn: CardNum.values()){
            if(cn.id==id){
                return cn;
            }
        }
        return Two;
    }
    public static int fromName(CardNum crdNum){
        for(CardNum cn: CardNum.values()){
            if(cn==crdNum){
                return cn.id;
            }
        }
        return 0;
    }
}
