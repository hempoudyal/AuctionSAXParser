public class Auction {
    private int id;
    private String name;
    private String category;
    private Double currently; // can be removed later
    private Double firstBid; // ""
    private Double buyPrice;
    private int numOfBids;
    private String startedTime;
    private String endsTime;
    private String description;
    private String sellerId;
    private String locationName;


    public int getId(){
        return id;
    }

    public void setId(int i){
        this.id = i;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Double getBuyPrice(){
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice){
        this.buyPrice = buyPrice;
    }

    public double getFirstBid(){
        return firstBid;
    }

    public void setFirstBid(double firstBid){
        this.firstBid = firstBid;
    }

    public double getCurrently(){
        return currently;
    }

    public void setCurrently(double currently){
        this.currently = currently;
    }

    public int getNumOfBids(){
        return numOfBids;
    }

    public void setNumOfBids(int numOfBids){
        this.numOfBids = numOfBids;
    }

    public String getStartedTime(){
        return startedTime;
    }

    public void setStartedTime(String startedTime){
        this.startedTime = startedTime;
    }

    public String getEndsTime(){
        return startedTime;
    }

    public void setEndsTime(String endsTime){
        this.endsTime = endsTime;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getSellerId(){
        return sellerId;
    }

    public void setSellerId(String sellerId){
        this.sellerId = sellerId;
    }

    public String getLocationName(){
        return locationName;
    }

    public void setLocationName(String locationName){
        this.locationName = locationName;
    }


    @Override
    public String toString() {
        return "Auction [ItemID=" + id + ",Name=" + name  + "]";
    }
}