BEGIN

for i in 1..10000 loop
	insert into Users (username,password,email)
	values('user'||i,'123456','user'||i||'@yahoo.com');
	
	insert into BankAccount(Users_user_id,card_number,expiring_date,cvv_code,account_balance)
	values(i,
  ''||FLOOR(DBMS_RANDOM.VALUE(1000000000000000,9999999999999999)),
  ADD_MONTHS(SYSDATE,10),
  FLOOR(DBMS_RANDOM.VALUE(100,999)),
  FLOOR(DBMS_RANDOM.VALUE(10,200)));
				
END loop;

for i in 1..100 loop
	insert into Parking_Lot(floor_no,availability)
	values(MOD(i,3),1);
END loop;


insert into Prices(day_price,night_price)
	values(10,5);

END;
/
commit work; 