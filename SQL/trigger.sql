create or replace trigger trg_expiring_date_BRIU 
	before insert or update on BankAccount
		for each row
begin
	if(:new.expiring_date<=SYSDATE) then
	RAISE_APPLICATION_ERROR(-10123,'Card expirat!');
	end if;
	if REGEX_SUBSTR(:new.card_number,'[0-9]{16}',1,1,'i') 
	is null then RAISE_APPLICATION_ERROR(-10124,'Numar de card invalid');
	end if;
	if not(:new.cvv_code>=100 AND :new.cvv_code<=999) then
	RAISE_APPLICATION_ERROR(-10125,'CVV invalid');
	end if;
	if (:new.account_balance<=0) then
	RAISE_APPLICATION_ERROR(-10126,'No money!');
	end if;

end;


CREATE OR REPLACE TRIGGER trg_Parking_subscription_BRIU 
    BEFORE INSERT OR UPDATE ON Parking_Subscription
    FOR EACH ROW 
BEGIN
  IF( :new.expiring_date <= SYSDATE )
  THEN
    RAISE_APPLICATION_ERROR(-20001,
          'Data invalida: ' || TO_CHAR( :new.expiring_date, 'DD.MM.YYYY HH24:MI:SS' ) || ' trebuie sa fie mai mare decat data curenta.' );
  END IF;
END; 




CREATE OR REPLACE TRIGGER trg_users_BRIU 
    BEFORE INSERT OR UPDATE ON Users 
    FOR EACH ROW 
BEGIN
  IF REGEXP_SUBSTR(:new.username, '[a-zA-Z0-9]{5,15}', 1, 1, 'i') IS NULL THEN
      RAISE_APPLICATION_ERROR(-20003, 'Username should contain only letters and digits. Username length is [5, 15].');
    END IF;
    IF REGEXP_SUBSTR(:new.email, '[a-z0-9._%-]+@[a-z0-9._%-]+\.[a-z]{2,4}', 1, 1, 'i') IS NULL THEN
      RAISE_APPLICATION_ERROR(-20004, 'Invalid email');
    END IF;
    IF not (length(:new.password) > 5 AND length(:new.password) < 16) THEN
      RAISE_APPLICATION_ERROR(-20005, 'Password length is [5, 15].');
    END IF;
    IF :new.user_id < 1 THEN
    	 RAISE_APPLICATION_ERROR(-20012, 'Invalid user id');
    END IF;
END; 