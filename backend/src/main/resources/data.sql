-- Reference Data
INSERT INTO desk (id, desk_name) VALUES (1000, 'FX'), (1001, 'Rates'), (1002, 'Credit');
INSERT INTO sub_desk (id, subdesk_name, desk_id) VALUES (1000, 'FX Spot', 1000), (1001, 'FX Options', 1000), (1002, 'Rates Swaps', 1001);
INSERT INTO cost_center (id, cost_center_name, subdesk_id) VALUES (1000, 'London Trading', 1000), (1001, 'NY Trading', 1002);
INSERT INTO book (id, book_name, active, version, cost_center_id) VALUES (1000, 'FX-BOOK-1', true, 1, 1000), (1001, 'RATES-BOOK-1', true, 1, 1001);
INSERT INTO trade_type (id, trade_type) VALUES (1000, 'Spot'), (1001, 'Swap'), (1002, 'Option');
INSERT INTO trade_sub_type (id, trade_sub_type) VALUES (1000, 'Vanilla'), (1001, 'Barrier'), (1002, 'XCCY Swap'), (1003, 'IR Swap');
INSERT INTO trade_status (id, trade_status) VALUES (1000, 'NEW'), (1001, 'AMENDED'), (1002, 'TERMINATED'),(1003, 'CANCELLED'), (1004, 'LIVE'), (1005, 'DEAD');
INSERT INTO currency (id, currency) VALUES (1000, 'USD'), (1001, 'EUR'), (1002, 'GBP');
INSERT INTO leg_type (id, type) VALUES (1000, 'Fixed'), (1001, 'Floating');
INSERT INTO index_table (id, index) VALUES (1000, 'LIBOR'), (1001, 'EURIBOR');
INSERT INTO holiday_calendar (id, holiday_calendar) VALUES (1000, 'NY'), (1001, 'LON');
INSERT INTO schedule (id, schedule) VALUES (1000, 'Monthly'), (1001, 'Quarterly');
INSERT INTO business_day_convention (id, bdc) VALUES (1000, 'Following'), (1001, 'Modified Following');
INSERT INTO pay_rec (id, pay_rec) VALUES (1000, 'Pay'), (1001, 'Receive');

-- Users
INSERT INTO user_profile (id, user_type) VALUES (1000,'TRADER_SALES'), (1001,'SUPPORT'),(1002,'ADMIN'),(1003,'MO'),(1004,'SUPERUSER');
INSERT INTO application_user (id, first_name, last_name, login_id, password, active, user_profile_id, version, last_modified_timestamp) VALUES
  (1000, 'Alice', 'Smith', 'alice', 'password', true, 1002, 1, '2025-06-02T00:00:00'),
  (1001, 'Bob', 'Jones', 'bob', 'password', true, 1001, 1, '2025-06-02T00:00:00'),
  (1003, 'Simon', 'King', 'simon', 'password', true, 1000, 1, '2025-06-02T00:00:00'),
  (1004, 'Ashley', 'Lovegood', 'ashley', 'password', true, 1003, 1, '2025-06-02T00:00:00'),
  (1005, 'Joey', 'Tribbiani', 'joey', 'password', true, 1000, 1, '2025-06-02T00:00:00'),
  (1006, 'Stuart', 'McGill', 'stuart', 'password', true, 1004, 1, '2025-06-02T00:00:00');

-- Privileges
INSERT INTO privilege (id, name) VALUES (1000, 'BOOK_TRADE'), (1001, 'AMEND_TRADE'),(1002, 'READ_TRADE'), (1003, 'READ_USER'), (1004,'WRITE_USER'), (1005,'READ_STATIC_DATA'), (1006,'WRITE_STATIC_DATA');

-- User Privileges (Fixed to match UserPrivilege entity structure - no separate id column)
INSERT INTO user_privilege (user_id, privilege_id) VALUES (1000, 1000), (1001, 1001), (1000, 1002), (1003, 1000), (1004, 1001), (1005, 1000);

-- Counterparties
INSERT INTO counterparty (id, name, address, phone_number, internal_code, created_date, last_modified_date, active) VALUES
  (1000, 'BigBank', '1 Bank St', '123-456-7890', 1001, '2024-01-01', '2025-06-02', true),
  (1001, 'MegaFund', '2 Fund Ave', '987-654-3210', 1002, '2024-01-01', '2025-06-02', true);

-- Sample Trades
INSERT INTO trade (id, trade_id, version, book_id, counterparty_id, trader_user_id, inputter_user_id, trade_type_id, trade_sub_type_id, trade_status_id,
                   trade_date, trade_start_date, trade_maturity_date, trade_execution_date, uti_code, last_touch_timestamp, validity_start_date, validity_end_date,
                   active, created_date, deactivated_date) VALUES
  (1000, 100001, 1, 1000, 1000, 1003, 1003, 1001, 1003, 1004, '2024-06-01', '2024-06-03', '2029-06-03', '2024-06-01', 'UTI-001', '2024-06-01T10:30:00', '2024-06-01', null, true, '2024-06-01T10:30:00', null),
  (1001, 100002, 1, 1001, 1001, 1005, 1005, 1000, 1000, 1004, '2024-06-02', '2024-06-02', '2024-06-04', '2024-06-02', 'UTI-002', '2024-06-02T11:15:00', '2024-06-02', null, true, '2024-06-02T11:15:00', null);

-- Sample Trade Legs
INSERT INTO trade_leg (leg_id, notional, rate, trade_id, currency_id, leg_rate_type_id, index_id, holiday_calendar_id,
                       calculation_period_schedule_id, payment_business_day_convention_id, fixing_business_day_convention_id, pay_rec_id,
                       active, created_date, deactivated_date) VALUES
  (1000, 1000000.00, 0.05, 1000, 1000, 1000, null, 1000, 1001, 1000, 1000, 1000, true, '2024-06-01T10:30:00', null),
  (1001, 1000000.00, 0.0, 1000, 1000, 1001, 1000, 1000, 1001, 1000, 1000, 1001, true, '2024-06-01T10:30:00', null),
  (1002, 5000000.00, 0.045, 1001, 1001, 1000, null, 1001, 1000, 1001, 1001, 1000, true, '2024-06-02T11:15:00', null);

-- Sample Cashflows
INSERT INTO cashflow (id, payment_value, value_date, rate, leg_id, pay_rec_id, payment_type_id, payment_business_day_convention_id,
                      active, created_date, validity_start_date, validity_end_date) VALUES
  (1000, 12500.00, '2024-09-01', 0.05, 1000, 1000, 1000, 1000, true, '2024-06-01T10:30:00', '2024-06-01', null),
  (1001, 12500.00, '2024-12-01', 0.05, 1000, 1000, 1000, 1000, true, '2024-06-01T10:30:00', '2024-06-01', null),
  (1002, 225000.00, '2024-06-04', 0.045, 1002, 1000, 1000, 1001, true, '2024-06-02T11:15:00', '2024-06-02', null);
