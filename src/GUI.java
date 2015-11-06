import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame
implements ActionListener, MouseListener,MouseMotionListener{ 	
	int n=0;
	double[][] gains = new double[20][20];
	boolean add =false;
	Circle selected;
  	private List<Circle> shapes = new ArrayList<Circle>();
    static JFrame frame1 = new JFrame();
    static JButton quitbutton = new JButton("Quit");
    static JButton clearbutton = new JButton("Clear");
    //static JButton delete = new JButton("Delete");
    static JButton addNode = new JButton("Add Node");
    static JButton addEdge = new JButton("Add Edge");
    static JButton solve = new JButton("Solve");
    static JPanel panel2 = new JPanel();
    static JPanel panel1 = new JPanel();
    static JPanel panel3 = new JPanel();
    TextArea t = new TextArea();
	static JComponent createVerticalSeparator() {  
		JSeparator x = new JSeparator(SwingConstants.VERTICAL);  
		x.setPreferredSize(new Dimension(3,50)); 
		return x;  
	} 
    public void addPanel1(){
		panel1.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
		panel1.setBackground(Color.WHITE);
    }
    public void addPanel2(){
    	panel2.setBackground(Color.getHSBColor((float) 0.5,(float) 0.09,1));
		panel2.add(addNode);
		panel2.add(addEdge);
		panel2.add(createVerticalSeparator());   	 
        panel2.add(solve);
       // panel2.add(createVerticalSeparator());  
       // panel2.add(delete);            
	    panel2.add(createVerticalSeparator());  
        panel2.add(clearbutton);
        panel2.add(quitbutton);
        quitbutton.addActionListener(this);
        clearbutton.addActionListener(this);
        addNode.addActionListener(this);
        addEdge.addActionListener(this);
        solve.addActionListener(this);
        //delete.addActionListener(this);
    }

	
	public GUI() {
        this.setTitle("Signal Flow Graph Solver");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPanel2();
        addPanel1();
		panel3.setBorder(BorderFactory.createLineBorder(Color.lightGray, 10));
        t = new TextArea("",36,27);
    	panel3.add(t);
        panel2.setPreferredSize(new Dimension(1000,70));
        panel1.setPreferredSize(new Dimension(1000,590));
        panel3.setPreferredSize(new Dimension(230,590));
        this.addMouseListener(this);
        this.setLayout(new BorderLayout());
        this.add(panel2, BorderLayout.NORTH);
        this.add(panel1,BorderLayout.CENTER);
        this.add(panel3, BorderLayout.EAST);
        this.addMouseMotionListener(this);
	 }
//	public void remove(){
//		shapes.remove(selected);
//		repaint();
//	}
	private void addN(MouseEvent me){
		if(add){
			Circle s = new Circle();
		    s.position = new Point(me.getX(),me.getY());
		    s.radius = 30;
		    s.number = n;
		    n++;
	        shapes.add(s);
	        add=false;
		}
		else{
			selected=null;
	 		for (Circle p : shapes){
	             if (p.isAt(me.getPoint()) && selected==null){ 
	            	 selected=p;
	             }
	 		}
		}
	}
	private void addE(){
		JTextField to = new JTextField(5);
        JTextField from = new JTextField(5);
        JTextField weight = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("From:"));
        myPanel.add(from);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("TO:"));
        myPanel.add(to);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Weight:"));
        myPanel.add(weight);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
            "Please Enter The Required Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
        	int t = Integer.parseInt(to.getText());
        	int f = Integer.parseInt(from.getText());
        	double w = Double.parseDouble(weight.getText());
        	if(t>=0 && t< n && f>=0 && f< n){
        		gains[f][t] = w;
        		
        	}
        	
        }
        repaint();
	}
	private void solveGraph(){
		JTextField s = new JTextField(5);
        JTextField d = new JTextField(5);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Source Node:"));
        myPanel.add(s);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Destination Node:"));
        myPanel.add(d);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
            "Please Enter The Required Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
        	int sr= Integer.parseInt(s.getText());
        	int ds= Integer.parseInt(d.getText());
        	if(sr>=0&&sr<=n&&ds>=0&&ds<=n&&sr!=ds){
	        	SFGLogic sfg = new SFGLogic(n,gains,sr,ds);
	        	panel3.removeAll();
	        	t = new TextArea(sfg.excute().toString(),36,27);
	        	panel3.add(t);
        	}

        }
        repaint();
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
	 
	 }
	@Override
	public void mouseClicked(MouseEvent me) {
		//addN(me);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent me) {
		// TODO Auto-generated method stub
	      
	}
	
	@Override
	public void mouseReleased(MouseEvent me) {
		addN(me);
        repaint();         			
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource()==quitbutton){
            System.exit(0);
        }
        
        else if(arg0.getSource()==solve){
        	solveGraph();
        	repaint(); 
        }
//        else if(arg0.getSource()==delete){
//        	remove();
//        }
        else if(arg0.getSource()==clearbutton){
            panel1.setForeground(null);
            shapes.clear();
            gains = new double[20][20];
            n=0;
            panel3.removeAll();
            t = new TextArea("",36,27);
        	panel3.add(t);
        	repaint();
        }
        else if(arg0.getSource()==addNode){
        	add=true;
        }
        else if(arg0.getSource()==addEdge){
        	addE();
        	repaint();
        }
	}
	public void paint(Graphics g) {
	    super.paintComponents(g);
	    for (Circle shape : shapes) { 
	      shape.draw(g);
	    }
//	    for (Arc shape : Arcs) { 
//		      shape.draw(g);
//		}
	    for(int i=0;i<n;i++){
	    	for(int j=0;j<n;j++){
	    		if(gains[i][j]!=0){
	    			if(i<j){
	    				Arc a = new Arc(shapes.get(i).position,shapes.get(j).position ,gains[i][j],true);
	    				a.draw(g);
	    			}
	    			else if(i>j){
	    				Arc a = new Arc(shapes.get(i).position,shapes.get(j).position ,gains[i][j],false);
	    				a.draw(g);
	    			}
	    			else{
	    				SelfLoop a = new SelfLoop(gains[i][j], shapes.get(i).position);
	    				a.draw(g);
	    			}
	    			
	    		}
	    	}
	    }
	}
	public static void main(String[] args) {
		  JFrame.setDefaultLookAndFeelDecorated(true);
	      GUI frame = new GUI();  
	      frame.pack();
	      frame.setVisible(true);
	}
}
