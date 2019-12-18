package ro.bd.parkingmanagement.UI;

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
	private javax.swing.JTextField ticketCode;
	private javax.swing.JLabel ticketCodeLabel;
	private javax.swing.JPanel ticketDetailsPanel;
	private static ParkingLotDAO parkingSpaceDAO = new ParkingLotDAOImpl();
	private static TicketDAO ticketDAO = new TicketDAOImpl();

	public TicketTab() {
		initComponents();
	}

	private void initComponents() {

		ticketDetailsPanel = new javax.swing.JPanel();
        ticketCodeLabel = new javax.swing.JLabel();
        floorLabel = new javax.swing.JLabel();
        parkingSpaceLabel = new javax.swing.JLabel();
        ticketCode = new javax.swing.JTextField();
        floor = new javax.swing.JTextField();
        parkingSpaceNumber = new javax.swing.JTextField();
        getTicketButton = new javax.swing.JButton();

        ticketDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ticket", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        ticketCodeLabel.setText("Ticket code:");

        floorLabel.setText("Floor:");

        parkingSpaceLabel.setText("Parking lot number:");

        ticketCode.setEditable(false);

        floor.setEditable(false);

        parkingSpaceNumber.setEditable(false);

        javax.swing.GroupLayout ticketDetailsPanelLayout = new javax.swing.GroupLayout(ticketDetailsPanel);
        ticketDetailsPanel.setLayout(ticketDetailsPanelLayout);
        ticketDetailsPanelLayout.setHorizontalGroup(
            ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ticketDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ticketCodeLabel)
                    .addComponent(floorLabel)
                    .addComponent(parkingSpaceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ticketCode)
                    .addComponent(floor)
                    .addComponent(parkingSpaceNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );
        ticketDetailsPanelLayout.setVerticalGroup(
            ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ticketDetailsPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ticketCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ticketCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(floorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(floor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ticketDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parkingSpaceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parkingSpaceNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        getTicketButton.setText("Get ticket");
        getTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getTicketButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ticketDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(getTicketButton, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(getTicketButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(ticketDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
	}

	/**
	 * @return Ticket if there is an available parking lot
	 */
	public Ticket generateTicket() {
		Calendar calendar = Calendar.getInstance();
		ParkingLot parkingLot = parkingSpaceDAO.getFirstAvailable();
		
		if(parkingLot == null)
			return null;
		parkingLot.setAvailability(false);

		// update parking space state to unavailable:
		parkingSpaceDAO.updateParkingAvailability(parkingLot.getLotNo(), false);

		/** code = time in milliseconds */
		String code = calendar.getTime().toString();

		return ticketDAO.insertTicket(new Ticket(code, parkingLot.getFloorNo(), parkingLot.getLotNo()));
	}

	public void clearTicketDetails() {
		ticketCode.setText("");
		floor.setText("");
		parkingSpaceNumber.setText("");
		ticketDetailsPanel.setVisible(false);
	}

	private void getTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Ticket ticket = generateTicket();
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
