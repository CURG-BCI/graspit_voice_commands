
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class recognitionTest
{
    public static enum InputState {
        TRANSITIONING,
        LOW,
        MED,
        HIGH
    }
	 private static Socket socket;
	    private static OutputStream os;
	    private static DataOutputStream ds;
	    private static boolean connected;
		
		public static boolean initSocketConnections() //throws UnknownHostException, IOException
	    {try {
			socket=new Socket("127.0.0.1",4775);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			os=socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		ds=new DataOutputStream(os);
		System.out.println("Socket connected");
		connected=true;
		return true;
		}
		
		
	    public static void voicePause()
	    {String s=voceRecognize();
	        //s="start";
	        {System.out.println("Pausing");
	        while(!s.equalsIgnoreCase("start"))
	        {s=voceRecognize();
	         System.out.println("Paused! "+s);
	        }
	        System.out.println("Starting!");}
	        
	    }
	    public static String voceRecognize()
	    {    String s=voce.SpeechInterface.popRecognizedString();
	        if(s.length()>0)
	        {
	        System.out.println("Voce Recognized: "+s);
	        }
	        return s;
	    }
	    public static void main(String[] argv) throws IOException
	{	boolean status=initSocketConnections();
	    	if(!status)
	    	{
	    		System.out.println("Connection Failed");
	    		
	    	}
	    	else
	    	{	
	    		voce.SpeechInterface.init("/home/srihari/Downloads/voce-0.9.1/lib", false, true,
	    	            "/home/srihari/Downloads/voce-0.9.1/lib/gram", "speech");

	        System.out.println("This is a speech recognition test. ");

	        String word="";
	        voicePause();
	        String input="";
	        while (!input.equalsIgnoreCase("finish"))
	            {
	                
	            input=voceRecognize();
	    

	            if(input.equalsIgnoreCase("pause"))
	            {
	                voicePause();
	            }
	            else if(input.equalsIgnoreCase("next"))
	            {	
	            	//ds.write("".getBytes());
	                ds.write((InputState.MED.ordinal()+"\n").getBytes());
	                System.out.println("Next");
	           
	            }
	            else if(input.equalsIgnoreCase("Select"))
	            { //ds.write("".getBytes());
	           ds.write((InputState.HIGH.ordinal()+"\n").getBytes());
	            System.out.println("Select");
	           
	            }

	        }

	        voce.SpeechInterface.destroy();
	        System.exit(0);
	    	}
	    }
	}