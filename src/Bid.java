public class Bid {
        private String userID;
        private int rating;
        private String location;
        private String country;
        private String time;
        private Double amount;

        public String getUserID(){
            return userID;
        }

        public void setUserID(String userID){
            this.userID = userID;
        }

        public int getRating(){
            return  rating;
        }

        public void setRating(int rating){
            this.rating = rating;
        }

        public String getLocation(){
            return location;
        }

        public void setLocation(String location){
            this.location = location;
        }

        public String getCountry(){
            return country;
        }

        public void setCountry(String country){
            this.country = country;
        }

        public String getTime(){
            return time;
        }

        public void setTime(String time){
            this.time = time;
        }

        public Double getAmount(){
            return amount;
        }

        public void setAmount(Double amount){
            this.amount = amount;
        }

}
