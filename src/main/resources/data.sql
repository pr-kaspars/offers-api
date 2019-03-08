insert
  into
    product (id, currency, description, name, price)
values
    (100001, 'GBP', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 'Item 1', 123.9),
    (100002, 'USD', 'Vestibulum ultricies at eros vitae fermentum.', 'Item 2', 0.85),
    (100003, 'GBP', 'Integer a risus nunc. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur gravida nunc a lobortis auctor.', 'Third item', 8.08),
    (100004, 'GBP', 'Vivamus et ex aliquam risus elementum porta eu eu nibh.', 'Item #4', 100),
    (100005, 'JPY', 'Etiam eros elit, efficitur vel vestibulum vel, pellentesque non metus. Donec sit amet maximus mi.', 'Item #5', 99),
    (100006, 'EUR', 'Interdum et malesuada fames ac ante ipsum primis in faucibus.', 'The Thing', 99),
    (100013, 'EUR', 'Foo Bar.', 'Not A Thing', 10),
    (100015, 'EUR', 'Baz Qux.', 'A Thing', 33);

insert
  into
    offer (id, expires, state, product_id)
values
    (100007, dateadd('DAY', 7, current_date), 0, 100001),
    (100008, dateadd('DAY', 7, current_date), 0, 100002),
    (100009, dateadd('DAY', 7, current_date), 0, 100003),
    (100010, dateadd('DAY', 7, current_date), 0, 100004),
    (100011, dateadd('DAY', 7, current_date), 0, 100005),
    (100012, dateadd('DAY', 7, current_date), 0, 100006),
    (100014, dateadd('DAY', -7, current_date), 0, 100013),
    (100016, dateadd('DAY', 7, current_date), 1, 100015);
