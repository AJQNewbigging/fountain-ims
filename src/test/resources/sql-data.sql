INSERT INTO `customers` (`first_name`, `surname`) VALUES
	('Jordan', 'Harrison'),
    ('Asher', 'Newbigging'),
    ('Jane', 'Smith');

INSERT INTO `items` (`name`, `price`) VALUES
	("Bodyboard", 19.99),
    ("Surfboard", 24.99),
    ("Skateboard", 16.99);

INSERT INTO `orders` (`customer_id`) VALUES (2), (3);

INSERT INTO `order_items` (`order_id`, `item_id`, `quantity`) VALUES
	(2, 2, 5),
    (2, 3, 3),
    (3, 3, 2);