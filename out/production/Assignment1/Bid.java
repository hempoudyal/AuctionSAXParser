public class Bid {
    private String userID;
    private String rating;
    private String location;
    private Double country;

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getRating(){
        return  rating;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(){
        this.location = location;
    }

}