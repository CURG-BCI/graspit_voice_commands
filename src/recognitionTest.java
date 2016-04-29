
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;	

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
	    private static boolean running;
	   static JFrame ui;
	    static JLabel label;
	    static JLabel label_word;
	    static JButton start;
	    static JButton connect;
	    @SuppressWarnings("deprecation")
		public static void initUI()
	    {
	    	ui=new JFrame("Voice Control");
	    	running=false;
	    	JPanel connectPanel=new JPanel();
	    	connectPanel.setLayout(new GridLayout(1,2,0,0));
	    	
	    	start=new JButton("Start");
	    	connectPanel.add(start);
	    	connect=new JButton("Connect");
	    	connectPanel.add(connect);
	    	
	  	  connect.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	  if(initSocketConnections())
		        	  {
		        		  ui.setTitle("Voice Control [Real Time]");
		        	  }
		        	  else
		        	  {
		        		  ui.setTitle("Voice Control [Debug]");
		        	  }
		             
		          }          
		       });
	  	  
	  	  
	    	
	    	final JPanel statusPanel=new JPanel();
	    	statusPanel.setLayout(new GridLayout(1,1,0,0));
	    	label=new JLabel("Paused",JLabel.CENTER);
	    	label_word=new JLabel("Words",JLabel.CENTER);
	    	label_word.setBackground(Color.orange);
	    	statusPanel.add(label);
	    	JPanel recognizedWordsPanel=new JPanel();
	    	recognizedWordsPanel.setLayout(new GridLayout(1,1,0,0));
	    	recognizedWordsPanel.add(label_word);
	    	statusPanel.setBackground(Color.red);
		  	  start.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) {
			        	  if(running==true)
			        	  {	  running=false;
			        	  		statusPanel.setBackground(Color.red);
			        		  label.setText("Paused");
			        		  start.setText("Start");
			        		 
			        	  }
			        	  else
			        	  {	   running=true;
			        	  		statusPanel.setBackground(Color.green);
			        		  label.setText("Live");
			        		  start.setText("Pause");
			        	  }
			             
			          }          
			       });
	    	ui.setLayout(new GridLayout(3,1));
	    	ui.add(statusPanel);
	    	ui.add(recognizedWordsPanel);
	    	ui.add(connectPanel);
	  	  ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  //ui.setSize(400,600);
		  ui.pack();
		  ui.setVisible(true);
		 
	    	
	    	
	  }
		
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
	    {	
	    	String s="";
	        //s="start";
	        {System.out.println("Pausing");
	        while(!s.equalsIgnoreCase("start") && running==false)
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
	{	//boolean status=initSocketConnections();
	    	
			initUI();
//			if(!status)
//	    	{
//	    		System.out.println("Connection Failed");
//	    		
//	    	}
	    	//else
	    	{	

	    		voce.SpeechInterface.init("/home/armuser/ros/graspit_bci_plugin/graspit_bci_ws/src/graspit_voice_commands/voce-0.9.1/lib", false, true,
	    	            "/home/armuser/ros/graspit_bci_plugin/graspit_bci_ws/src/graspit_voice_commands/voce-0.9.1/lib/gram", "speech");


	        System.out.println("This is a speech recognition test. ");
	        
	        String word="";
	        voicePause();
	        String input="";
	        while (true)
	            {
	             
	            input=voceRecognize();
	            
	            if(input.length()>0)
	            {System.out.println("Input: "+input);
	            label_word.setText(input);
	            }
	            if(input.equalsIgnoreCase("pause")||running==false)
	            {	
	                voicePause();
	            }
	            else if(input.equalsIgnoreCase("next"))
	            {	if(connected)
	            	{ds.write("".getBytes());
	                ds.write((InputState.MED.ordinal()+"\n").getBytes());}
	                System.out.println("Next");
	           
	            }
	            else if(input.equalsIgnoreCase("Select"))
	            {if(connected)
	           {ds.write("".getBytes());
	           ds.write((InputState.HIGH.ordinal()+"\n").getBytes());}
	            System.out.println("Select");
	           
	            }

	        }

	       // voce.SpeechInterface.destroy();
	       // System.exit(0);
	    	}
	    }
	}