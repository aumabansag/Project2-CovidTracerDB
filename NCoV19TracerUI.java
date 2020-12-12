//package Covid19TracerApp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class NCoV19TracerUI extends JFrame{
	private String establishment;
	private NCoV19TracerController controller = null;
	private int establishmentID;

	//constructor
	public NCoV19TracerUI(){
		initUI();
		controller = new NCoV19TracerController(this);
	}

	//initialize the UI for the user
	private void initUI(){
		//FURNISH THIS
		setSize(500,500);
		//setResizable(false);
		setLayout(null);
		//setUndecorated(true);
		setLocationRelativeTo(null);
		initLogin();
		// initMenu();

		//make this center EDIT PA
		JLabel title = new JLabel("UP Contact Tracing\n DataBase", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 30));
		title.setBounds(0, 0, this.getWidth(), 50);

		this.add(title);
		//pack();
	}

	//login panel 
	private void initLogin(){
		this.setJMenuBar(null);
		this.getContentPane().repaint();
		loadMenu(false, false);
		JLabel companyName = new JLabel("Establishment ");
		JLabel password = new JLabel("Password ");
		JTextField companyTF = new JTextField(10);
		JPasswordField companyPass = new JPasswordField(10);
		JButton loginSubmit = new JButton("Login");

		JPanel loginPanel = new JPanel(new GridLayout(3,2,2,5));
		loginPanel.setBounds((this.getWidth()-300)/2, (this.getHeight()-50)/2, 300, 80);

		loginPanel.add(companyName);
		loginPanel.add(companyTF);
		loginPanel.add(password);
		loginPanel.add(companyPass);
		loginPanel.add(loginSubmit);

		loginSubmit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String estName = companyTF.getText();
				String estPass = new String (companyPass.getPassword());
				//send credentials to the database for checking
				establishmentID = controller.logIn(estName, estPass);

				if(controller.establishmentVerified(establishmentID)){
						NCoV19TracerUI.this.getContentPane().remove(loginPanel);
						revalidate();
						if(estName.equals("UPCT19")){ //contact tracers
							loadMenu(true, true);
							viewingScreen();
						}else{ //other establishments
							loadMenu(false, true);
							listingScreen();
						}
				}else{
					//establishment does not exists pop up
					if(establishmentID!=-1)
						JOptionPane.showMessageDialog(null,"Wrong Name or Password.\nIf your establishment is"
						+"not yet registered,\nPlease contact the developers.","Login Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.add(loginPanel);
		this.revalidate();
	}

	//JMenu
	private void loadMenu(boolean tracer, boolean logged){
		//
		final JMenuBar menuBar = new JMenuBar();

		if(tracer){ //are contact tracers
			JButton regBut = new JButton("Register New");

			regBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){ //edit
					JPanel inputPanel = new JPanel(new GridLayout(4,2,2,2));
					JTextField id = new JTextField(10);
					JTextField name = new JTextField(10);
					JTextField addr = new JTextField(20);
					JTextField pass = new JTextField(10);
					inputPanel.add(new JLabel("Estab. ID #"));
					inputPanel.add(id);
					inputPanel.add(new JLabel("Name"));
					inputPanel.add(name);
					inputPanel.add(new JLabel("Address"));
					inputPanel.add(addr);
					inputPanel.add(new JLabel("Password"));
					inputPanel.add(pass);
					int i = JOptionPane.showConfirmDialog(null,inputPanel,"Register New Establishment",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if(i==0){
						try{
							int idno = Integer.parseInt(id.getText());
							String[] newEst = new String[4];
							
							newEst[0] = id.getText();
							newEst[1] = name.getText();
							newEst[2] = addr.getText();
							newEst[3] = pass.getText(); 
							controller.regEstablishment(newEst);
						}catch(NumberFormatException nee){
							JOptionPane.showMessageDialog(null,"Your entered an invalid ID number.",
									"Registration Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			menuBar.add(regBut);
		}

		JButton aboutBut = new JButton("About");
		aboutBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null,"COVID19 Tracer App"+
					"\nThis app is a project for CMSC 127 which digitized the"+
					"\nfilling of forms in the establishments. This also made"+
					"\ncontact tracing efficient by implementing DBMS."+
					"\n\nDevelopers:\nAdrian Mabansag"+
					"\nSamson Rollo Jr","ABOUT", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		menuBar.add(aboutBut);
		menuBar.add(Box.createHorizontalGlue());

		if(logged){
			JButton logoutBut = new JButton("LogOut");
			//logoutBut.set(10,5);
			logoutBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int choice = JOptionPane.showConfirmDialog(null,"You are about to log out!","LogOut",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if(choice==0){
						try{
							NCoV19TracerUI.this.getContentPane().remove(activePanel);
							revalidate();
							initLogin();
						}catch(Exception ne){}
					}
				}
			});
			menuBar.add(logoutBut);
		}

		this.setJMenuBar(menuBar);
	}

	//listing screen for the establishments
	private void listingScreen(){
		this.getContentPane().repaint();
		JLabel estLabel =  new JLabel(establishment);
		JLabel nameLabel = new JLabel("Name ");
		JLabel iDLabel = new JLabel("ID");
		JLabel addrLabel = new JLabel("Address");
		JLabel ageLabel = new JLabel("Age");
		JLabel contNumLabel = new JLabel("Contact #");
		JLabel custIDLabelOut = new JLabel("ID");
		JTextField nameField = new JTextField(15);
		JTextField idField = new JTextField(10);
		JTextField addrField = new JTextField(15);
		JTextField ageField = new JTextField(2);
		JTextField contNumField = new JTextField(11);
		JTextField idOutField = new JTextField(10);
		JButton add = new JButton("GO IN");
		JButton out = new JButton("GO OUT");
		//add label for establishment on top

		JPanel listingPanel = new JPanel(new GridLayout(10,2,2,5));
		listingPanel.setBounds(50, 70, this.getWidth()-100, this.getHeight()-160);
		listingPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		listingPanel.add(nameLabel);
		listingPanel.add(nameField);
		listingPanel.add(iDLabel);
		listingPanel.add(idField);
		listingPanel.add(addrLabel);
		listingPanel.add(addrField);
		listingPanel.add(ageLabel);
		listingPanel.add(ageField);
		listingPanel.add(contNumLabel);
		listingPanel.add(contNumField);
		listingPanel.add(add);
		listingPanel.add(new JLabel(""));
		listingPanel.add(new JLabel(""));
		listingPanel.add(new JLabel(""));
		listingPanel.add(new JLabel(""));
		listingPanel.add(new JLabel(""));

		listingPanel.add(custIDLabelOut);
		listingPanel.add(idOutField);
		listingPanel.add(out);

		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//assume all are valid entries
				if(!controller.userVisited(Integer.parseInt(idField.getText()))){
					String[] input = new String[8];
					input[0] = nameField.getText();
					input[1] = idField.getText();
					input[2] = addrField.getText();
					input[3] = ageField.getText();
					input[4] = contNumField.getText();
					input[5] = (java.time.LocalTime.now()).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")).toString();//(java.time.LocalTime.now()).toString(); //change to date fromat later
					input[6] = (java.time.LocalDate.now()).toString();
					input[7] = String.valueOf(establishmentID);

					controller.addRow(input);
					//ADD POP U{ SAYING THANK YU BETCH}
					nameField.setText("");
					idField.setText("");
					addrField.setText("");
					ageField.setText("");
					contNumField.setText("");
				}else{
					JOptionPane.showMessageDialog(null,"Person is already IN!",
	                "Signup Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		out.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent f){
				try{
					int id = Integer.parseInt(idOutField.getText());
					//check if id is in the db, else pop up error
						if(controller.userVisited(id)){
							String outTime = (java.time.LocalTime.now()).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")).toString();
							controller.updateRow(id, outTime);
							idOutField.setText("");
							JOptionPane.showMessageDialog(null,"Thank you come again!",
	                        "Sign out", JOptionPane.INFORMATION_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null,"Person is not in yet!",
	                        "Sign out Error", JOptionPane.ERROR_MESSAGE);
						}

				}catch(NumberFormatException nf){
					nf.printStackTrace();
					JOptionPane.showMessageDialog(null,"Not an ID number!",
	                "Sign out Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		activePanel = listingPanel;
		this.add(listingPanel);
		this.revalidate();
	}

	//screen for contact tracers
	private void viewingScreen(){
		this.getContentPane().repaint();

		JPanel searchPanel = new JPanel(new FlowLayout());
		searchPanel.setBounds(10, 60, this.getWidth()-20, this.getHeight()-140);
		//searchPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel searchTitle = new JLabel("Search", JLabel.LEFT);
		JTextField searchbox = new JTextField("Enter ID",15);
		String[] categories = {"1st-Level Contact","2nd-Level Contact","Establishments"};
		JComboBox<String> traceType = new JComboBox<>(categories);
		JTextField fromDate = new JTextField("from: yyyy-MM-dd",10);
		JTextField toDate = new JTextField("to: yyyy-MM-dd",10);
		JButton traceButton = new JButton("Trace");

		searchPanel.add(searchTitle);
		searchPanel.add(searchbox);
		searchPanel.add(traceType);
		searchPanel.add(fromDate);
		searchPanel.add(new JLabel(" to "));
		searchPanel.add(toDate);
		searchPanel.add(traceButton);

		traceButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String contact = searchbox.getText();
				String traceTypeString = traceType.getSelectedItem().toString();

				try{
					java.sql.Date.valueOf(fromDate.getText());
					java.sql.Date.valueOf(toDate.getText());
					NCoV19TracerUI.this.showTable(controller.tracerQuery(contact, traceTypeString,
									fromDate.getText(), toDate.getText()));
				}catch(Exception eD){
					JOptionPane.showMessageDialog(null,"Invalid date input!",
	                        "Trace Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}); 
		activePanel = searchPanel;
		this.add(searchPanel);
		this.revalidate();
	}

	private void showTable(JTable table){
		Component[] list = NCoV19TracerUI.this.activePanel.getComponents();
		for(Component c: list)
			if(c instanceof JPanel)
				NCoV19TracerUI.this.activePanel.remove(c);

		NCoV19TracerUI.this.activePanel.revalidate();
		NCoV19TracerUI.this.activePanel.repaint();

		JPanel tableResultPanel = new JPanel(new FlowLayout());
		tableResultPanel.setBounds(10, 120, NCoV19TracerUI.this.getWidth()-20, NCoV19TracerUI.this.getHeight()-200); //make this dynamic
		tableResultPanel.add(new JScrollPane(table));

		NCoV19TracerUI.this.activePanel.add(tableResultPanel);
		NCoV19TracerUI.this.activePanel.revalidate();
	}

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new NCoV19TracerUI().setVisible(true);
			}
		});
	}

	private JPanel activePanel;
}