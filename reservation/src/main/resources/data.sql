INSERT INTO stores (store_id, name, max_capacity, image_url, created_at) VALUES
('store-1', '맛있는 한식당', 30, 'https://images.unsplash.com/photo-1555396273', CURRENT_TIMESTAMP),
('store-2', '화덕 피자 하우스', 20, NULL, CURRENT_TIMESTAMP),
('store-3', '스시 오마카세', 12, NULL, CURRENT_TIMESTAMP);

INSERT INTO slots (slot_id, store_id, slot_date, time_block, is_available, max_people) VALUES
('SLOT202604211', 'store-1', '2026-04-02', '18:00', TRUE, 10),
('SLOT202604212', 'store-1', '2026-04-02', '18:30', TRUE, 8),
('SLOT202604213', 'store-1', '2026-04-02', '19:00', TRUE, 10),
('SLOT202604311', 'store-1', '2026-04-03', '18:00', TRUE, 10);

INSERT INTO menus (menu_id, store_id, name, price, is_required) VALUES
('MENU202604211', 'store-1', '김치찌개', 9000, FALSE),
('MENU202604212', 'store-1', '된장찌개', 8000, FALSE),
('MENU202604213', 'store-1', '불고기', 15000, FALSE),
('MENU202604214', 'store-1', '공기밥', 1000, TRUE);

INSERT INTO min_order_rules (rule_id, store_id, min_headcount, max_headcount, min_order_amount) VALUES
('RULE202604211', 'store-1', 1, 5, 50000),
('RULE202604212', 'store-1', 6, 15, 100000),
('RULE202604213', 'store-1', 16, 30, 200000);
