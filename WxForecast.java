/*
WxForecast.java
Januska, Edward
COP-3252
part of Project 2
3/12/12

This class uses the XML SAXParser to parse various hurricane weather forecasts from the National Weather Service (NWS). These products are updated during active hurricane events and may contain stale data outside of huuricane season. 

Designed for use on command line/terminal use.

Call action( LinkedList<String> ) to utilize WxForecast.

The action( LinkedList<String> list ) method is called from the JavaConsole driver but can be used with any driver since there are no dependancies.
   
Parameters are passed in as two elements in a linked list. Any extra parameters are ignored. Incorrect command parameters will result in a call to the manual method.

Missing second parameters (suffix) will return a default selection. Incorrect second parameters will result in a error message followed by a return.
   
The manual() method takes no parameters and returns a String containing usage information.
*/
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.LinkedList;
import java.net.*;
import java.io.*;
 
public class WxForecast extends DefaultHandler {
   
   String cmd    = new String();
   String suffix = new String();
   String url;
   
   public void action( LinkedList<String> list ) {
   
      // Check for command in list, could be anything for now,
      // switch statement will check for valid input
      if (  list.peekFirst() == null ) {
         System.out.println( this.manual() ); 
         return;
      }
	  
      // Populate cmd and suffix with commands and parameters from list
      cmd = list.poll();
      if (  list.peekFirst() == null ) {
         suffix = "";
      } else {
         suffix = list.poll();
      }
      // Select command to build URL
      switch ( cmd ) {
         case "-h": // If no suffix provided default to "a", Atlantic 
	    if ( suffix.compareTo("a") == 0 || suffix.compareTo("") == 0 )
	       setUrl( "http://www.nhc.noaa.gov/xml/HSFAT2.xml" );
	    if ( suffix.compareTo("n") == 0 )
	       setUrl( "http://www.nhc.noaa.gov/xml/HSFEP2.xml" );
	    if ( suffix.compareTo("s") == 0 )
	       setUrl( "http://www.nhc.noaa.gov/xml/HSFEP3.xml" );
	    if ( suffix.compareTo( "a" ) != 0 &&
	         suffix.compareTo( "n" ) != 0 &&
		 suffix.compareTo( "s" ) != 0 && 
		 suffix.compareTo( ""  ) != 0 ) {
	       System.out.println( "\nIncorrect parameter" );
	       return;
	    }
	    break;
	  case "-d":
	     setUrl( "http://www.nhc.noaa.gov/xml/MIMATS.xml" );
	     break;
	  case "-n": // If no suffix provided default to "m", Miami
	     if ( suffix.compareTo("m") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/OFFN04.xml" );
 	     if ( suffix.compareTo("j") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/OFFN05.xml" );
	     if ( suffix.compareTo("o") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/OFFN06.xml" );
	     if ( suffix.compareTo( "m" ) != 0 &&
	        suffix.compareTo( "j" ) != 0 &&
	        suffix.compareTo( "o" ) != 0 && 
	        suffix.compareTo( ""  ) != 0 ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
	  case "-r": // If no suffix provided default to "c", Central Carib
	     if ( suffix.compareTo("c") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/STDCCA.xml" );
	     if ( suffix.compareTo("e") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/STDECA.xml" );
	     if ( suffix.compareTo("w") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/STDWCA.xml" );
	     if ( suffix.compareTo( "c" ) != 0 &&
	        suffix.compareTo( "e" ) != 0 &&
	        suffix.compareTo( "w" ) != 0 && 
		    suffix.compareTo( ""  ) != 0 ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
      case "-a": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSAT1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSAT2.xml" );
	     if ( suffix.compareTo("3") == 0 )
            setUrl( "http://www.nhc.noaa.gov/xml/PWSAT3.xml" );
  	     if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSAT4.xml" );
	     if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSAT5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
      case "-p": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSEP1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSEP2.xml" );
	     if ( suffix.compareTo("3") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSEP3.xml" );
	     if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSEP4.xml" );
	     if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/PWSEP5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;			  
	  case "-t": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDAT1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDAT2.xml" );
	     if ( suffix.compareTo("3") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDAT3.xml" );
	     if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDAT4.xml" );
	     if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDAT5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
      case "-e": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDEP1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDEP2.xml" );
	     if ( suffix.compareTo("3") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDEP3.xml" );
	     if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDEP4.xml" );
	     if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCDEP5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
	  case "-y": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPAT1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPAT2.xml" );
	     if ( suffix.compareTo("3") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPAT3.xml" );
	     if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPAT4.xml" );
         if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPAT5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
	 case "-c": // If no suffix provided default to "1"
	     if ( suffix.compareTo("1") == 0 || suffix == "" )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPEP1.xml" );
	     if ( suffix.compareTo("2") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPEP2.xml");
	     if ( suffix.compareTo("3") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPEP3.xml" );
    	 if ( suffix.compareTo("4") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPEP4.xml" );
	     if ( suffix.compareTo("5") == 0 )
	        setUrl( "http://www.nhc.noaa.gov/xml/TCPEP5.xml" );
	     if ( isSuffixValid( suffix ) == false ) {
	        System.out.println( "\nIncorrect parameter" );
	        return;
	     }
	     break;
	  case "-m":
	     System.out.println( this.manual() );
	     return;
      default:
	     System.out.println( "Invalid command. Use -m for manual" );
	     return;
      } // End switch( cmd )
	  
      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
	     SAXParser saxParser = factory.newSAXParser();
	     factory.setNamespaceAware( true );

         DefaultHandler handler = new DefaultHandler() {
 
            // Called at the start of document element.
            public void startElement( String uri, String localName,String qName, 
              Attributes attributes ) throws SAXException {
              // Nothing to do here since not specific elements are needed.
            } // end startElement
	
            // Called at the end of document element.
	        public void endElement( String uri, String localName,
	        String qName ) throws SAXException {
	          // Nothing to do here since not specific elements are needed.
	        }
    
	        // Called in between the start and end tags of an XML document element.
            public void characters( char ch[], int start, int length ) throws SAXException {

			   final Pattern p = Pattern.compile( "<br />", Pattern.CASE_INSENSITIVE );
               final Pattern p2 = Pattern.compile( "<pre>", Pattern.CASE_INSENSITIVE );
	           // place all char into string builder
	           StringBuilder data = new StringBuilder( );
	           data.append( ch, start, length );
	           // Put data into a string to edit out tags
	           String parsedCData = new String( data.toString() );
	         
	           Matcher m = p.matcher( parsedCData );
               // if block ensures only data from CDATA section
 	           // is printed since its the only section with <br /> tags,
	           // remove tag and print the rest.
	           if ( m.find() == true ) {
	              // Remove all <br /> tags
	              String strOut = new String( m.replaceAll( "" ) );
	              System.out.print( strOut );
               }
	           // Some products do not contain <br /> tags
	           // but contains a <pre> tag in CDATA section 
	           m = p2.matcher( parsedCData );
	           if ( m.find() == true ) {
	              // Remove <pre> tag and print the rest
	              String strOut = new String( m.replaceAll( "" ) );			  
	              System.out.print( strOut );
	           }
            } // end characters()
         }; // End DefaultHandler handler = new DefaultHandler()

         // Parse the url. Callback methods of handler do the work.
         try {
            saxParser.parse( getUrl(), handler ); 
         }
         catch ( IllegalArgumentException e1 ) {
            System.out.println( "Parse Error Illegal Argument\n" );
         }
         catch ( IOException e2 ) {
            System.out.println( "Parse Error: IO\n" );
         }
         catch ( SAXException e3 ) {
            System.out.println( "Parse Error SAX Exception\n" );
         } // End try/catch block, parse
      } 
	  catch ( Exception e ) {
          System.out.println( "Exception occured in parser factory\n" );
          e.printStackTrace();
      } // End try/catch block
   } // End action()
   
   public String manual() {
      return "Use parameters to retrieve various weather products from NWS.\n\n" +
      "-h a  Atlantic High Seas Forecast \n" +
 	  "-h n  Northeast Pacific High Seas Forecast \n" +
	  "-h s  Southeast Pacific High Seas Forecast \n\n" +
	  "-d    Marine Weather Discussion \n\n" +
	  "-n m  NAVTEX Marine Forecast (Miami, FL)  \n" +
	  "-n j  NAVTEX Marine Forecast (San Juan, PR) \n" +
	  "-n o  NAVTEX Marine Forecast (New Orleans, LA) \n\n" +
	  "      Satellite Tropical Disturbance Rainfall Estimates \n" +
	  "-r c  for Central Caribbean \n" +
	  "-r e  for Eastern Caribbean\n" +
	  "-r w  for Western Caribbean. \n\n" +
	  "  When requesting the following products use number 1 - 5 as the \n" +
	  "second parameter to retrieve wallets 1 through 5.\n" +
	  "\n    ex: forecast -a 1\n\n" +
	  "-a    Atlantic Wind Speed Probabilities Wallet 1 through 5 \n" +
	  "-p    Pacific Wind Speed Probabilities Wallet 1 through 5 \n" +
	  "-t    Atlantic Post-Tropical Cyclone Discussions 1 through 5 \n" +
	  "-e    East Pacific Post-Tropical Cyclone Discussions 1 through 5 \n" +
	  "-y    Atlantic Tropical Cyclone Position Estimate Wallets 1 through 5 \n" +
	  "-c    East Pacific Tropical Cyclone Position Estimate Wallets 1 through 5 \n";
   }
		 
   private void setUrl( String u ) {
      url = u ;
   }
	 
   private String getUrl() {
      return url;
   }
   
   private boolean isSuffixValid( String s ){
      // Only checks suffix values for products that 
      // use numbers 1 - 5.
      if ( suffix.compareTo( "1" ) != 0 &&
           suffix.compareTo( "2" ) != 0 &&
           suffix.compareTo( "3" ) != 0 && 
           suffix.compareTo( "4" ) != 0 &&
           suffix.compareTo( "5" ) != 0 && 
           suffix.compareTo( ""  ) != 0 ) {
         return false;
      }  else {
         return true;
      }
   } // End isSuffixValid()
 
} // End class ParseWxFeed