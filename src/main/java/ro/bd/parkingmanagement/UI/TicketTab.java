package ro.bd.parkingmanagement.UI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

import ro.bd.parkingmanagement.DAO.ParkingLotDAO;
import ro.bd.parkingmanagement.DAO.ParkingLotDAOImpl;
import ro.bd.parkingmanagement.DAO.TicketDAO;
import ro.bd.parkingmanagement.DAO.TicketDAOImpl;
import ro.bd.parkingmanagement.model.ParkingLot;
import ro.bd.parkingmanagement.model.Ticket;

public class TicketTab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private javax.swing.JTextField floor;
	private javax.swing.JLabel floorLabel;
	private javax.swing.JButton getTicketButton;
	private javax.swing.JLabel parkingSpaceLabel;
	private javax.swing.JTextField parkingSpaceNumber;
	private javax.swing.JPanel selectVehicleTypePanel;
	private javax.swing.JTextField ticketCode;
	private javax.swing.JLabel ticketCodeLabel;
	private javax.swing.JPanel ticketDetailsPanel;
	private javax.swing.JComboBox<String> vehicleTypeComboBox;
	private javax.swing.JLabel vehicleTypeLabel;
	private static final SimpleDateFormat datePlusTime = new SimpleDateFormat("dd.MM.YY.HH.mm.ss");
	private static int ticketNumber = 0;
	private static ParkingLotDAO parkingSpaceDAO = new ParkingLotDAOImpl();
	private static TicketDAO ticketDAO = new TicketDAOImpl();

	public TicketTab() {
		initComponents();
	}

	private void initComponents() {

		selectVehicleTypePanel = new javax.swing.JPanel();
		vehicleTypeComboBox = new javax.swing.JComboBox<>();
		vehicleTypeLabel = new javax.swing.JLabel();
		getTicketButton = new javax.swing.JButton();
		ticketDetailsPanel = new javax.swing.JPanel();
		ticketCodeLabel = new javax.swing.JLabel();
		floorLabel = new javax.swing.JLabel();
		parkingSpaceLabel = new javax.swing.JLabel();
		ticketCode = new javax.swing.JTextField();
		floor = new javax.swing.JTextField();
		parkingSpaceNumber = new javax.swing.JTextField();

		ticketDetailsPanel.setVisible(false);

		selectVehicleTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select vehicle type",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 12))); // NOI18N

		vehicleTypeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		vehicleTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "motorcycle", "car", "van", "truck", "trailer" }));

		vehicleTypeLabel.setText("Vehicle type:");

		getTicketButton.setText("Get ticket");
		getTicketButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getTicketButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout selectVehicleTypePanelLayout = new javax.swing.GroupLayout(selectVehicleTypePanel);
		selectVehicleTypePanel.setLayout(selectVehicleTypePanelLayout);
		selectVehicleTypePanelLayout.setHorizontalGroup(selectVehicleTypePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(selectVehicleTypePanelLayout.createSequentialGroup()
						.addGroup(selectVehicleTypePanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(selectVehicleTypePanelLayout.createSequentialGroup().addContainerGap()
										.addComponent(vehicleTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 112,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(1, 1, 1).addComponent(vehicleTypeComboBox,
												javax.swing.GroupLayout.PREFERRED_SIZE, 125,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(selectVehicleTypePanelLayout.createSequentialGroup().addGap(45, 45, 45)
										.addComponent(getTicketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 149,
												javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(31, Short.MAX_VALUE)));
		selectVehicleTypePanelLayout.setVerticalGroup(selectVehicleTypePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(selectVehicleTypePanelLayout.createSequentialGroup().addGap(21, 21, 21)
						.addGroup(selectVehicleTypePanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(vehicleTypeLabel).addComponent(vehicleTypeComboBox,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(31, 31, 31).addComponent(getTicketButton)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		ticketDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ticket details",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 12))); // NOI18N

		ticketCodeLabel.setText("Ticket code:");

		floorLabel.setText("Floor:");

		parkingSpaceLabel.setText("Parking lot number:");

		ticketCode.setEditable(false);

		floor.setEditable(false);

		parkingSpaceNumber.setEditable(false);

		javax.swing.GroupLayout ticketDetailsPanelLayout = new javax.swing.GroupLayout(ticketDetailsPanel);
		ticketDetailsPanel.setLayout(ticketDetailsPanelLayout);
		ticketDetailsPanelLayout.setHorizontalGroup(ticketDetailsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(ticketDetailsPanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(ticketDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(ticketCodeLabel).addComponent(floorLabel).addComponent(parkingSpaceLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
						.addGroup(ticketDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(ticketCode).addComponent(floor).addComponent(parkingSpaceNumber,
										javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
						.addContainerGap()));
		ticketDetailsPanelLayout.setVerticalGroup(ticketDetailsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(ticketDetailsPanelLayout.createSequentialGroup().addGap(21, 21, 21)
						.addGroup(ticketDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(ticketCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(ticketCode, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(ticketDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(floorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(floor, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(ticketDetailsPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(parkingSpaceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(parkingSpaceNumber, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(176, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(selectVehicleTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(ticketDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(24, 24, 24)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(selectVehicleTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(ticketDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
	}

	/**
	 * @param vehicleType
	 *            - specifies the category of the parking space
	 * @return Ticket if there is an available parking space
	 */
	public Ticket generateTicket(int vehicleType) {
		Calendar calendar = Calendar.getInstance();
		ParkingLot parkingLot = parkingSpaceDAO.getFirstAvailable();
		
		if(parkingLot == null)
			return null;
		parkingLot.setAvailability(false);

		++TicketTab.ticketNumber;

		// update parking space state to unavailable:
		parkingSpaceDAO.updateParkingAvailability(parkingLot.getLotNo(), false);

		/** code = date + time + vehicle type + ticket number */
		String code = datePlusTime.format(calendar.getTime()) + "." + vehicleType + ticketNumber;

		return ticketDAO.insertTicket(new Ticket(code, parkingLot.getFloorNo(), parkingLot.getLotNo()));
	}

	public void clearTicketDetails() {
		ticketCode.setText("");
		floor.setText("");
		parkingSpaceNumber.setText("");
		ticketDetailsPanel.setVisible(false);
	}

	private void getTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int vehicleType = vehicleTypeComboBox.getSelectedIndex() + 1;
		Ticket ticket = generateTicket(vehicleType);
		if (ticket != null) {
			ticketCode.setText(ticket.getTicketCode());
			floor.setText("" + ticket.getFloorNo());
			parkingSpaceNumber.setText(""+ticket.getLotNo());
			ticketDetailsPanel.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "There is no available parking lot");
		}
	}
}
