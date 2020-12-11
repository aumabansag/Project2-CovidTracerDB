//package Covid19TracerApp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;

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
		// //pack();
	}

	//login panel 
	private void initLogin(){
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
				if(controller.establishmentExists(estName)){
					establishmentID = controller.logIn(estName, estPass);
					if(establishmentID>0){
						if(estName.equals("UPCT19")){ //contact tracers
							viewingScreen();
						}else{ //other establishments
							listingScreen();
						}
						remove(loginPanel);
						revalidate();
					}else{//wrong password
						JOptionPane.showMessageDialog(null,"Wrong Password",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
					}
				}else{
					//establishment does not exists pop up
					JOptionPane.showMessageDialog(null,"Establishment does not Exist.\nContact"+
						" the developer to register.","Login Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.add(loginPanel);
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

		//label for establishment

		JPanel listingPanel = new JPanel(new GridLayout(10,2,2,5));
		//bounds are set as (frameW-W)/2
		listingPanel.setBounds(50, 110, this.getWidth()-100, this.getHeight()-160);

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
	                "Pisting Yawa", JOptionPane.ERROR_MESSAGE);
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
	                        "Pisting Yawa", JOptionPane.ERROR_MESSAGE);
						}

				}catch(NumberFormatException nf){
					nf.printStackTrace();
					JOptionPane.showMessageDialog(null,"Piste not an ID number!",
	                "Pisting Yawa", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		this.add(listingPanel);
	}

	//screen for contact tracers
	private void viewingScreen(){
		//panel that holds the search engine
		JPanel searchPanel = new JPanel(new FlowLayout());
		//change the width and height to follow the frame
		searchPanel.setBounds(10, 60, this.getWidth()-20, 50);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel searchTitle = new JLabel("Search", JLabel.LEFT);
		JTextField searchbox = new JTextField("Name or ID",15);

		String[] categories = {"1st-Level Contact","2nd-Level Contact","Establishments"};
		JComboBox<String> traceType = new JComboBox<>(categories);
		//add more for JCombobox functionality

		JButton traceButton = new JButton("Trace");
		searchPanel.add(searchTitle);
		searchPanel.add(searchbox);
		searchPanel.add(traceType);
		searchPanel.add(traceButton);

		traceButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String contact = searchbox.getText();
				String traceTypeString = traceType.getSelectedItem().toString();

				NCoV19TracerUI.this.showTable(controller.tracerQuery(contact, traceTypeString));
			}
		}); 
		this.add(searchPanel);
	}

	private void showTable(JTable table){
		JPanel tableResultPanel = new JPanel(new FlowLayout());
		tableResultPanel.setBounds(10, 120, NCoV19TracerUI.this.getWidth()-20, NCoV19TracerUI.this.getHeight()-180); //make this dynamic
		tableResultPanel.add(new JScrollPane(table));

		NCoV19TracerUI.this.getContentPane().add(tableResultPanel);
		revalidate();
	}

	//modify this to only be included in the contact tracers
	private void initMenu(){
		final JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		final JMenu aboutMenu = new JMenu("About");

		JMenuItem registerEstab = new JMenuItem("Register New Establishment");
		registerEstab.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//add for registering new establishment
				//use popup
			}
		});

		//add more jmenu item for fileMenu
		JMenuItem updateMenuItem = new JMenuItem("Update DB");
		updateMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//add for recieving sql file
			}
		});

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		updateMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//perform necessary end for transactions
			}
		});

		//add to jmenu
		fileMenu.add(registerEstab);
		fileMenu.add(updateMenuItem);
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);
		this.setJMenuBar(menuBar);
	}

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				new NCoV19TracerUI().setVisible(true);
			}
		});
	}
}