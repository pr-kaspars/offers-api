Offers
======

Simple API to access and manage offers.

[H2 Console](http://localhost:8080/h2-console)

### GET /offers

Returns a list of active offers. Supported currencies: _`[EUR, JPY, GBP, USD]`_.

    $ curl -XGET 'http://localhost:8080/offers'

Response:

    [
        {
            "currency": "GBP",
            "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "expires": "2019-03-15",
            "id": 7,
            "name": "Item 1",
            "price": 123.9,
            "state": "ACTIVE"
        },
        {
            "currency": "USD",
            "description": "Vestibulum ultricies at eros vitae fermentum.",
            "expires": "2019-03-15",
            "id": 8,
            "name": "Item 2",
            "price": 0.85,
            "state": "ACTIVE"
        }
    ]

### POST /offers

Creates an offer.

    $ curl -XPOST 'http://localhost:8080/offers' \
        -H 'Content-Type: application/json' \
        -d '{"name":"Foo","description":"Lorem ipsum","currency":"GBP","price":89.99,"duration":2}'

Payload:

    {
        "currency": "GBP",
        "description": "Lorem ipsum",
        "duration": 2,
        "name": "Foo",
        "price": 89.99
    }

Response:

    {
        "currency": "GBP",
        "description": "Lorem ipsum",
        "expires": "2019-03-10",
        "id": 6,
        "name": "Foo",
        "price": 89.99,
        "state": "ACTIVE"
    }


### GET /offers/{id}

Returns resource identified by the _id_.

    $ curl -XGET 'http://localhost:8080/offers/6'

Response:

    {
        "currency": "GBP",
        "description": "Lorem ipsum",
        "expires": "2019-03-10",
        "id": 6,
        "name": "Foo",
        "price": 89.99,
        "state": "ACTIVE"
    }

### PATCH /offers/{id}

Updates resource identified by the _id_.

    $ curl -XPATCH 'http://localhost:8080/offers/6' \
        -H 'Content-Type: application/json' \
        -d '{"state":"CANCELLED"}'

Payload:

    {
        "state": "CANCELLED"
    }

Response:

    None
