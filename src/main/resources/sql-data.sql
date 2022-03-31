INSERT INTO `ims`.`customers` (`first_name`, `surname`) VALUES
	('Jordan', 'Harrison'),
    ('Asher', 'Newbigging'),
    ('Jane', 'Smith');

INSERT INTO `ims`.`items` (`name`, `price`) VALUES
	("Bodyboard", 19.99),
    ("Surfboard", 24.99),
    ("Skateboard", 16.99);

INSERT INTO `ims`.`orders` (`customer_id`) VALUES (1), (2), (3);

INSERT INTO `ims`.`order_items` (`order_id`, `item_id`, `quantity`) VALUES
	(1, 2, 5),
    (1, 3, 3),
    (2, 3, 2);

REPLACE INTO `order_items` (`order_id`, `item_id`, `quantity`) VALUES 
	(1, 2, 7),
    (1, 3, 4),
    (1, 1, 2),
    (2, 1, 2);