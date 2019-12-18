create or replace package Parking_Lot_pkg as
procedure getAllParkingLots(c_parkinglot OUT SYS_REFCURSOR);
procedure getLastParkingLotId(pli OUT NUMBER);
procedure insertParkingLot(
	v_floor_no in Parking_Lot.floor_no%TYPE,
	v_availability in Parking_Lot.availability%TYPE,
	v_lot_id OUT Parking_Lot.lot_id%TYPE);

procedure updateParkingAvailability(
	v_availability in Parking_Lot.availability%TYPE,
	v_lot_id in Parking_Lot.lot_id%TYPE);

procedure countAvailableLots(
	v_count out NUMBER);

procedure countOccupiedLots(
	v_count out NUMBER);
	
procedure getParkingLot(
    v_lot_id IN Parking_Lot.lot_id%TYPE,
    c_Parking_Lot OUT SYS_REFCURSOR);

procedure getFirstAvailable(
    v_lot_id OUT Parking_Lot.lot_id%TYPE);

procedure deleteParkingLot(
	v_lot_id in Parking_Lot.lot_id%TYPE);

procedure checkAvailability(
	v_lot_id in Parking_Lot.lot_id%TYPE,
	v_availability out Parking_Lot.availability%TYPE);

procedure subscribe(
    v_user_id IN Parking_Subscription.Users_user_id%TYPE,
    v_lot_id IN Parking_Subscription.Parking_Lot_lot_id%TYPE,
    v_expiring_date IN Parking_Subscription.expiring_date%TYPE,
    v_subs_id OUT Parking_Subscription.subs_id%TYPE,
    v_card_number IN BankAccount.card_number%TYPE,
    v_taxa IN BankAccount.account_balance%TYPE);

procedure paySubscription(
      v_card_number IN BankAccount.card_number%TYPE,
      v_taxa IN BankAccount.account_balance%TYPE);

procedure payTicket(
    v_code IN Tickets.ticket_code%TYPE,
    v_card_number IN BankAccount.card_number%TYPE,
    v_account_balance IN BankAccount.account_balance%TYPE,
    v_taxa IN BankAccount.account_balance%TYPE);

e_NOT_ENOUGH_MONEY EXCEPTION;
PRAGMA EXCEPTION_INIT(e_NOT_ENOUGH_MONEY, -20011);
end Parking_Lot_pkg;
/
create or replace package body Parking_Lot_pkg as
 procedure getAllParkingLots(c_parkinglot OUT SYS_REFCURSOR)
  IS
  BEGIN
    OPEN c_parkinglot FOR SELECT * FROM Parking_Lot;
  END getAllParkingLots;

  procedure getLastParkingLotId(pli OUT NUMBER)
  IS
  BEGIN
    SELECT MAX(lot_id) INTO pli FROM Parking_Lot;
  END getLastParkingLotId;

  procedure insertParkingLot(
    v_floor_no IN Parking_Lot.floor_no%TYPE,
    v_availability IN Parking_Lot.availability%TYPE,
    v_lot_id OUT Parking_Lot.lot_id%TYPE)
  IS
  BEGIN
    INSERT INTO Parking_Lot(floor_no, availability)
    VALUES(v_floor_no, v_availability)
    RETURNING lot_id INTO v_lot_id;
  END insertParkingLot;

  procedure getFirstAvailable(
    v_lot_id OUT Parking_Lot.lot_id%TYPE)
  IS
  BEGIN
    SELECT MIN(lot_id) INTO v_lot_id FROM Parking_Lot WHERE availability='1';
  END getFirstAvailable;

  procedure updateParkingAvailability(
    v_availability IN Parking_Lot.availability%TYPE,
    v_lot_id IN Parking_Lot.lot_id%TYPE)
  IS
  BEGIN
    UPDATE Parking_Lot SET availability=v_availability
    WHERE lot_id=v_lot_id;
  END updateParkingAvailability;

  procedure countAvailableLots(
    v_count OUT NUMBER)
  IS
  BEGIN
    SELECT COUNT(*) INTO v_count FROM Parking_Lot
    WHERE availability='1';
  END countAvailableLots;

  procedure countOccupiedLots(
    v_count OUT NUMBER)
  IS
  BEGIN
    SELECT COUNT(*) INTO v_count FROM Parking_Lot
    WHERE availability='0';
  END countOccupiedLots;

  procedure getParkingLot(
    v_lot_id IN Parking_Lot.lot_id%TYPE,
    c_Parking_Lot OUT SYS_REFCURSOR)
  IS
  BEGIN
    OPEN c_Parking_Lot FOR SELECT * FROM Parking_Lot
    WHERE lot_id = v_lot_id;
  END getParkingLot;


  procedure deleteParkingLot(v_lot_id IN Parking_Lot.lot_id%TYPE)
  IS
  BEGIN
    DELETE FROM Parking_Lot WHERE lot_id=v_lot_id;
  END deleteParkingLot;

  procedure checkAvailability(
    v_lot_id IN Parking_Lot.lot_id%TYPE,
    v_availability OUT Parking_Lot.availability%TYPE)
  IS
  BEGIN
    SELECT availability INTO v_availability
    FROM Parking_Lot WHERE lot_id=v_lot_id;
  END checkAvailability;

  procedure paySubscription(
      v_card_number IN BankAccount.card_number%TYPE,
      v_taxa IN BankAccount.account_balance%TYPE)
  IS
  v_account_balance BankAccount.account_balance%TYPE;
  v_admin_id Users.user_id%TYPE;
  v_parking_card_number BankAccount.card_number%TYPE;
  BEGIN
    SELECT account_balance INTO v_account_balance
    FROM BankAccount WHERE card_number=v_card_number; --card number is UK

    IF v_account_balance < v_taxa THEN
      RAISE Parking_Lot_pkg.e_NOT_ENOUGH_MONEY;
    END IF;

    SELECT user_id INTO v_admin_id
    FROM Users WHERE username='Teo';

    SELECT card_number INTO v_parking_card_number
    FROM BankAccount WHERE Users_user_id=v_admin_id;

    UPDATE BankAccount SET account_balance=(v_account_balance - v_taxa)
    WHERE card_number=v_card_number;

    UPDATE BankAccount SET account_balance=(account_balance + v_taxa)
    WHERE card_number=v_parking_card_number;
  END paySubscription;

  procedure subscribe(
    v_user_id IN Parking_Subscription.Users_user_id%TYPE,
    v_lot_id IN Parking_Subscription.Parking_Lot_lot_id%TYPE,
    v_expiring_date IN Parking_Subscription.expiring_date%TYPE,
    v_subs_id OUT Parking_Subscription.subs_id%TYPE,
    v_card_number IN BankAccount.card_number%TYPE,
    v_taxa IN BankAccount.account_balance%TYPE)
  IS
  BEGIN
    SET TRANSACTION NAME 'subscription_payment';
	INSERT INTO Parking_Subscription(subs_id, expiring_date, Users_user_id, Parking_Lot_lot_id)
	VALUES(v_subs_id, v_expiring_date, v_user_id, v_lot_id);
    paySubscription(v_card_number, v_taxa);
    COMMIT WORK;

    EXCEPTION
      WHEN Parking_Lot_pkg.e_NOT_ENOUGH_MONEY THEN
        ROLLBACK;
        dbms_output.put_line('Not enough money');
        RAISE_APPLICATION_ERROR(-12034, 'Not enough money.');
  END subscribe;

  procedure payTicket(
    v_code IN Tickets.ticket_code%TYPE,
    v_card_number IN BankAccount.card_number%TYPE,
    v_account_balance IN BankAccount.account_balance%TYPE,
    v_taxa IN BankAccount.account_balance%TYPE)
  IS
  v_admin_id Users.user_id%TYPE;
  v_parking_card_number BankAccount.card_number%TYPE;
  v_lot_id Tickets.Parking_Lot_lot_id%TYPE;
  BEGIN
    SET TRANSACTION NAME 'ticket_payment';
    IF v_account_balance < v_taxa THEN
      RAISE Parking_Lot_pkg.e_NOT_ENOUGH_MONEY;
    END IF;

    SELECT user_id INTO v_admin_id
    FROM Users WHERE username='Teo';

    SELECT card_number INTO v_parking_card_number
    FROM BankAccount WHERE Users_user_id=v_admin_id;

    UPDATE BankAccount SET account_balance=(account_balance + v_taxa)
    WHERE card_number=v_parking_card_number;

    SELECT Parking_Lot_lot_id INTO v_lot_id FROM Tickets WHERE ticket_code=v_code;
    Parking_Lot_pkg.updateParkingAvailability(1, v_lot_id);
    COMMIT WORK;

    EXCEPTION
      WHEN Parking_Lot_pkg.e_NOT_ENOUGH_MONEY THEN
        ROLLBACK;
        dbms_output.put_line('Not enough money to pay the ticket.');
        RAISE_APPLICATION_ERROR(-20011, 'Not enough money to pay the ticket.');
  END payTicket;
END Parking_Lot_pkg;
