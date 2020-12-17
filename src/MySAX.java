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

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


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


	//List to hold Auctions object
	private List<Auction> auctionList = null;
	private Auction auction = null;

	//getter method for Auction list
	public  List<Auction> getAuctionList() {
		return  auctionList;
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
//	   	 FileReader r = new FileReader(args[i]);
//	  	  xr.parse(new InputSource(r));
//		}

		FileReader r = new FileReader("items-0.xml");
		xr.parse(new InputSource(r));

		List<Auction> auctionList = handler.getAuctionList();

		try {
			PrintWriter writer = new PrintWriter("whatever.txt");

			//print out info
			for (Auction auction : auctionList) {
				System.out.println(auction);
				//saveRecord(auction, "~/Desktop/auction.txt");
//				writer.println(auction.getId()+","+auction.getName()+","+auction.getFirstBid()+","+
//						auction.getNumOfBids()+","+auction.getLocationName()+","+auction.getStartedTime()+","+
//						auction.getEndsTime()+","+auction.getSellerId()+","+auction.getDescription());
				writer.println(auction.getId()+","+auction.getName()+","+auction.getFirstBid()+","+auction.getNumOfBids()
				+","+auction.getLocationName()+","+auction.getStartedTime()+","+auction.getEndsTime()+","+auction.getSellerId()+","
				+auction.getDescription());
			}
		} catch (Exception E) {

		}
	}

    public  static  void saveRecord(Auction auction, String filepath){
		try{
			FileWriter fw = new FileWriter(filepath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			pw.println(auction.getId()+","+auction.getName());
			pw.flush();
			pw.close();

			JOptionPane.showMessageDialog(null,"Record saved");
		}
		catch (Exception E){

		}
	}

	public String removeFirstChar(String s){
		return s.substring(1);
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
		} else if (qName.equals("Name")){
			hasName = true;
		} else if (qName.equals("First_Bid")){
			hasFirstBid = true;
		} else if (qName.equals("Number_of_Bids")){
			hasNumberOfBids = true;
		} else if (qName.equals("Location")){
			hasLocationName = true;
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
		}


//	if ("".equals (uri))
//	    System.out.println("Start element: " + qName);
//	else
//	    System.out.println("Start element: {" + uri + "}" + name);
//	for (int i = 0; i < atts.getLength(); i++) {
//	    System.out.println("Attribute: " + atts.getLocalName(i) + "=" + atts.getValue(i));
//	}

    }


    public void endElement (String uri, String name, String qName)
    {
    	if(qName.equals("Item")){
    		auctionList.add(auction);
		}

//	if ("".equals (uri))
//	    System.out.println("End element: " + qName);
//	else
//	    System.out.println("End element:   {" + uri + "}" + name);
    }


    public void characters (char ch[], int start, int length)
    {
    	if (hasName){
			auction.setName(new String(ch, start, length));
			hasName = false;
		} else if (hasFirstBid){
			auction.setFirstBid(Double.parseDouble((new String(ch, start, length)).substring(1)));
			hasFirstBid = false;
		} else if (hasNumberOfBids){
    		auction.setNumOfBids(Integer.parseInt(new String(ch, start, length)));
    		hasNumberOfBids = false;
		} else if (hasLocationName){
    		auction.setLocationName(new String(ch, start, length));
    		hasLocationName = false;
		} else if (hasStarted){
    		auction.setStartedTime(new String(ch, start, length));
    		hasStarted = false;
		} else if (hasEnds){
			auction.setEndsTime(new String(ch, start, length));
			hasEnds = false;
		} else if (hasDescription){
    		auction.setDescription(new String(ch, start, length));
    		hasDescription = false;
		}

//	System.out.print("Characters:    \"");
//	for (int i = start; i < start + length; i++) {
//	    switch (ch[i]) {
//	    case '\\':
//		System.out.print("\\\\");
//		break;
//	    case '"':
//		System.out.print("\\\"");
//		break;
//	    case '\n':
//		System.out.print("\\n");
//		break;
//	    case '\r':
//		System.out.print("\\r");
//		break;
//	    case '\t':
//		System.out.print("\\t");
//		break;
//	    default:
//		System.out.print(ch[i]);
//		break;
//	    }
//	}
//	System.out.print("\"\n");
    }

}
