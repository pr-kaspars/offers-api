insert
  into
    product (id, currency, description, name, price)
values
    (1, 'GBP', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'Item 1', 123.9),
    (2, 'USD', 'Vestibulum ultricies at eros vitae fermentum.', 'Item 2', 0.85),
    (3, 'GBP', 'Integer a risus nunc. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur gravida nunc a lobortis auctor.', 'Third item', 8.08),
    (4, 'GBP', 'Vivamus et ex aliquam risus elementum porta eu eu nibh.', 'Item #4', 100),
    (5, 'JPY', 'Etiam eros elit, efficitur vel vestibulum vel, pellentesque non metus. Donec sit amet maximus mi.', 'Item #5', 99),
    (6, 'EUR', 'Interdum et malesuada fames ac ante ipsum primis in faucibus.', 'The Thing', 99),
    (13, 'EUR', 'Foo Bar.', 'Not A Thing', 10),
    (15, 'EUR', 'Baz Qux.', 'A Thing', 33);

insert
  into
    offer (id, expires, state, product_id)
values
    (7, dateadd('DAY', 7, current_date), 0, 1),
    (8, dateadd('DAY', 7, current_date), 0, 2),
    (9, dateadd('DAY', 7, current_date), 0, 3),
    (10, dateadd('DAY', 7, current_date), 0, 4),
    (11, dateadd('DAY', 7, current_date), 0, 5),
    (12, dateadd('DAY', 7, current_date), 0, 6),
    (14, dateadd('DAY', -7, current_date), 0, 13),
    (16, dateadd('DAY', 7, current_date), 1, 15);
