/* Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 */

import java.io.*;
import java.text.*;
import java.util.*;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;


public class MySAX extends DefaultHandler
{
	private boolean hasName = false;
	private boolean hasFirstBid = false;
	private boolean hasCategory = false;
	private boolean hasBuyPrice = false;
	private boolean hasNumberOfBids = false;
	private boolean hasStarted = false;
	private boolean hasEnds = false;
	private boolean hasDescription = false;
	private boolean hasLocationName = false;
	private boolean hasCountry = false;
	private boolean hasBid = false;
	private boolean hasBidLocationName = false;
	private boolean hasBidCountry = false;
	private boolean hasBidTime = false;
	private boolean hasBidAmount = false;
	private String currentValue = null ;

	public static class User{
		public String UserID;
		public String countryName;
		public String locationName;

		public User(String UserID, String countryName, String locationName) {
			this.UserID = UserID;
			this.countryName = countryName;
			this.locationName = locationName;
		}
	}

	public static class Rating{
		public String UserID;
		public int isSeller;
		public int rating;

		public Rating(String UserID, int isSeller, int rating) {
			this.UserID = UserID;
			this.isSeller = isSeller;
			this.rating = rating;
		}
	}

	final static String auctionsTable = "auctions.csv";
	final static String auctionsCategoriesTable = "auctions_categories.csv";
	final static String categoriesTable = "categories.csv";
	final static String locationsTable = "locations.csv";
	final static String countriesTable = "countries.csv";
	final static String usersTable = "users.csv";
	final static String bidsTable = "bids.csv";
	final static String ratingsTable = "ratings.csv";

	final static String oldTimeFormat = "MMM-dd-yy HH:mm:ss";
	final static String newTimeFormat = "yyyy-MM-dd HH:mm:ss";

	//List to hold Auctions object
	private List<Auction> auctionList = null;
	private Auction auction = null;

	//List to hold Bids object
	private List<Bid> bidList = null;
	private Bid bid = null;

	//List to hold temporary categories, countries
	private List<String> categoryList = null;
	private List<String> countries = new ArrayList<String>();
	private List<String> locations = new ArrayList<String>();

	//getter method for Auction list
	public  List<Auction> getAuctionList() {
		return  auctionList;
	}
	public List<String> getCountries() {
		return countries;
	}
	public List<String> getLocations() {
		return locations;
	}

	public static void main (String args[])
	throws Exception {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		MySAX handler = new MySAX();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);

		// Parse each file provided on the
		// command line.
//		for (int i = 0; i < args.length; i++) {
//	   	 	FileReader r = new FileReader(args[i]);
//	  	  	xr.parse(new InputSource(r));
//			r.close();
//		}
		//String s = "Merry Christmas &amp; Happy New Year";
		//System.out.println(handleString(s));

		for (int i = 0; i < 40; i++){
			String fileName = "ebay-data/items-"+i+".xml";
			FileReader r = new FileReader(fileName);
			xr.parse(new InputSource(r));
			r.close();
		}

		//FileReader r = new FileReader("items-0.xml");
		//xr.parse(new InputSource(r));

		try {
			//initialize table printers
			PrintWriter pwAuction = new PrintWriter(new FileOutputStream(auctionsTable, false), true);
			PrintWriter pwBids = new PrintWriter(new FileOutputStream(bidsTable, false), true);
			PrintWriter pwCountries = new PrintWriter(new FileOutputStream(countriesTable, false), true);
			PrintWriter pwLocations = new PrintWriter(new FileOutputStream(locationsTable, false), true);
			PrintWriter pwAuctionCategories = new PrintWriter(new FileOutputStream(auctionsCategoriesTable, false), true);
			PrintWriter pwCategories = new PrintWriter(new FileOutputStream(categoriesTable, false), true);
			PrintWriter pwUsers = new PrintWriter(new FileOutputStream(usersTable, false), true);
			PrintWriter pwRatings = new PrintWriter(new FileOutputStream(ratingsTable, false), true);

			//Print Countries Table
			List<String> countries = handler.getCountries();
			if (countries.size() > 0) {
				Set<String> countriesSet = new HashSet<String>(countries);

				for (String country: countriesSet) {
					pwCountries.println("\""+country+"\"");
				}
			}

			//Print Locations Table
			List<String> locations = handler.getLocations();
			if (locations.size() > 0) {
				Set<String> locationSet = new HashSet<String>(locations);

				for (String loc: locationSet){
					pwLocations.println("\""+loc+"\"");
				}
			}

			//Create list for tables
			List<String> catList = new ArrayList<>();
			List<User> userList = new ArrayList<User>();
			List<Rating> ratingList = new ArrayList<Rating>();
			List<Auction> auctionList = handler.getAuctionList();

			//print out info
			for (Auction auction : auctionList) {

				StringBuilder sb = new StringBuilder();

				String buyPrice = auction.getBuyPrice();
				if (auction.getBuyPrice() == null){
					buyPrice = "\\N";
				}

				String latitude = auction.getLatitude();
				if (auction.getLatitude() == null){
					latitude = "\\N";
				}

				String longitude = auction.getLongitude();
				if (auction.getLatitude() == null){
					longitude = "\\N";
				}

				//Print auctions Table
				User user = new User(auction.getSellerId(), auction.getCountry(), auction.getLocationName());
				userList.add(user);
				Rating rating = new Rating(auction.getSellerId(), 1, auction.getRating());
				ratingList.add(rating);

				sb.append("\""+auction.getId()+"\""+","+"\""+auction.getName()+"\""+","+"\""+auction.getFirstBid()+"\""+","+"\""+buyPrice+"\""+
						","+"\""+auction.getStartedTime()+"\""+","+"\""+auction.getEndsTime()+"\""+","+"\""+auction.getDescription()+"\""+
						","+"\""+latitude+"\""+","+"\""+longitude+"\""+
						","+"\""+auction.getSellerId()+"\""+","+"\""+auction.getCountry()+"\""+","+"\""+auction.getLocationName()+"\"");
				pwAuction.println(sb);

				//Print Bids Table
				if (auction.getBidList() != null){
					for(Bid bid: auction.getBidList()) {
						System.out.println(bid);
						pwBids.println("\""+bid.getTime()+"\""+","+"\""+
								bid.getAmount()+"\""+","+"\""+auction.getId()+"\""+","+"\""+bid.getUserID()+"\"");
						User user2 = new User(bid.getUserID(), bid.getCountry(), bid.getLocation());
						userList.add(user2);
						Rating rating2 = new Rating(bid.getUserID(),0, bid.getRating());
						ratingList.add(rating2);
					}
				}

				//Create Category List
				for(String cat: auction.getCategory()){
					catList.add(cat);
					pwAuctionCategories.println("\""+auction.getId()+"\""+","+"\""+cat+"\"");
				}

			}

			//Filter duplicates from categories and print
			Set<String> catSet = new HashSet<String>(catList);
			for (String c: catSet){
				pwCategories.println("\""+c+"\"");
			}


			//Filter duplicates from user list and print
			List<User> userUniques = new ArrayList<>();
			Set<String> userIDs = new HashSet<String>();

			for (User u: userList){
				if (userIDs.add(u.UserID)){
					userUniques.add(u);
				}
			}

			for (User u: userUniques){
				pwUsers.println("\""+u.UserID+"\""+","+"\""+u.countryName+"\""+","+"\""+u.locationName+"\"");
			}

			//Filter duplicates from Rating List and print
			List<Rating> ratingUniques = new ArrayList<>();
			Set<String> userIDsRating = new HashSet<String>();

			for (Rating rate: ratingList){
				if (userIDsRating.add(rate.UserID)){
					ratingUniques.add(rate);
				}
			}

			for (Rating rate: ratingUniques){
				pwRatings.println("\""+rate.UserID+"\""+","+"\""+rate.isSeller+"\""+","+"\""+rate.rating+"\"");
			}


		} catch (Exception E) {

		}

	}

    public MySAX ()
    {
	super();
    }

    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

	static String convertDate(String oldDateString){
		String newDateString;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(oldTimeFormat);
			Date d = sdf.parse(oldDateString);
			sdf.applyPattern(newTimeFormat);
			newDateString = sdf.format(d);
			return newDateString;
		} catch (Exception e){
			return oldDateString;
		}
	}

	static String handleString(String str){
		return str.replace("\\", "\\\\").replace("\"","\\\"");
	}

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////

    public void startDocument ()
    {
	System.out.println("Start document");
    }

    public void endDocument ()
    {
	System.out.println("End document");
    }


    public void startElement (String uri, String name,
			      String qName, Attributes atts)
	{
		if(qName.equals("Item")){
			String id = atts.getValue("ItemID");
			auction = new Auction();
			auction.setId(Integer.parseInt(id));
			//initialize auction
			if (auctionList == null)
				auctionList = new ArrayList<>();

			if (categoryList == null)
				categoryList = new ArrayList<>();
			else
				categoryList.clear();

		} else if (qName.equals("Name")){
			hasName = true;
		} else if (qName.equals("First_Bid")){
			hasFirstBid = true;
		} else if (qName.equals("Number_of_Bids")){
			hasNumberOfBids = true;
		} else if (qName.equals("Buy_Price")){
			hasBuyPrice = true;
		} else if (qName.equals("Location")){
			if (hasBid){
				hasBidLocationName = true;
			} else {
				auction.setLatitude(atts.getValue("Latitude"));
				auction.setLongitude(atts.getValue("Longitude"));
				hasLocationName = true;
			}
		} else if (qName.equals("Started")){
			hasStarted = true;
		} else if (qName.equals("Ends")){
			hasEnds = true;
		} else if (qName.equals("Description")){
			hasDescription = true;
		} else if(qName.equals("Category")){
			hasCategory = true;
		} else if(qName.equals("Seller")){
			String id = atts.getValue("UserID");
			auction.setSellerId(id);
			String rating = atts.getValue("Rating");
			auction.setRating(Integer.parseInt(rating));
		} else if(qName.equals("Country")){
			if (hasBid) {
				hasBidCountry = true;
			} else{
				hasCountry = true;
			}
		} else if(qName.equals("Bid")){
			bid = new Bid();
			if (bidList == null)
				bidList = new ArrayList<>();

			hasBid = true;
		} else if(qName.equals("Bidder")){
			String id = atts.getValue("UserID");
			bid.setUserID(id);

			String rating = atts.getValue("Rating");
			bid.setRating(Integer.parseInt(rating));
		} else if(qName.equals("Time")){
			hasBidTime = true;
		} else if(qName.equals("Amount")){
			hasBidAmount = true;
		}

    }


    public void endElement (String uri, String name, String qName)
    {
    	if(qName.equals("Item")){
    		//Add categoryList to auction
			List<String> c = new ArrayList<>(categoryList);
    		auction.setCategory(c);

    		//Add auction to auctionList
    		auctionList.add(auction);
		} else if(qName.equals("Bid")){
    		bidList.add(bid);
    		hasBid = false;

		} else if(qName.equals("Bids")){
    		if (bidList != null) {
    			List<Bid> b = new ArrayList<>(bidList);
				auction.setBidList(b);
				bidList.clear();
			}
		} else if(qName.equals("Location")){
			locations.add(handleString(currentValue));
    		if (hasBid){
				bid.setLocation(handleString(currentValue));
    			hasBidLocationName = false;
			} else {
				auction.setLocationName(handleString(currentValue));
				hasLocationName = false;
			}
			currentValue = "";
		} else if(qName.equals("Description")){
    		currentValue = handleString(currentValue);
			if (currentValue.length() > 4000) {
				currentValue = currentValue.substring(0, 4000); //Description Characters limited to 4000
				currentValue.replaceFirst("\\*$", "");
			}
			auction.setDescription(currentValue);
			currentValue = "";
			hasDescription = false;
		} else if(qName.equals("Category")){
			categoryList.add(handleString(currentValue));
			currentValue = "";
			hasCategory = false;
		} else if(qName.equals("Country")){
			countries.add(handleString(currentValue));
    		if(hasBid){
				bid.setCountry(handleString(currentValue));
				hasBidCountry = false;
			} else {
    			auction.setCountry(handleString(currentValue));
    			hasCountry = false;
			}
			currentValue = "";
		}
    }


    public void characters (char ch[], int start, int length)
    {
		if (hasName){
    		String s = handleString(new String(ch, start, length));
			auction.setName(s);
			hasName = false;
		} else if (hasFirstBid){
			auction.setFirstBid(strip(new String(ch, start, length)));
			hasFirstBid = false;
		} else if (hasNumberOfBids){
    		auction.setNumOfBids(Integer.parseInt(new String(ch, start, length)));
    		hasNumberOfBids = false;
		} else if (hasBuyPrice){
    		auction.setBuyPrice(strip(new String(ch, start, length)));
    		hasBuyPrice = false;
		} else if (hasLocationName){
			currentValue = currentValue + new String(ch, start, length);

		} else if (hasStarted){
    		auction.setStartedTime(convertDate(new String(ch, start, length)));
    		hasStarted = false;
		} else if (hasEnds){
			auction.setEndsTime(convertDate(new String(ch, start, length)));
			hasEnds = false;
		} else if (hasDescription){
			currentValue = currentValue + new String(ch, start, length);
		} else if (hasCountry){
			currentValue = currentValue + new String(ch, start, length);
		} else if(hasBidAmount){
			bid.setAmount(Double.parseDouble(strip(new String(ch, start, length))));
			hasBidAmount = false;
		} else if(hasBidTime){
			bid.setTime(convertDate(new String(ch, start, length)));
			hasBidTime = false;
		} else if (hasBidCountry) {
			currentValue = currentValue + new String(ch, start, length);
		} else if(hasBidLocationName){
			currentValue = currentValue + new String(ch, start, length);
		} else if(hasCategory){
			currentValue = currentValue + new String(ch, start, length);
		}
    }

}
